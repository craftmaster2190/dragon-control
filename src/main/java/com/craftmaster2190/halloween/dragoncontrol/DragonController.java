package com.craftmaster2190.halloween.dragoncontrol;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("dragon")
@RequiredArgsConstructor
public class DragonController {
  private final Dragon dragon;

//  @PreDestroy
  @PostMapping("reset")
  public void reset() throws InterruptedException {
    dragon.reset();
  }

  @PostMapping("stop")
  public void stop() {
    dragon.stop();
  }

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

  @PostMapping("admin-move")
  public void adminMove(@RequestBody AdminMoveRequest adminMoveRequest) {
    var part = switch (adminMoveRequest.part()) {
      case "head.jaw" -> dragon.getHead().getJaw();
      case "head.leftneck" -> dragon.getHead().getLeftNeck();
      case "head.rightneck" -> dragon.getHead().getRightNeck();
      case "neck.lowerleft" -> dragon.getLongNeck().getLowerNeckLeft();
      case "neck.lowerright" -> dragon.getLongNeck().getLowerNeckRight();
      case "rightwing.hand" -> dragon.getRightWing().getRightHand();
      default -> throw new IllegalArgumentException("Unknown part: " + adminMoveRequest.part());
    };
    part.requestMoveToPercentage(adminMoveRequest.percent());
  }
}

