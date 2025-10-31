package com.craftmaster2190.halloween.dragoncontrol.behavior;

import com.craftmaster2190.halloween.dragoncontrol.*;
import java.util.concurrent.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class RandomRoarAndGrowlSequence3Behavior implements Behavior {
  private final Dragon dragon;
  private final SoundPlayer soundPlayer;

  public RandomRoarAndGrowlSequence3Behavior(Dragon dragon, @Value("classpath:roar-4s-2s-silence-2s-roar-4s-3s.wav") Resource soundfile) {
    this.dragon = dragon;
    this.soundPlayer = new SoundPlayer(soundfile);
  }

  @Override
  public void perform() throws InterruptedException {
    dragon.getLongNeck().setBoth(ThreadLocalRandom.current().nextDouble());
    dragon.getHead().getRightNeck().requestMoveToPercentage(ThreadLocalRandom.current().nextDouble());
    dragon.getHead().getLeftNeck().requestMoveToPercentage(ThreadLocalRandom.current().nextDouble());
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

    dragon.getLongNeck().setBoth(ThreadLocalRandom.current().nextDouble());
    dragon.getHead().getRightNeck().requestMoveToPercentage(ThreadLocalRandom.current().nextDouble());
    dragon.getHead().getLeftNeck().requestMoveToPercentage(ThreadLocalRandom.current().nextDouble());


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
