package com.craftmaster2190.halloween.dragoncontrol.behavior;

import com.craftmaster2190.halloween.dragoncontrol.*;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class Roar1Behavior implements Behavior {
  private final Dragon dragon;
  private final SoundPlayer soundPlayer;

  public Roar1Behavior(Dragon dragon, @Value("classpath:roar1.wav") Resource soundfile) {
    this.dragon = dragon;
    this.soundPlayer = new SoundPlayer(soundfile);
  }

  @Override
  public void perform() throws InterruptedException {
    dragon.getHead().getJaw().requestMoveToMax();
    soundPlayer.play();
    TimeUnit.SECONDS.sleep(3);
    dragon.getHead().getJaw().requestMoveToZero();
    TimeUnit.SECONDS.sleep(3);
  }
}
