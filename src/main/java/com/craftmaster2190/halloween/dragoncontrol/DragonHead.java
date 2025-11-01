package com.craftmaster2190.halloween.dragoncontrol;

import java.time.Duration;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.craftmaster2190.halloween.dragoncontrol.ActuatorState.*;
import static com.craftmaster2190.halloween.dragoncontrol.NamedGpioPin.*;

@Slf4j
@Data
@Component
public class DragonHead {
  private final ActuatorState leftNeck;
  private final ActuatorState rightNeck;
  private final ActuatorState jaw;

  public DragonHead(PinController pinController) {
    leftNeck = new ActuatorState("UpperLeftNeck",
        Duration.ofSeconds(10), CLOSES_FASTER,
        new Chicago3WaySwitch(pinController, UPPER_LEFT_NECK_POSITIVE, UPPER_LEFT_NECK_NEGATIVE));
    rightNeck = new ActuatorState("UpperRightNeck",
        Duration.ofSeconds(10), CLOSES_FASTER,
        new Chicago3WaySwitch(pinController, UPPER_RIGHT_NECK_POSITIVE, UPPER_RIGHT_NECK_NEGATIVE));
    jaw = new ActuatorState("Jaw",
        Duration.ofSeconds(6), OPENS_FASTER,
        new Chicago3WaySwitch(pinController, JAW_POSITIVE, JAW_NEGATIVE));
  }

  public void sleep() {
    leftNeck.requestMoveToZero();
    rightNeck.requestMoveToZero();
    jaw.requestMoveToZero();
  }

  public void stop() {
    leftNeck.stop();
    rightNeck.stop();
    jaw.stop();
  }

}
