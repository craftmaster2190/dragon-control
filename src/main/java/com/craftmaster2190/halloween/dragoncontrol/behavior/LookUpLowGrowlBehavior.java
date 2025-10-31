package com.craftmaster2190.halloween.dragoncontrol.behavior;

import com.craftmaster2190.halloween.dragoncontrol.*;
import java.util.concurrent.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class LookUpLowGrowlBehavior implements Behavior {
  private final Dragon dragon;
  private final SoundPlayer soundPlayer;

  public LookUpLowGrowlBehavior(Dragon dragon, @Value("classpath:growl5-5s.wav") Resource soundfile) {
    this.dragon = dragon;
    this.soundPlayer = new SoundPlayer(soundfile);
  }

  @Override
  public void perform() throws InterruptedException {
    dragon.getLongNeck().setBoth(0.5);
    dragon.getHead().getRightNeck().requestMoveToPercentage(1.0);
    dragon.getHead().getLeftNeck().requestMoveToPercentage(1.0);

    dragon.getHead().getJaw().requestMoveToPercentage(1);
    soundPlayer.play();
    TimeUnit.SECONDS.sleep(2);
    dragon.getHead().getJaw().requestMoveToZero();
    TimeUnit.SECONDS.sleep(3);
  }
}
