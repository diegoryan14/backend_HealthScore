package com.healthscore.app.domain;

import static com.healthscore.app.domain.PontuacaoUsuarioTestSamples.*;
import static com.healthscore.app.domain.UsuarioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.healthscore.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PontuacaoUsuarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PontuacaoUsuario.class);
        PontuacaoUsuario pontuacaoUsuario1 = getPontuacaoUsuarioSample1();
        PontuacaoUsuario pontuacaoUsuario2 = new PontuacaoUsuario();
        assertThat(pontuacaoUsuario1).isNotEqualTo(pontuacaoUsuario2);

        pontuacaoUsuario2.setId(pontuacaoUsuario1.getId());
        assertThat(pontuacaoUsuario1).isEqualTo(pontuacaoUsuario2);

        pontuacaoUsuario2 = getPontuacaoUsuarioSample2();
        assertThat(pontuacaoUsuario1).isNotEqualTo(pontuacaoUsuario2);
    }

    @Test
    void usuarioTest() throws Exception {
        PontuacaoUsuario pontuacaoUsuario = getPontuacaoUsuarioRandomSampleGenerator();
        Usuario usuarioBack = getUsuarioRandomSampleGenerator();

        pontuacaoUsuario.setUsuario(usuarioBack);
        assertThat(pontuacaoUsuario.getUsuario()).isEqualTo(usuarioBack);

        pontuacaoUsuario.usuario(null);
        assertThat(pontuacaoUsuario.getUsuario()).isNull();
    }
}
