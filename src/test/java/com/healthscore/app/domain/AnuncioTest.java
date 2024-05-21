package com.healthscore.app.domain;

import static com.healthscore.app.domain.AnuncioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.healthscore.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnuncioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Anuncio.class);
        Anuncio anuncio1 = getAnuncioSample1();
        Anuncio anuncio2 = new Anuncio();
        assertThat(anuncio1).isNotEqualTo(anuncio2);

        anuncio2.setId(anuncio1.getId());
        assertThat(anuncio1).isEqualTo(anuncio2);

        anuncio2 = getAnuncioSample2();
        assertThat(anuncio1).isNotEqualTo(anuncio2);
    }
}
