package com.healthscore.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MetasSaudeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MetasSaude getMetasSaudeSample1() {
        return new MetasSaude().id(1L).valorMeta(1);
    }

    public static MetasSaude getMetasSaudeSample2() {
        return new MetasSaude().id(2L).valorMeta(2);
    }

    public static MetasSaude getMetasSaudeRandomSampleGenerator() {
        return new MetasSaude().id(longCount.incrementAndGet()).valorMeta(intCount.incrementAndGet());
    }
}
