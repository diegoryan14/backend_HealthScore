package com.healthscore.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DietaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Dieta getDietaSample1() {
        return new Dieta().id(1L).descricaoRefeicao("descricaoRefeicao1").caloriasConsumidas(1);
    }

    public static Dieta getDietaSample2() {
        return new Dieta().id(2L).descricaoRefeicao("descricaoRefeicao2").caloriasConsumidas(2);
    }

    public static Dieta getDietaRandomSampleGenerator() {
        return new Dieta()
            .id(longCount.incrementAndGet())
            .descricaoRefeicao(UUID.randomUUID().toString())
            .caloriasConsumidas(intCount.incrementAndGet());
    }
}
