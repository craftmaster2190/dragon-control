package com.craftmaster2190.halloween.dragoncontrol;

import java.io.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RaspberryPiDetector {

  @Getter(lazy = true)
  private final boolean isRaspberryPi = detectRaspberryPi();

  private boolean detectRaspberryPi() {
    try {
      var file = new File("/etc/os-release");
      var osRelease = readFirstLine(file);
      return osRelease.contains("Raspbian");
    } catch (Exception e) {
      log.warn("Could not access /etc/os-release to determine if running on Raspberry Pi. Assuming not.", e);
      return false;
    }
  }

  private static String readFirstLine(File file) throws IOException {
    try (var reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
      return reader.readLine();
    }
  }
}
