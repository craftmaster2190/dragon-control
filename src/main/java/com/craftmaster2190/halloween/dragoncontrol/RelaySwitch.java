package com.craftmaster2190.halloween.dragoncontrol;

import com.pi4j.boardinfo.definition.HeaderPins;
import com.pi4j.boardinfo.model.HeaderPin;
import lombok.Getter;

@Getter
public enum RelaySwitch {
  // power side B 2
  // power side A 4
  // ground side B 6
  SIDE_B_S8(8),
  SIDE_B_S7(10),
  SIDE_B_S6(12),
  // ground side A 14
  SIDE_A_S1(16),
  SIDE_B_S5(18),
  // 20 unused

  SIDE_B_S4(22),
  SIDE_B_S3(24),
  SIDE_B_S2(26),
  // 28 unused
  // 30 unused
  SIDE_B_S1(32),
  // 34 unused
  SIDE_A_S2(36),
  SIDE_A_S3(38),
  SIDE_A_S4(40),
  
  SIDE_A_S5(31),
  SIDE_A_S6(33),
  SIDE_A_S7(35),
  SIDE_A_S8(37);

  private final HeaderPin pin;

  RelaySwitch(int logicalPinNumber) {
    pin = HeaderPins.HEADER_40.getPins()
        .stream().filter(headerPin -> headerPin.getPinNumber() == logicalPinNumber)
        .findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid logic number " + logicalPinNumber));
  }
  
  static {
    EnumUtils.extractedValuesAreUnique(RelaySwitch.values(), relaySwitch -> relaySwitch.getPin().getPinNumber());
  }
}
