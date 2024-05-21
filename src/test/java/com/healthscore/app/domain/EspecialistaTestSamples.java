package com.healthscore.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EspecialistaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Especialista getEspecialistaSample1() {
        return new Especialista().id(1L).nome("nome1").cpf("cpf1").telefone(1).email("email1");
    }

    public static Especialista getEspecialistaSample2() {
        return new Especialista().id(2L).nome("nome2").cpf("cpf2").telefone(2).email("email2");
    }

    public static Especialista getEspecialistaRandomSampleGenerator() {
        return new Especialista()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .cpf(UUID.randomUUID().toString())
            .telefone(intCount.incrementAndGet())
            .email(UUID.randomUUID().toString());
    }
}
