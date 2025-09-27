package com.craftmaster2190.halloween.dragoncontrol;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PinController {

  public void openPin(Pin pin) {
    log.info("{} opened", pin);
    // TODO: Implement actual pin opening logic
  }

  public void closePin(Pin pin) {
    log.info("{} closed", pin);
  }
}
