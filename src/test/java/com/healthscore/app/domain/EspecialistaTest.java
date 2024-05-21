package com.healthscore.app.domain;

import static com.healthscore.app.domain.ConsultaEspecialistaTestSamples.*;
import static com.healthscore.app.domain.EspecialistaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.healthscore.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EspecialistaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Especialista.class);
        Especialista especialista1 = getEspecialistaSample1();
        Especialista especialista2 = new Especialista();
        assertThat(especialista1).isNotEqualTo(especialista2);

        especialista2.setId(especialista1.getId());
        assertThat(especialista1).isEqualTo(especialista2);

        especialista2 = getEspecialistaSample2();
        assertThat(especialista1).isNotEqualTo(especialista2);
    }

    @Test
    void consultasTest() throws Exception {
        Especialista especialista = getEspecialistaRandomSampleGenerator();
        ConsultaEspecialista consultaEspecialistaBack = getConsultaEspecialistaRandomSampleGenerator();

        especialista.addConsultas(consultaEspecialistaBack);
        assertThat(especialista.getConsultas()).containsOnly(consultaEspecialistaBack);
        assertThat(consultaEspecialistaBack.getEspecialista()).isEqualTo(especialista);

        especialista.removeConsultas(consultaEspecialistaBack);
        assertThat(especialista.getConsultas()).doesNotContain(consultaEspecialistaBack);
        assertThat(consultaEspecialistaBack.getEspecialista()).isNull();

        especialista.consultas(new HashSet<>(Set.of(consultaEspecialistaBack)));
        assertThat(especialista.getConsultas()).containsOnly(consultaEspecialistaBack);
        assertThat(consultaEspecialistaBack.getEspecialista()).isEqualTo(especialista);

        especialista.setConsultas(new HashSet<>());
        assertThat(especialista.getConsultas()).doesNotContain(consultaEspecialistaBack);
        assertThat(consultaEspecialistaBack.getEspecialista()).isNull();
    }
}
