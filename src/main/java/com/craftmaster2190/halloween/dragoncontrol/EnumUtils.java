package com.craftmaster2190.halloween.dragoncontrol;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnumUtils {
  public static <V> void extractedValuesAreUnique(V[] enumValues, Function<V, ?> valueExtractor) {
    var enumKeysWithDuplicateValues = Arrays
        .stream(enumValues).collect(Collectors.groupingBy(valueExtractor))
            .entrySet().stream().filter(entry -> entry.getValue().size() > 1)
            .map(Map.Entry::getKey).toList();
    if (!enumKeysWithDuplicateValues.isEmpty()) {
      throw new IllegalStateException("Enum values are not unique: " + enumKeysWithDuplicateValues);
    }
  }
}
