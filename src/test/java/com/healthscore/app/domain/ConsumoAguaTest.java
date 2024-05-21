package com.healthscore.app.domain;

import static com.healthscore.app.domain.ConsumoAguaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.healthscore.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsumoAguaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsumoAgua.class);
        ConsumoAgua consumoAgua1 = getConsumoAguaSample1();
        ConsumoAgua consumoAgua2 = new ConsumoAgua();
        assertThat(consumoAgua1).isNotEqualTo(consumoAgua2);

        consumoAgua2.setId(consumoAgua1.getId());
        assertThat(consumoAgua1).isEqualTo(consumoAgua2);

        consumoAgua2 = getConsumoAguaSample2();
        assertThat(consumoAgua1).isNotEqualTo(consumoAgua2);
    }
}
