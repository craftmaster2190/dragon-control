package com.craftmaster2190.halloween.dragoncontrol;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ListUtils {
  public static <T> ArrayList<T> copyOfAndExclude(List<T> list, T toRemove) {
    ArrayList<T> newList = new ArrayList<>(list);
    newList.remove(toRemove);
    return newList;
  }

  public static <T> T random(List<T> list) {
    return list.get(ThreadLocalRandom.current().nextInt(list.size()));
  }
}
