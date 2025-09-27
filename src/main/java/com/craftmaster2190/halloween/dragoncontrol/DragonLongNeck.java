package com.craftmaster2190.halloween.dragoncontrol;

import java.time.Duration;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class DragonLongNeck {

  private final ActuatorState lowerNeckLeft;
  private final ActuatorState lowerNeckRight;
  private final ActuatorState midNeckVertical;

  public DragonLongNeck(PinController pinController) {
    lowerNeckLeft = new ActuatorState("LowerNeckLeft",
        Duration.ofSeconds(4),
        pinController,
        Pin.LOWER_LEFT_NECK_NEGATIVE,
        Pin.LOWER_LEFT_NECK_POSITIVE);
    lowerNeckRight = new ActuatorState("LowerNeckRight",
        Duration.ofSeconds(4),
        pinController,
        Pin.LOWER_RIGHT_NECK_NEGATIVE,
        Pin.LOWER_RIGHT_NECK_POSITIVE);
    midNeckVertical = new ActuatorState("MidNeckVertical",
        Duration.ofSeconds(4),
        pinController,
        Pin.MID_NECK_VERTICAL_NEGATIVE,
        Pin.MID_NECK_VERTICAL_POSITIVE);
  }

  public void sleep() {

  }

  public void wakeUp() {

  }

  public void extendToRoar() {

  }
}


