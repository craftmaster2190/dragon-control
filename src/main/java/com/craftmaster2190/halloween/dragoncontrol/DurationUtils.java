package com.craftmaster2190.halloween.dragoncontrol;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DurationUtils {
  static Duration clampDuration(Duration target, Duration min, Duration max) {
    if (target.compareTo(min) < 0) {
      target = min;
    }
    if (target.compareTo(max) > 0) {
      target = max;
    }
    return target;
  }

  static String formatDuration(Duration duration) {
    return duration.truncatedTo(ChronoUnit.MILLIS).toString().substring("PT".length());
  }
}
