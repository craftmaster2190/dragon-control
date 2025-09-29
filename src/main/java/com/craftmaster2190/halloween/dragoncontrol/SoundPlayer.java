package com.craftmaster2190.halloween.dragoncontrol;

import java.io.*;
import javax.sound.sampled.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
@RequiredArgsConstructor
public class SoundPlayer implements Closeable {
  private final Resource soundFile;
  private final LineListener lineListener = event -> {
    if (LineEvent.Type.START == event.getType()) {
      playbackCompleted = false;
    } else if (LineEvent.Type.STOP == event.getType()) {
      playbackCompleted = true;
    }
  };

  @Getter
  private volatile boolean playbackCompleted = true;
  private InputStream soundFileInputStream;
  private AudioInputStream audioStream;
  private Clip audioClip;

  public void play() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
    if (!playbackCompleted) {
      log.warn("Attempted to start playback while another playback is active. soundFile={}", soundFile);
      return;
    }
    soundFileInputStream = soundFile.getInputStream();
    audioStream = AudioSystem.getAudioInputStream(soundFileInputStream);
    AudioFormat audioFormat = audioStream.getFormat();
    DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
    audioClip = (Clip) AudioSystem.getLine(info);
    audioClip.addLineListener(lineListener);
    audioClip.open(audioStream);
    audioClip.start();
  }

  public void stop() {
    if (audioClip != null) {
      audioClip.stop();
      audioClip.removeLineListener(lineListener);
      audioClip.close();
    }
  }

  @Override
  public void close() throws IOException {
    // try-with-resources to auto-close all streams, gracefully handles nulls
    try (InputStream soundFileInputStream = this.soundFileInputStream;
         AudioInputStream audioStream = this.audioStream;
         Clip audioClip = this.audioClip) {
      stop();
    }
  }
}
