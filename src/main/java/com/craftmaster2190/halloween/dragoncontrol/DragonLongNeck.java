package com.craftmaster2190.halloween.dragoncontrol;

import java.time.Duration;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class DragonLongNeck {

  private final ActuatorState lowerNeckLeft;
  private final ActuatorState lowerNeckRight;

  public DragonLongNeck(PinController pinController) {
    lowerNeckLeft = new ActuatorState("LowerNeckLeft",
        Duration.ofSeconds(20), ActuatorState.CLOSES_FASTER, new Chicago3WaySwitch(pinController,
        NamedGpioPin.LOWER_LEFT_NECK_POSITIVE,
        NamedGpioPin.LOWER_LEFT_NECK_NEGATIVE));
    lowerNeckRight = new ActuatorState("LowerNeckRight",
        Duration.ofSeconds(20), ActuatorState.CLOSES_FASTER, new Chicago3WaySwitch(pinController,
        NamedGpioPin.LOWER_RIGHT_NECK_POSITIVE,
        NamedGpioPin.LOWER_RIGHT_NECK_NEGATIVE));
  }

  public void sleep() {
    lowerNeckLeft.requestMoveToZero();
    lowerNeckRight.requestMoveToZero();
  }

  public void stop() {
    lowerNeckLeft.stop();
    lowerNeckRight.stop();
  }
}


