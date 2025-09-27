package com.craftmaster2190.halloween.dragoncontrol;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("dragon")
@RequiredArgsConstructor
public class DragonController {
  private final Dragon dragon;

  @PostMapping("sleep")
  public void sleep() {
    dragon.sleep();
  }

  @PostMapping("wakeUp")
  public void wakeUp() {
    dragon.wakeUp();
  }

  @PostMapping("roar")
  public void roar() {
    dragon.roar();
  }

  @GetMapping
  public Dragon getDragon() {
    return dragon;
  }
}
