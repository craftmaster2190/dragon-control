package com.craftmaster2190.halloween.dragoncontrol;

import java.time.Duration;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Data
@Component
public class DragonHead {
  private final ActuatorState leftNeck;
  private final ActuatorState rightNeck;
  private final ActuatorState jaw;
  private final ActuatorState eyes;
  private final ActuatorState upperLip;

  public DragonHead(PinController pinController) {
    leftNeck = new ActuatorState("UpperLeftNeck",
        Duration.ofSeconds(4), new Chicago3WaySwitch(pinController,
        Pin.UPPER_LEFT_NECK_POSITIVE,
        Pin.UPPER_LEFT_NECK_NEGATIVE));
    rightNeck = new ActuatorState("UpperRightNeck",
        Duration.ofSeconds(4), new Chicago3WaySwitch(pinController,
        Pin.UPPER_RIGHT_NECK_POSITIVE,
        Pin.UPPER_RIGHT_NECK_NEGATIVE));
    jaw = new ActuatorState("Jaw",
        Duration.ofSeconds(2), new Chicago3WaySwitch(pinController, Pin.JAW_POSITIVE, Pin.JAW_NEGATIVE));
    eyes = new ActuatorState("Eyes",
        Duration.ofSeconds(1), new Chicago3WaySwitch(pinController, Pin.UPPER_LIP_POSITIVE, Pin.UPPER_LIP_NEGATIVE));
    upperLip = new ActuatorState("UpperLip",
        Duration.ofSeconds(1), new Chicago3WaySwitch(pinController, Pin.EYES_POSITIVE, Pin.EYES_NEGATIVE));
  }

  public void sleep() {

  }

  public void wakeUp() {

  }

  public void blinkEyes() {

  }

  public void growl() {

  }

  public void openJaw() {

  }
}
