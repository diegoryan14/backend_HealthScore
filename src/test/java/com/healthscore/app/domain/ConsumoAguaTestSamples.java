package com.healthscore.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ConsumoAguaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ConsumoAgua getConsumoAguaSample1() {
        return new ConsumoAgua().id(1L).quantidadeMl(1);
    }

    public static ConsumoAgua getConsumoAguaSample2() {
        return new ConsumoAgua().id(2L).quantidadeMl(2);
    }

    public static ConsumoAgua getConsumoAguaRandomSampleGenerator() {
        return new ConsumoAgua().id(longCount.incrementAndGet()).quantidadeMl(intCount.incrementAndGet());
    }
}
