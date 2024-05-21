package com.healthscore.app.domain;

import static com.healthscore.app.domain.ControleMedicamentosTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.healthscore.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ControleMedicamentosTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ControleMedicamentos.class);
        ControleMedicamentos controleMedicamentos1 = getControleMedicamentosSample1();
        ControleMedicamentos controleMedicamentos2 = new ControleMedicamentos();
        assertThat(controleMedicamentos1).isNotEqualTo(controleMedicamentos2);

        controleMedicamentos2.setId(controleMedicamentos1.getId());
        assertThat(controleMedicamentos1).isEqualTo(controleMedicamentos2);

        controleMedicamentos2 = getControleMedicamentosSample2();
        assertThat(controleMedicamentos1).isNotEqualTo(controleMedicamentos2);
    }
}
