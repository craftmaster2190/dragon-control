package com.craftmaster2190.halloween.dragoncontrol;

import lombok.*;

@Data
public class Chicago3WaySwitch {
  enum State {UP, OFF, DOWN}

  private State currentState = State.OFF;
  private final Runnable onUp;
  private final Runnable onDown;
  private final Runnable onOff;

  public void setState(State state) {
    this.currentState = state;

    switch (state) {
      case UP -> onUp.run();
      case DOWN -> onDown.run();
      case OFF -> onOff.run();
    }
  }

  public Chicago3WaySwitch(PinController pinController, Pin upPin, Pin downPin) {
    this.onUp = () -> {
      pinController.closePin(downPin);
      pinController.openPin(upPin);
    };
    this.onDown = () -> {
      pinController.closePin(upPin);
      pinController.openPin(downPin);
    };
    this.onOff = () -> {
      pinController.closePin(upPin);
      pinController.closePin(downPin);
    };
  }


}
