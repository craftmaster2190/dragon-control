package com.craftmaster2190.halloween.dragoncontrol;

import java.time.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import static com.craftmaster2190.halloween.dragoncontrol.ActuatorState.State.OPENING_NEGATIVE;

@Component
@Data
public class Dragon {
  private final DragonHead head;
  private final DragonLongNeck longNeck;

  private State state = State.ASLEEP;

  public void stop() {
    head.stop();
    longNeck.stop();
  }

  public void reset() throws InterruptedException {
    state = State.RESETTING;
    stop();
    ActuatorState[] parts = new ActuatorState[]{ head.getJaw(), head.getLeftNeck(), head.getRightNeck(), longNeck.getLowerNeckLeft(), longNeck.getLowerNeckRight() };
    for (var part : parts) {
      part.requestMoveToZero();
    }

    for(Instant start = Instant.now();
        Duration.between(start, Instant.now()).compareTo(Duration.ofSeconds(21)) < 0;
        Thread.currentThread().sleep(1000)) {
      for (var part : parts) {
        part.forceState(OPENING_NEGATIVE);
      }
    }
    stop();
    state = State.ASLEEP;
  }

  enum State {
    RESETTING,
    ASLEEP,
    AWAKE,
    SITTING_UP_TO_ROAR,
    ROARING
  }
}
