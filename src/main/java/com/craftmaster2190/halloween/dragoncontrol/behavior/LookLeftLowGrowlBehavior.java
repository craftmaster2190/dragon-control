package com.craftmaster2190.halloween.dragoncontrol.behavior;

import com.craftmaster2190.halloween.dragoncontrol.*;
import java.util.concurrent.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class LookLeftLowGrowlBehavior implements Behavior {
  private final Dragon dragon;
  private final SoundPlayer soundPlayer;

  public LookLeftLowGrowlBehavior(Dragon dragon, @Value("classpath:growl2-4s.wav") Resource soundfile) {
    this.dragon = dragon;
    this.soundPlayer = new SoundPlayer(soundfile);
  }

  @Override
  public void perform() throws InterruptedException {
    dragon.getLongNeck().setBoth(1.0);
    dragon.getHead().getRightNeck().requestMoveToPercentage(0.0);
    dragon.getHead().getLeftNeck().requestMoveToPercentage(1.0);
    dragon.getHead().getJaw().requestMoveToZero();
    TimeUnit.SECONDS.sleep(4);

    dragon.getHead().getJaw().requestMoveToPercentage(ThreadLocalRandom.current().nextDouble(0.5));
    soundPlayer.play();
    TimeUnit.SECONDS.sleep(2);
    dragon.getHead().getJaw().requestMoveToZero();
    TimeUnit.SECONDS.sleep(2);
  }
}
