package com.craftmaster2190.halloween.dragoncontrol.behavior;

import com.craftmaster2190.halloween.dragoncontrol.*;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class QuickRoarBehavior implements Behavior {
  private final Dragon dragon;
  private final SoundPlayer soundPlayer;

  public QuickRoarBehavior(Dragon dragon, @Value("classpath:quick-roar6-4s.wav") Resource soundfile) {
    this.dragon = dragon;
    this.soundPlayer = new SoundPlayer(soundfile);
  }

  @Override
  public void perform() throws InterruptedException {
    dragon.getLongNeck().setBoth(1);
    dragon.getHead().getRightNeck().requestMoveToPercentage(0.5);
    dragon.getHead().getLeftNeck().requestMoveToPercentage(0.5);
    TimeUnit.SECONDS.sleep(5);

    dragon.getHead().getJaw().requestMoveToMax();
    soundPlayer.play();
    TimeUnit.SECONDS.sleep(3);

    dragon.getHead().getJaw().requestMoveToZero();
    dragon.getHead().getRightNeck().requestMoveToPercentage(0);
    dragon.getHead().getLeftNeck().requestMoveToPercentage(0);
    TimeUnit.SECONDS.sleep(3);
  }
}
