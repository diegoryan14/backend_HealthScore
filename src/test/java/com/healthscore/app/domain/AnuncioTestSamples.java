package com.healthscore.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AnuncioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Anuncio getAnuncioSample1() {
        return new Anuncio().id(1L).titulo("titulo1").descricao("descricao1");
    }

    public static Anuncio getAnuncioSample2() {
        return new Anuncio().id(2L).titulo("titulo2").descricao("descricao2");
    }

    public static Anuncio getAnuncioRandomSampleGenerator() {
        return new Anuncio().id(longCount.incrementAndGet()).titulo(UUID.randomUUID().toString()).descricao(UUID.randomUUID().toString());
    }
}
