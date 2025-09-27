package com.craftmaster2190.halloween.dragoncontrol;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class SoundPlayer {

  @PostConstruct
  public void startMusic() {

  }

  public void stopMusic() {

  }

  public void playGrowl() {
    playSound("growl.wav");
  }

  public void playRoar() {
    playSound("roar.mp3");
  }

  private void playSound(String soundFileName) {
    // TODO Play sound file
  }
}
