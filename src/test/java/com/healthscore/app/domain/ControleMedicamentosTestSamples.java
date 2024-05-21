package com.healthscore.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ControleMedicamentosTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ControleMedicamentos getControleMedicamentosSample1() {
        return new ControleMedicamentos().id(1L).nomeMedicamento("nomeMedicamento1").dosagem("dosagem1");
    }

    public static ControleMedicamentos getControleMedicamentosSample2() {
        return new ControleMedicamentos().id(2L).nomeMedicamento("nomeMedicamento2").dosagem("dosagem2");
    }

    public static ControleMedicamentos getControleMedicamentosRandomSampleGenerator() {
        return new ControleMedicamentos()
            .id(longCount.incrementAndGet())
            .nomeMedicamento(UUID.randomUUID().toString())
            .dosagem(UUID.randomUUID().toString());
    }
}
