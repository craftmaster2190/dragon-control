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
    leftNeck = new ActuatorState("UpperLeftNeck", Duration.ofSeconds(4), pinController, Pin.UPPER_LEFT_NECK_NEGATIVE, Pin.UPPER_LEFT_NECK_POSITIVE);
    rightNeck = new ActuatorState("UpperRightNeck", Duration.ofSeconds(4), pinController, Pin.UPPER_RIGHT_NECK_NEGATIVE, Pin.UPPER_RIGHT_NECK_POSITIVE);
    jaw = new ActuatorState("Jaw", Duration.ofSeconds(2), pinController, Pin.JAW_NEGATIVE, Pin.JAW_POSITIVE);
    eyes = new ActuatorState("UpperLip", Duration.ofSeconds(1), pinController, Pin.UPPER_LIP_NEGATIVE, Pin.UPPER_LIP_POSITIVE);
    upperLip = new ActuatorState("Eyes", Duration.ofSeconds(1), pinController, Pin.EYES_NEGATIVE, Pin.EYES_POSITIVE);
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
