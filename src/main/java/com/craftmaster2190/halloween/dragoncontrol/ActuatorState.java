package com.craftmaster2190.halloween.dragoncontrol;

import com.fasterxml.jackson.annotation.*;
import jakarta.annotation.Nullable;
import java.time.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Synchronized;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@JsonAutoDetect(fieldVisibility = NONE, getterVisibility = NONE) // Disable auto-detection of fields
public class ActuatorState {
  private static final Duration STEP = Duration.ofMillis(100);

  @JsonProperty
  private final String name;
  @JsonProperty
  private final Duration max;
  private final Runnable doOnOpenPositive;
  private final Runnable doOnOpenNegative;
  private final Runnable doOnStop;
  private final AtomicReference<Duration> targetPositiveElapsed = new AtomicReference<>(Duration.ZERO);
  private final AtomicReference<ScheduledFuture<?>> thread = new AtomicReference<>(); // TODO Move to watchdog thread
  private final ScheduledExecutorService executorService;

  @JsonProperty
  private State state = State.STOPPED;
  @Nullable
  private Instant lastUpdate;

  @JsonProperty
  private Duration currentPositiveElapsed = Duration.ZERO;

  public ActuatorState(String name, Duration max, Runnable doOnOpenPositive, Runnable doOnOpenNegative, Runnable doOnStop) {
    this.name = name;
    this.max = max;
    this.doOnOpenPositive = doOnOpenPositive;
    this.doOnOpenNegative = doOnOpenNegative;
    this.doOnStop = doOnStop;
    executorService = Executors.newSingleThreadScheduledExecutor(runnable -> {
      Thread newThread = new Thread(runnable, "ActuatorState-" + name);
      newThread.setDaemon(true);
      return newThread;
    });
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

  @Synchronized
  private void initStepThreadIfNotAlreadyStarted() {
    ScheduledFuture<?> scheduledFuture1 = thread.get();
    if (scheduledFuture1 != null && !scheduledFuture1.isDone() && !scheduledFuture1.isCancelled()) {
      return;
    }
    thread.set(executorService.scheduleAtFixedRate(this::doStep, // Do update every 100ms
        0, STEP.toMillis(), TimeUnit.MILLISECONDS));
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
          thread
              .get()
              .cancel(false);
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
    Duration min = Duration.ZERO;
    Duration max1 = max;
    target = DurationUtils.clampDuration(target, min, max1);
    targetPositiveElapsed.set(target);
    initStepThreadIfNotAlreadyStarted();
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
