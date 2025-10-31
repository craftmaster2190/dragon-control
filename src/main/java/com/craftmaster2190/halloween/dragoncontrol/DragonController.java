package com.craftmaster2190.halloween.dragoncontrol;

import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("dragon")
@RequiredArgsConstructor
public class DragonController {
  private final Dragon dragon;
  private final PinController pinController;
  private final BehaviorManager behaviorManager;

//  @PreDestroy
  @PostMapping("reset")
  public void reset() throws InterruptedException {
    dragon.reset();
  }

  @PostMapping("stop")
  public void stop() {
    dragon.stop();
  }

  @GetMapping
  public Map<String, ?> getDragon() {
    return Map.of("dragon", dragon,
        "behavior", behaviorManager.getCurrentBehavior().getClass().getSimpleName(),
        "pins", pinController.getCurrentPins()
        );
  }

  @PostMapping("admin-move")
  public void adminMove(@RequestBody AdminMoveRequest adminMoveRequest) {
    if (Objects.equals(adminMoveRequest.part(), "all")) {
      dragon.getHead().getJaw().requestMoveToPercentage(adminMoveRequest.percent());
      dragon.getHead().getLeftNeck().requestMoveToPercentage(adminMoveRequest.percent());
      dragon.getHead().getRightNeck().requestMoveToPercentage(adminMoveRequest.percent());
      dragon.getLongNeck().getLowerNeckLeft().requestMoveToPercentage(adminMoveRequest.percent());
      dragon.getLongNeck().getLowerNeckRight().requestMoveToPercentage(adminMoveRequest.percent());
      return;
    }

    if (Objects.equals(adminMoveRequest.part(), "head.neck.both")) {
      dragon.getHead().getLeftNeck().requestMoveToPercentage(adminMoveRequest.percent());
      dragon.getHead().getRightNeck().requestMoveToPercentage(adminMoveRequest.percent());
      return;
    }

    if (Objects.equals(adminMoveRequest.part(), "neck.lower.both")) {
      dragon.getLongNeck().getLowerNeckLeft().requestMoveToPercentage(adminMoveRequest.percent());
      dragon.getLongNeck().getLowerNeckRight().requestMoveToPercentage(adminMoveRequest.percent());
      return;
    }

    var part = switch (adminMoveRequest.part()) {
      case "head.jaw" -> dragon.getHead().getJaw();
      case "head.leftneck" -> dragon.getHead().getLeftNeck();
      case "head.rightneck" -> dragon.getHead().getRightNeck();
      case "neck.lowerleft" -> dragon.getLongNeck().getLowerNeckLeft();
      case "neck.lowerright" -> dragon.getLongNeck().getLowerNeckRight();
      default -> throw new IllegalArgumentException("Unknown part: " + adminMoveRequest.part());
    };
    part.requestMoveToPercentage(adminMoveRequest.percent());
  }
}

