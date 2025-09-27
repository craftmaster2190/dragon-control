package com.craftmaster2190.halloween.dragoncontrol;

import jakarta.annotation.Nullable;
import java.time.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class ActuatorState {
  private static final Duration STEP = Duration.ofMillis(100);
  private final Duration max;
  private final Runnable doOnOpenPositive;
  private final Runnable doOnOpenNegative;
  private final Runnable doOnStop;
  private final AtomicReference<Duration> targetPositiveElapsed = new AtomicReference<>(Duration.ZERO);
  private final AtomicReference<ScheduledFuture<?>> thread = new AtomicReference<>(null);
  private final ScheduledExecutorService executorService;
  private State state = State.STOPPED;
  @Nullable
  private Instant lastUpdate;
  private Duration currentPositiveElapsed = Duration.ZERO;

  public ActuatorState(String name, Duration max, Runnable doOnOpenPositive, Runnable doOnOpenNegative, Runnable doOnStop) {
    this.max = max;
    this.doOnOpenPositive = doOnOpenPositive;
    this.doOnOpenNegative = doOnOpenNegative;
    this.doOnStop = doOnStop;
    executorService = Executors.newSingleThreadScheduledExecutor(runnable -> {
      Thread thread = new Thread(runnable, "ActuatorState-" + name);
      thread.setDaemon(true);
      return thread;
    });
  }

  private void initThread() {
    ScheduledFuture<?> scheduledFuture1 = thread.get();
    if (scheduledFuture1 != null && !scheduledFuture1.isDone() && !scheduledFuture1.isCancelled()) {
      return;
    }
    thread.set(executorService.scheduleAtFixedRate(this::doStep, // Do update every 100ms
        0, STEP.toMillis(), TimeUnit.MILLISECONDS));
  }

  private void doStep() {
    Duration targetPositiveElapsed$ = targetPositiveElapsed.get();
    State newState;
    if (targetPositiveElapsed$.compareTo(currentPositiveElapsed) > 0) {
      newState = State.OPENING_POSITIVE;
    }
    else if (targetPositiveElapsed$.compareTo(currentPositiveElapsed) < 0) {
      newState = State.OPENING_NEGATIVE;
    }
    else {
      newState = State.STOPPED;
    }

    var step$ = lastUpdate == null ? Duration.ZERO : Duration.between(lastUpdate, Instant.now());
    lastUpdate = Instant.now();
    switch (state) {
      case OPENING_POSITIVE -> {
        currentPositiveElapsed = currentPositiveElapsed.plus(step$);
        if (currentPositiveElapsed.compareTo(max) > 0) {
          currentPositiveElapsed = max;
          newState = State.STOPPED;
        }
      }
      case OPENING_NEGATIVE -> {
        currentPositiveElapsed = currentPositiveElapsed.minus(step$);
        if (currentPositiveElapsed.compareTo(Duration.ZERO) < 0) {
          currentPositiveElapsed = Duration.ZERO;
          newState = State.STOPPED;
        }
      }
    }

    if (newState != state) {
      state = newState;
      switch (state) {
        case OPENING_POSITIVE -> {
          doOnOpenPositive.run();
        }
        case OPENING_NEGATIVE -> {
          doOnOpenNegative.run();
        }
        case STOPPED -> {
          doOnStop.run();
          ScheduledFuture<?> thread$ = thread.get();
          thread$.cancel(false);
        }
      }
    }
  }

  public void requestMoveTo(Duration target) {
    targetPositiveElapsed.set(target);
    initThread();
  }

  private enum State {
    OPENING_POSITIVE,
    OPENING_NEGATIVE,
    STOPPED
  }
}
