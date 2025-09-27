package com.craftmaster2190.halloween.dragoncontrol;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Dragon {
  private final DragonHead head;
  private final DragonLongNeck longNeck;
  private final DragonRightWing rightWing;
  @JsonIgnore
  private final SoundPlayer soundPlayer;

  private State state = State.ASLEEP;

  enum State {
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
    soundPlayer.playRoar();
    // TODO Move back to AWAKE after roar is done
  }
}
