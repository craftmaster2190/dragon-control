package com.craftmaster2190.halloween.dragoncontrol.behavior;

import com.craftmaster2190.halloween.dragoncontrol.Dragon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResetBehavior implements Behavior {
  private final Dragon dragon;

  @Override
  public void perform() throws InterruptedException {
    dragon.reset();
  }
}
