package com.ro.orders.utils;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.util.Random;

public class ObjectGenerator {
    private static final EasyRandom er;

    static {
        Random random = new Random();
        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(Integer.class, () -> random.nextInt() & Integer.MAX_VALUE)
                .randomize(Short.class, () -> (short) random.nextInt(1 << 15));

        er = new EasyRandom(parameters);
    }

    public static <T> T getRandomObject(Class<T> clazz) {
        return er.nextObject(clazz);
    }
}
