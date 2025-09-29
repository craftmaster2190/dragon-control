package com.craftmaster2190.halloween.dragoncontrol;

import com.fasterxml.jackson.annotation.*;
import jakarta.annotation.Nullable;
import java.time.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@JsonAutoDetect(fieldVisibility = NONE, getterVisibility = NONE) // Disable auto-detection of fields
public class ActuatorState {

  @JsonProperty
  private final String name;
  @JsonProperty
  private final Duration max;
  private final Runnable doOnOpenPositive;
  private final Runnable doOnOpenNegative;
  private final Runnable doOnStop;
  private final AtomicReference<Duration> targetPositiveElapsed = new AtomicReference<>(Duration.ZERO);

  @JsonProperty
  private State state = State.STOPPED;
  @Nullable
  private Instant lastUpdate;

  @JsonProperty
  private Duration currentPositiveElapsed = Duration.ZERO;
  private final WatchdogThread thread;

  public ActuatorState(String name, Duration max, Runnable doOnOpenPositive, Runnable doOnOpenNegative, Runnable doOnStop) {
    this.name = name;
    this.max = max;
    this.doOnOpenPositive = doOnOpenPositive;
    this.doOnOpenNegative = doOnOpenNegative;
    this.doOnStop = doOnStop;
    thread = new WatchdogThread(name, this::doStep);
  }

  public ActuatorState(String name, Duration max, PinController pinController, Pin openPositivePin, Pin openNegativePin) {
    this(name, max, () -> {
          pinController.closePin(openNegativePin);
          pinController.openPin(openPositivePin);
        },
        () -> {
          pinController.closePin(openPositivePin);
          pinController.openPin(openNegativePin);
        },
        () -> {
          pinController.closePin(openPositivePin);
          pinController.closePin(openNegativePin);
        });
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
        currentPositiveElapsed = Duration.ZERO;
        newState = State.STOPPED;
      }
    }

    // Handle state change
    if (state != newState) {
      state = newState;
      switch (state) {
        case OPENING_POSITIVE -> doOnOpenPositive.run();
        case OPENING_NEGATIVE -> doOnOpenNegative.run();
        case STOPPED -> {
          doOnStop.run();
          thread.stop();
        }
      }
    }
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

  private enum State {
    OPENING_POSITIVE,
    OPENING_NEGATIVE,
    STOPPED
  }
}
