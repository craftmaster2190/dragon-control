package com.craftmaster2190.halloween.dragoncontrol.behavior;

import com.craftmaster2190.halloween.dragoncontrol.*;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class Roar3Behavior implements Behavior {
  private final Dragon dragon;
  private final SoundPlayer soundPlayer;

  public Roar3Behavior(Dragon dragon, @Value("classpath:loud-roar4-9s.wav") Resource soundfile) {
    this.dragon = dragon;
    this.soundPlayer = new SoundPlayer(soundfile);
  }

  @Override
  public void perform() throws InterruptedException {
    dragon.getLongNeck().setBoth(1.0);
    dragon.getHead().getRightNeck().requestMoveToPercentage(0.2);
    dragon.getHead().getLeftNeck().requestMoveToPercentage(0.5);
    TimeUnit.SECONDS.sleep(5);

    dragon.getHead().getJaw().requestMoveToMax();
    dragon.getHead().getRightNeck().requestMoveToPercentage(1);
    dragon.getHead().getLeftNeck().requestMoveToPercentage(1);
    soundPlayer.play();
    TimeUnit.SECONDS.sleep(5);

    dragon.getHead().getJaw().requestMoveToZero();
    dragon.getHead().getRightNeck().requestMoveToPercentage(0.5);
    dragon.getHead().getLeftNeck().requestMoveToPercentage(0.5);
    TimeUnit.SECONDS.sleep(5);
  }
}
