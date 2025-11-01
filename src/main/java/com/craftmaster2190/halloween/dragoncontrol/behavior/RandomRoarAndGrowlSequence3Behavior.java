package com.craftmaster2190.halloween.dragoncontrol.behavior;

import com.craftmaster2190.halloween.dragoncontrol.*;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

//@Component
public class RandomRoarAndGrowlSequence3Behavior implements Behavior {
  private final Dragon dragon;
  private final SoundPlayer soundPlayer;

  public RandomRoarAndGrowlSequence3Behavior(Dragon dragon, @Value("classpath:roar-4s-2s-silence-2s-roar-4s-3s.wav") Resource soundfile) {
    this.dragon = dragon;
    this.soundPlayer = new SoundPlayer(soundfile);
  }

  @Override
  public void perform() throws InterruptedException {
    dragon.getLongNeck().setBoth(1);
    dragon.getHead().getRightNeck().requestMoveToPercentage(1);
    dragon.getHead().getLeftNeck().requestMoveToPercentage(0);
    dragon.getHead().getJaw().requestMoveToZero();

    soundPlayer.play();

    dragon.getHead().getJaw().requestMoveToMax();
    TimeUnit.SECONDS.sleep(2);
    dragon.getHead().getJaw().requestMoveToZero();
    TimeUnit.SECONDS.sleep(2);

    dragon.getHead().getJaw().requestMoveToMax();
    TimeUnit.SECONDS.sleep(1);
    dragon.getHead().getJaw().requestMoveToZero();
    TimeUnit.SECONDS.sleep(1);

    dragon.getHead().getRightNeck().requestMoveToPercentage(0);
    dragon.getHead().getLeftNeck().requestMoveToPercentage(1);


    TimeUnit.SECONDS.sleep(2);

    dragon.getHead().getJaw().requestMoveToMax();
    TimeUnit.SECONDS.sleep(2);
    dragon.getHead().getJaw().requestMoveToZero();
    TimeUnit.SECONDS.sleep(2);

    dragon.getHead().getJaw().requestMoveToMax();
    TimeUnit.SECONDS.sleep(2);
    dragon.getHead().getJaw().requestMoveToZero();
    TimeUnit.SECONDS.sleep(2);
  }
}
