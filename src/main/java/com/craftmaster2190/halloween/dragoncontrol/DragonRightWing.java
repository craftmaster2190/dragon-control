package com.craftmaster2190.halloween.dragoncontrol;

import java.time.Duration;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class DragonRightWing {

  private final ActuatorState rightHand;

  public DragonRightWing(PinController pinController) {
    rightHand = new ActuatorState("RightHand",
        Duration.ofSeconds(5), new Chicago3WaySwitch(pinController, NamedGpioPin.RIGHT_WING_POSITIVE, NamedGpioPin.RIGHT_WING_NEGATIVE));
  }

  public void extend() {

  }

  public void retract() {

  }

  public void stop() {
    rightHand.stop();
  }
}
