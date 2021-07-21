package com.hla.gamp;

import java.util.Random;

public class RandomUtils {
    private static Random random = new Random();

    private RandomUtils() {
    }

    public static int rand(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
