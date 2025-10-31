package com.craftmaster2190.halloween.dragoncontrol;

import jakarta.annotation.Nonnull;
import java.io.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

@Slf4j
public class SoundPlayer {
  @Nonnull
  private final Resource soundFile;

  public SoundPlayer(Resource soundFile) {
    this.soundFile = soundFile;
    Preconditions.checkState(soundFile.exists(), "soundFile is missing: " + soundFile);
  }

  public void play() {
    if (!PiUtils.IS_RASPBERRY_PI) {
      return;
    }

    File file = new File(soundFile.getFilename());
    if (!file.exists()) {
      try (FileOutputStream fileOutputStream = new FileOutputStream(file);
           InputStream resourceInputStream = soundFile.getInputStream();) {
        resourceInputStream.transferTo(fileOutputStream);
        log.info("Created sound file={}", file);
      }
      catch (Exception e) {
        log.error("Unable to create file={}", file, e);
        return;
      }
    }

    Thread
        .ofVirtual()
        .name("SoundPlayer-aplay-" + file.getName())
        .start(() -> {
          try {
            ProcessBuilder soundPlayerProcess = new ProcessBuilder("aplay", "--device=pulse", file.getCanonicalPath());
            soundPlayerProcess.environment().put("XDG_RUNTIME_DIR", "/run/user/1000");
            Process process = soundPlayerProcess.start();

            // It's crucial to read the process's output streams (stdout and stderr)
            // to prevent the child process from blocking or hanging.

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
              log.info("aplay {} stdout: {}", file, line);
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
              log.error("aplay {} stderr: {}", file, line);
            }

            int exitCode = process.waitFor();
            log.info("aplay {} exit-code: {}", file, exitCode);
          }
          catch (Exception e) {
            log.error("Error playing file={}", file, e);
          }

        });
  }
}
