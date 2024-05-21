package com.healthscore.app.domain;

import static com.healthscore.app.domain.QualidadeSonoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.healthscore.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QualidadeSonoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QualidadeSono.class);
        QualidadeSono qualidadeSono1 = getQualidadeSonoSample1();
        QualidadeSono qualidadeSono2 = new QualidadeSono();
        assertThat(qualidadeSono1).isNotEqualTo(qualidadeSono2);

        qualidadeSono2.setId(qualidadeSono1.getId());
        assertThat(qualidadeSono1).isEqualTo(qualidadeSono2);

        qualidadeSono2 = getQualidadeSonoSample2();
        assertThat(qualidadeSono1).isNotEqualTo(qualidadeSono2);
    }
}
