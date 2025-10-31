package com.craftmaster2190.halloween.dragoncontrol;

import com.pi4j.Pi4J;
import com.pi4j.boardinfo.util.BoardInfoHelper;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import jakarta.annotation.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class PinController {

  private static final boolean IS_RASPBERRY_PI = BoardInfoHelper.runningOnRaspberryPi();
  private final AtomicReference<Context> pi4j = new AtomicReference<>();
  private final Map<NamedGpioPin, DigitalOutput> pinMapping = new ConcurrentHashMap<>();

  @PostConstruct
  public void init() {
    if (!IS_RASPBERRY_PI) {
      log.warn("Not running on Raspberry Pi. GPIO pins will NOT be controlled.");
      return;
    }
    log.info("Running on Raspberry Pi. GPIO pins will be controlled.");
    pi4j.set(Pi4J.newAutoContext());
    resetAllPins();
  }

  @PreDestroy
  public void shutdown() {
    Context pi4j = this.pi4j.get();
    if (pi4j != null) {
      resetAllPins();
      pi4j.shutdown();
    }
  }

  private void resetAllPins() {
    Arrays.stream(NamedGpioPin.values()).forEach(this::closePin);
  }

  public void openPin(NamedGpioPin pin) {
    log.debug("{} opened", pin);
    if (!IS_RASPBERRY_PI) {
      return;
    }
    getDigitalOutput(pin).high();
  }

  public void closePin(NamedGpioPin pin) {
    log.debug("{} closed", pin);
    if (!IS_RASPBERRY_PI) {
      return;
    }
    getDigitalOutput(pin).low();
  }

  private DigitalOutput getDigitalOutput(NamedGpioPin pin) {
    return pinMapping.computeIfAbsent(pin,
        p -> pi4j
            .get()
            .digitalOutput()
            .create(pin.getRelaySwitch().getPin().getBcmNumber()));
  }
}
