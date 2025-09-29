package com.craftmaster2190.halloween.dragoncontrol;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class SoundPlayerService {

  @Value("classpath:growl1.wav")
  private Resource growl1;

  public void playRandomGrowl() {

  }

  public void playRoar() {

  }

  private void playSound(String soundFileName) {
    // TODO Play sound file
  }
}

