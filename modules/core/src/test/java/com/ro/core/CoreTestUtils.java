package com.ro.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.util.Random;

public class CoreTestUtils {
  private static final EasyRandom easyRandom;
  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    Random random = new Random();
    EasyRandomParameters parameters = new EasyRandomParameters()
        .randomize(Integer.class, () -> random.nextInt() & Integer.MAX_VALUE)
        .randomize(Short.class, () -> (short) random.nextInt(1 << 15));

    easyRandom = new EasyRandom(parameters);
  }

  public static String asJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static <T> T getRandomObject(Class<T> clazz) {
    return easyRandom.nextObject(clazz);
  }
  public static String getRandomDigitsString(int length) {
    StringBuilder builder = new StringBuilder(10);
    for (int i = 0; i < length; i++) {
      builder.append((char) ('0' + (easyRandom.nextInt(9) + 1)));
    }
    return builder.toString();
  }
}
