package com.healthscore.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConsultaEspecialistaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ConsultaEspecialista getConsultaEspecialistaSample1() {
        return new ConsultaEspecialista().id(1L).linkConsulta("linkConsulta1");
    }

    public static ConsultaEspecialista getConsultaEspecialistaSample2() {
        return new ConsultaEspecialista().id(2L).linkConsulta("linkConsulta2");
    }

    public static ConsultaEspecialista getConsultaEspecialistaRandomSampleGenerator() {
        return new ConsultaEspecialista().id(longCount.incrementAndGet()).linkConsulta(UUID.randomUUID().toString());
    }
}
