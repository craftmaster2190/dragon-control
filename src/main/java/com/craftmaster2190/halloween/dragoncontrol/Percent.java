package com.craftmaster2190.halloween.dragoncontrol;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
@JsonSerialize(using = PercentSerializer.class)
public class Percent {
  private final double percent;

  public Percent(double percent) {
    if (percent < 0 || percent > 1) {
      throw new IllegalArgumentException("Percent must be between 0 and 1");
    }
    this.percent = percent;
  }

  public Percent(double numerator, double denominator) {
    this(numerator / denominator);
  }

  @Override
  public String toString() {
    return String.format("%.2f%%", percent * 100);
  }

}
