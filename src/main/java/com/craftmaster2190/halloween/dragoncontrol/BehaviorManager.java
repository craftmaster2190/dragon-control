package com.craftmaster2190.halloween.dragoncontrol;

import com.craftmaster2190.halloween.dragoncontrol.behavior.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@RequiredArgsConstructor
public class BehaviorManager {
  private final ResetBehavior resetBehavior;
  private final List<Behavior> behaviors;
  @Getter(lazy = true, value = PRIVATE)
  private final List<Behavior> activeBehaviors = ListUtils.copyOfAndExclude(behaviors, resetBehavior);
  private volatile Behavior currentBehavior = null;

  private final AtomicReference<Instant> lastResetTime = new AtomicReference<>(Instant.EPOCH);
  public static final Duration needsToResetEvery = Duration.ofMinutes(1);

  private final Deque<Behavior> queuedBehaviors = new ConcurrentLinkedDeque<>();

  {
    Executors.newSingleThreadExecutor(runnable -> {
      Thread newThread = new Thread(runnable, "BehaviorManager");
      newThread.setDaemon(true);
      return newThread;
    }).submit(() -> {
      while (true) {
        try {
          performNextBehavior();
        } catch (Exception e) {
          log.error("Exception occurred during behavior execution", e);
        }
      }
    });
  }

  public void requestBehavior(Behavior behavior) {
    queuedBehaviors.add(behavior);
  }

  public void performNextBehavior() throws InterruptedException {
    Behavior behavior = queuedBehaviors.poll();
    if (behavior != null) {
      behavior.perform();
    } else if (Duration.between(lastResetTime.get(), Instant.now()).compareTo(needsToResetEvery) > 0) {
      lastResetTime.set(Instant.now());
      resetBehavior.perform();
    } else {
      queuedBehaviors.add(ListUtils.random(getActiveBehaviors()));
    }
  }
}

