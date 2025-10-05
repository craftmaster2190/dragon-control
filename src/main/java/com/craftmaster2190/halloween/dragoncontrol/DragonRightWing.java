package com.craftmaster2190.halloween.dragoncontrol;

import java.time.Duration;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class DragonRightWing {

  private final ActuatorState rightHand;

  public DragonRightWing(PinController pinController) {
    var rightHandRaw = new Chicago3WaySwitch(pinController, Pin.RIGHT_WING_POSITIVE, Pin.RIGHT_WING_NEGATIVE);

    rightHand = new ActuatorState("RightHand",
        Duration.ofSeconds(5), new Chicago3WaySwitch(pinController, Pin.RIGHT_WING_POSITIVE, Pin.RIGHT_WING_NEGATIVE));
  }

  public void extend() {

  }

  public void retract() {

  }
}
