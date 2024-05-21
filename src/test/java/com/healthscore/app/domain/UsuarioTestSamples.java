package com.healthscore.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UsuarioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Usuario getUsuarioSample1() {
        return new Usuario().id(1L).telefone(1).email("email1").metaConsumoAgua(1).pontosUser(1);
    }

    public static Usuario getUsuarioSample2() {
        return new Usuario().id(2L).telefone(2).email("email2").metaConsumoAgua(2).pontosUser(2);
    }

    public static Usuario getUsuarioRandomSampleGenerator() {
        return new Usuario()
            .id(longCount.incrementAndGet())
            .telefone(intCount.incrementAndGet())
            .email(UUID.randomUUID().toString())
            .metaConsumoAgua(intCount.incrementAndGet())
            .pontosUser(intCount.incrementAndGet());
    }
}
