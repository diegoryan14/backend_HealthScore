package com.healthscore.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PontuacaoUsuarioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PontuacaoUsuario getPontuacaoUsuarioSample1() {
        return new PontuacaoUsuario().id(1L).valorAlteracao(1).motivo("motivo1");
    }

    public static PontuacaoUsuario getPontuacaoUsuarioSample2() {
        return new PontuacaoUsuario().id(2L).valorAlteracao(2).motivo("motivo2");
    }

    public static PontuacaoUsuario getPontuacaoUsuarioRandomSampleGenerator() {
        return new PontuacaoUsuario()
            .id(longCount.incrementAndGet())
            .valorAlteracao(intCount.incrementAndGet())
            .motivo(UUID.randomUUID().toString());
    }
}
