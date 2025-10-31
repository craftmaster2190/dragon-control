package com.craftmaster2190.halloween.dragoncontrol;

public class Preconditions {
  public static void checkState(boolean condition, String message) {
    if (!condition) {
      throw new IllegalStateException(message);
    }
  }
}
