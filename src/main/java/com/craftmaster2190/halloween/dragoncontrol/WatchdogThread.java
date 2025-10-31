package com.craftmaster2190.halloween.dragoncontrol;

import java.io.Closeable;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WatchdogThread implements Closeable {
  private static final Duration STEP = Duration.ofMillis(100);
  private final AtomicReference<ScheduledFuture<?>> thread = new AtomicReference<>();
  private final ScheduledExecutorService executorService;
  private final String name;
  private final Runnable doStep;

  public WatchdogThread(String name, Runnable doStep) {
    executorService = Executors.newSingleThreadScheduledExecutor(runnable ->
       Thread.ofVirtual().name("W-" + name).unstarted(runnable)
    );
    this.name = name;
    this.doStep = doStep;
  }

  public WatchdogThread start() {
    initStepThreadIfNotAlreadyStarted();
    return this;
  }

  public void stop() {
    ScheduledFuture<?> scheduledFuture = thread.getAndSet(null);
    if (scheduledFuture != null) {
      log.info("Stopping watchdog thread-{}", name);
      scheduledFuture.cancel(false);
    }
  }


  @Synchronized
  private void initStepThreadIfNotAlreadyStarted() {
    ScheduledFuture<?> scheduledFuture1 = thread.get();
    if (scheduledFuture1 != null && !scheduledFuture1.isDone() && !scheduledFuture1.isCancelled()) {
      return;
    }
    log.info("Starting watchdog thread-{}", name);
    thread.set(executorService.scheduleAtFixedRate(doStep, // Do update every 100ms
        0, STEP.toMillis(), TimeUnit.MILLISECONDS));
  }

  @Override
  public void close() {
    stop();
    executorService.shutdown();
  }
}
