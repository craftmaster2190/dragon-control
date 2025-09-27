package com.craftmaster2190.halloween.dragoncontrol;

import java.time.Duration;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class DragonRightWing {

  private final ActuatorState rightWing;

  public DragonRightWing(PinController pinController) {
    rightWing = new ActuatorState("RightWing",
        Duration.ofSeconds(5),
        pinController,
        Pin.RIGHT_WING_NEGATIVE,
        Pin.RIGHT_WING_POSITIVE);
  }

  public void extend() {

  }

  public void retract() {

  }
}
