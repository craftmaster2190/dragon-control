package com.craftmaster2190.halloween.dragoncontrol;

import lombok.Getter;

@Getter
public enum NamedGpioPin {
  UPPER_LEFT_NECK_POSITIVE(RelaySwitch.SIDE_B_S1),
  UPPER_LEFT_NECK_NEGATIVE(RelaySwitch.SIDE_B_S2),
  UPPER_RIGHT_NECK_POSITIVE(RelaySwitch.SIDE_B_S3),
  UPPER_RIGHT_NECK_NEGATIVE(RelaySwitch.SIDE_B_S4),
  JAW_POSITIVE(RelaySwitch.SIDE_A_S6), // Black
  JAW_NEGATIVE(RelaySwitch.SIDE_A_S5),
  RIGHT_WING_POSITIVE(RelaySwitch.SIDE_B_S5),
  RIGHT_WING_NEGATIVE(RelaySwitch.SIDE_B_S6),
  LOWER_LEFT_NECK_POSITIVE(RelaySwitch.SIDE_A_S2),
  LOWER_LEFT_NECK_NEGATIVE(RelaySwitch.SIDE_A_S1),
  LOWER_RIGHT_NECK_POSITIVE(RelaySwitch.SIDE_A_S4),
  LOWER_RIGHT_NECK_NEGATIVE(RelaySwitch.SIDE_A_S3);

  private final RelaySwitch relaySwitch;

  NamedGpioPin(RelaySwitch relaySwitch) {
    this.relaySwitch = relaySwitch;
  }

  static {
    EnumUtils.extractedValuesAreUnique(NamedGpioPin.values(), NamedGpioPin::getRelaySwitch);
  }
}
