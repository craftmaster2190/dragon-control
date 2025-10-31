package com.craftmaster2190.halloween.dragoncontrol;

import com.pi4j.boardinfo.util.BoardInfoHelper;

public class PiUtils {

  public static final boolean IS_RASPBERRY_PI = BoardInfoHelper.runningOnRaspberryPi();
}
