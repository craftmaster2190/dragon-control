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

  public DragonHead(PinController pinController) {
    leftNeck = new ActuatorState("UpperLeftNeck",
        Duration.ofSeconds(20), new Chicago3WaySwitch(pinController,
        NamedGpioPin.UPPER_LEFT_NECK_POSITIVE,
        NamedGpioPin.UPPER_LEFT_NECK_NEGATIVE));
    rightNeck = new ActuatorState("UpperRightNeck",
        Duration.ofSeconds(20), new Chicago3WaySwitch(pinController,
        NamedGpioPin.UPPER_RIGHT_NECK_POSITIVE,
        NamedGpioPin.UPPER_RIGHT_NECK_NEGATIVE));
    jaw = new ActuatorState("Jaw",
        Duration.ofSeconds(6), new Chicago3WaySwitch(pinController, NamedGpioPin.JAW_POSITIVE, NamedGpioPin.JAW_NEGATIVE));
  }

  public void sleep() {
    leftNeck.requestMoveToZero();
    rightNeck.requestMoveToZero();
    jaw.requestMoveToZero();
  }

  public void lookDown() {
    leftNeck.requestMoveToZero();
    rightNeck.requestMoveToZero();
  }

  public void lookUp() {
    leftNeck.requestMoveToMax();
    rightNeck.requestMoveToMax();
  }

  public void lookLeft() {
    leftNeck.requestMoveToZero();
    rightNeck.requestMoveToPercentage(0.5);
  }

  public void lookRight() {
    leftNeck.requestMoveToPercentage(0.5);
    rightNeck.requestMoveToZero();
  }

  public void closeJaw() {
    jaw.requestMoveToZero();
  }

  public void openJaw() {
    jaw.requestMoveToMax();
  }

  public void stop() {
    leftNeck.stop();
    rightNeck.stop();
    jaw.stop();
  }

}
