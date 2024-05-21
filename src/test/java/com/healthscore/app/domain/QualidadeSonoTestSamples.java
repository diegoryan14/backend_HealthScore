package com.healthscore.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QualidadeSonoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static QualidadeSono getQualidadeSonoSample1() {
        return new QualidadeSono().id(1L).horasSono(1);
    }

    public static QualidadeSono getQualidadeSonoSample2() {
        return new QualidadeSono().id(2L).horasSono(2);
    }

    public static QualidadeSono getQualidadeSonoRandomSampleGenerator() {
        return new QualidadeSono().id(longCount.incrementAndGet()).horasSono(intCount.incrementAndGet());
    }
}
