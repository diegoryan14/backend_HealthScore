package com.healthscore.app.domain;

import static com.healthscore.app.domain.ConsultaEspecialistaTestSamples.*;
import static com.healthscore.app.domain.EspecialistaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.healthscore.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsultaEspecialistaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsultaEspecialista.class);
        ConsultaEspecialista consultaEspecialista1 = getConsultaEspecialistaSample1();
        ConsultaEspecialista consultaEspecialista2 = new ConsultaEspecialista();
        assertThat(consultaEspecialista1).isNotEqualTo(consultaEspecialista2);

        consultaEspecialista2.setId(consultaEspecialista1.getId());
        assertThat(consultaEspecialista1).isEqualTo(consultaEspecialista2);

        consultaEspecialista2 = getConsultaEspecialistaSample2();
        assertThat(consultaEspecialista1).isNotEqualTo(consultaEspecialista2);
    }

    @Test
    void especialistaTest() throws Exception {
        ConsultaEspecialista consultaEspecialista = getConsultaEspecialistaRandomSampleGenerator();
        Especialista especialistaBack = getEspecialistaRandomSampleGenerator();

        consultaEspecialista.setEspecialista(especialistaBack);
        assertThat(consultaEspecialista.getEspecialista()).isEqualTo(especialistaBack);

        consultaEspecialista.especialista(null);
        assertThat(consultaEspecialista.getEspecialista()).isNull();
    }
}
