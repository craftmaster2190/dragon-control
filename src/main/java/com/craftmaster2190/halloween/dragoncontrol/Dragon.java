package com.craftmaster2190.halloween.dragoncontrol;

import com.fasterxml.jackson.annotation.*;
import java.time.Duration;
import lombok.Data;
import org.springframework.stereotype.Component;

import static com.craftmaster2190.halloween.dragoncontrol.ActuatorState.State.OPENING_NEGATIVE;

@Component
@Data
public class Dragon {
  private final DragonHead head;
  private final DragonLongNeck longNeck;
  private final DragonRightWing rightWing;
  @JsonIgnore
  private final SoundPlayerService soundPlayerService;

  private State state = State.ASLEEP;

  public void stop() {
    head.stop();
    longNeck.stop();
    rightWing.stop();
  }

  public void reset() throws InterruptedException {
    state = State.RESETTING;
    stop();
    Thread.currentThread().sleep(500);
    head.getJaw().forceState(OPENING_NEGATIVE);
    head.getLeftNeck().forceState(OPENING_NEGATIVE);
    head.getRightNeck().forceState(OPENING_NEGATIVE);
    longNeck.getLowerNeckLeft().forceState(OPENING_NEGATIVE);
    longNeck.getLowerNeckRight().forceState(OPENING_NEGATIVE);
    rightWing.getRightHand().forceState(OPENING_NEGATIVE);
    Thread.currentThread().sleep(Duration.ofSeconds(20).toMillis());
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

  public void sleep() {
    state = State.ASLEEP;
    longNeck.sleep();
    rightWing.retract();
    // head.sleepPosition();
    // wait for all to finish
    // head.closeEyes();
  }

  public void wakeUp() {
    state = State.AWAKE;
    // Go to sleep after some time, maybe 60 seconds
    // move around a bit
    // growl soundPlayer.playRandomGrowl();
  }

  public void roar() {
    state = State.SITTING_UP_TO_ROAR;
    // TODO Wait for neck to extend
    state = State.ROARING;
    soundPlayerService.playRoar();
    // TODO Move back to AWAKE after roar is done
  }
}
