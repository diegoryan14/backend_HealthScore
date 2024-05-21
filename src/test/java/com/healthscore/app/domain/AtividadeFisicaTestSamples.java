package com.healthscore.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AtividadeFisicaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AtividadeFisica getAtividadeFisicaSample1() {
        return new AtividadeFisica().id(1L).duracao(1).passosCalorias(1);
    }

    public static AtividadeFisica getAtividadeFisicaSample2() {
        return new AtividadeFisica().id(2L).duracao(2).passosCalorias(2);
    }

    public static AtividadeFisica getAtividadeFisicaRandomSampleGenerator() {
        return new AtividadeFisica()
            .id(longCount.incrementAndGet())
            .duracao(intCount.incrementAndGet())
            .passosCalorias(intCount.incrementAndGet());
    }
}
