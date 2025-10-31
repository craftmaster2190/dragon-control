package com.craftmaster2190.halloween.dragoncontrol;

import com.fasterxml.jackson.annotation.*;
import jakarta.annotation.Nullable;
import java.time.*;
import java.util.concurrent.atomic.AtomicReference;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@Slf4j
@JsonAutoDetect(fieldVisibility = NONE, getterVisibility = NONE) // Disable auto-detection of fields
public class ActuatorState {

  public static final WhenStateAdjustment OPENS_FASTER = new WhenStateAdjustment(
      State.OPENING_NEGATIVE,
      0.1);
  public static final WhenStateAdjustment CLOSES_FASTER = new WhenStateAdjustment(
      State.OPENING_POSITIVE,
      0.1);
  @JsonProperty
  private final String name;
  @JsonProperty
  private final Duration max;
  private final WhenStateAdjustment whenStateAdjustment;
  private final Runnable doOnOpenPositive;
  private final Runnable doOnOpenNegative;
  private final Runnable doOnStop;
  private final AtomicReference<Duration> targetPositiveElapsed = new AtomicReference<>(Duration.ZERO);
  private final WatchdogThread thread;
  /**
   * Used for raw access to the switch
   */
  @Getter
  private final Chicago3WaySwitch actuatorSwitch;
  @JsonProperty
  private volatile State state = State.STOPPED;
  @Nullable
  private volatile Instant lastUpdate;
  @JsonProperty
  private volatile Duration currentPositiveElapsed = Duration.ZERO;

  private ActuatorState(String name, Duration max, WhenStateAdjustment whenStateAdjustment,
                        Runnable doOnOpenPositive, Runnable doOnOpenNegative, Runnable doOnStop, Chicago3WaySwitch actuatorSwitch) {
    this.name = name;
    this.max = max;
    this.whenStateAdjustment = whenStateAdjustment;
    this.doOnOpenPositive = doOnOpenPositive;
    this.doOnOpenNegative = doOnOpenNegative;
    this.doOnStop = doOnStop;
    thread = new WatchdogThread(name, this::doStep);
    this.actuatorSwitch = actuatorSwitch;
  }

  public ActuatorState(String name, Duration max, WhenStateAdjustment whenStateAdjustment, Chicago3WaySwitch actuatorSwitch) {
    this(name, max, whenStateAdjustment,
        () -> actuatorSwitch.setState(Chicago3WaySwitch.State.UP),
        () -> actuatorSwitch.setState(Chicago3WaySwitch.State.DOWN),
        () -> actuatorSwitch.setState(Chicago3WaySwitch.State.OFF), actuatorSwitch);
  }

  @Override
  public String toString() {
    var stateString = switch (state) {
      case OPENING_POSITIVE, OPENING_NEGATIVE -> ", target=" + targetPositiveElapsed;
      default -> "";
    };

    return "ActuatorState(" + name + ", " + state +
        ", current=" + DurationUtils.formatDuration(currentPositiveElapsed) + "/" + DurationUtils.formatDuration(max) +
        " (" + getPercent() + ")" +
        stateString +
        ")";
  }

  @JsonProperty
  public Percent getPercent() {
    return new Percent(currentPositiveElapsed.toMillis(), max.toMillis());
  }

  private void doStep() {
    State newState = determineNewState();

    // The actual step may vary from the 100ms we requested, so calculate the actual time passed
    var actualStepValue = lastUpdate == null ? Duration.ZERO : Duration.between(lastUpdate, Instant.now());
    lastUpdate = Instant.now();
    log.debug("{}: Step called, actual step value: {}, current state: {}, new state: {}, target: {}, current: {}",
        name, actualStepValue, state, newState, targetPositiveElapsed.get(), currentPositiveElapsed);

    actualStepValue = whenStateAdjustment.adjust(newState, actualStepValue);

    // Handle update of current position
    if (newState == State.OPENING_POSITIVE) {
      currentPositiveElapsed = currentPositiveElapsed.plus(actualStepValue);
      if (currentPositiveElapsed.compareTo(max) > 0) {
        currentPositiveElapsed = max;
        newState = State.STOPPED;
      }
    }
    else if (newState == State.OPENING_NEGATIVE) {
      currentPositiveElapsed = currentPositiveElapsed.minus(actualStepValue);
      if (currentPositiveElapsed.compareTo(Duration.ZERO) < 0) {
        log.debug("{}: Current position ({}) went below zero, clamping to zero", name, currentPositiveElapsed);
        currentPositiveElapsed = Duration.ZERO;
        newState = State.STOPPED;
      }
    }

    // Handle state change
    if (state != newState) {
      forceState(newState);
    }
    if (state == State.STOPPED) {
      doStop();
    }
  }

  public void forceState(State newState) {
    state = newState;
    switch (state) {
      case OPENING_POSITIVE -> doOnOpenPositive.run();
      case OPENING_NEGATIVE -> doOnOpenNegative.run();
      case STOPPED -> doStop();
    }
  }

  private void doStop() {
    doOnStop.run();
    lastUpdate = null;
    thread.stop();
  }

  private State determineNewState() {
    State newState;
    final Duration targetPositiveElapsedLocal = targetPositiveElapsed.get();
    if (targetPositiveElapsedLocal.compareTo(currentPositiveElapsed) > 0) {
      newState = State.OPENING_POSITIVE;
    }
    else if (targetPositiveElapsedLocal.compareTo(currentPositiveElapsed) < 0) {
      newState = State.OPENING_NEGATIVE;
    }
    else {
      newState = State.STOPPED;
    }
    return newState;
  }

  public void requestMoveTo(Duration target) {
    target = DurationUtils.clampDuration(target, Duration.ZERO, max);
    targetPositiveElapsed.set(target);
    thread.start();
  }

  public void requestMoveToMax() {
    requestMoveTo(max);
  }

  public void requestMoveToZero() {
    requestMoveTo(Duration.ZERO);
  }

  public void requestMoveToPercentage(double percentage) {
    if (percentage < 0.0 || percentage > 1.0) {
      throw new IllegalArgumentException("Percentage must be between 0.0 and 1.0");
    }
    requestMoveTo(Duration.ofMillis(((long) (max.toMillis() * percentage))));
  }

  public void stop() {
    targetPositiveElapsed.set(currentPositiveElapsed);
    thread.start();
  }

  public enum State {
    OPENING_POSITIVE,
    OPENING_NEGATIVE,
    STOPPED
  }

  @Data
  public static class WhenStateAdjustment {
    private final State state;
    private final double percentage;

    public Duration adjust(State currentState, Duration currentDuration) {
      if (this.state != currentState) {
        return currentDuration;
      }
      long adjustedMillis = (long) (currentDuration.toMillis() * percentage);
      Duration newDuration = Duration.ofMillis(adjustedMillis);
      log.debug("Adjusting duration from {} to {} for state {}", currentDuration, newDuration, currentState);
      return newDuration;
    }

  }
}
