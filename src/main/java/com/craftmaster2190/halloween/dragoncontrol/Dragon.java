package com.craftmaster2190.halloween.dragoncontrol;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Dragon {
  private final DragonHead head;
  private final DragonLongNeck longNeck;
  private final DragonRightWing rightWing;
  private final SoundPlayer soundPlayer;

  public void sleep() {

  }

  public void wakeUp() {

  }

  public void roar() {

  }
}
