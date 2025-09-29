package com.craftmaster2190.halloween.dragoncontrol;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import jakarta.annotation.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class PinController {

  private static final boolean IS_RASPBERRY_PI = new RaspberryPiDetector().isRaspberryPi();
  private final AtomicReference<Context> pi4j = new AtomicReference<>();
  private final Map<Pin, DigitalOutput> pinMapping = new ConcurrentHashMap<>();

  @PostConstruct
  public void init() {
    if (!IS_RASPBERRY_PI) {
      return;
    }
    log.info("Running on Raspberry Pi. GPIO pins will be controlled.");
    pi4j.set(Pi4J.newAutoContext());
  }

  @PreDestroy
  public void shutdown() {
    Context pi4j = this.pi4j.get();
    if (pi4j != null) {
      pi4j.shutdown();
    }
  }

  public void openPin(Pin pin) {
    log.info("{} opened", pin);
    if (!IS_RASPBERRY_PI) {
      return;
    }
    getDigitalOutput(pin).high();
  }

  public void closePin(Pin pin) {
    log.info("{} closed", pin);
    if (!IS_RASPBERRY_PI) {
      return;
    }
    getDigitalOutput(pin).low();
  }

  private DigitalOutput getDigitalOutput(Pin pin) {
    return pinMapping.computeIfAbsent(pin,
        p -> pi4j
            .get()
            .digitalOutput()
            .create(pin.getPinNumber()));
  }
}
