package com.healthscore.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PontuacaoUsuarioAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPontuacaoUsuarioAllPropertiesEquals(PontuacaoUsuario expected, PontuacaoUsuario actual) {
        assertPontuacaoUsuarioAutoGeneratedPropertiesEquals(expected, actual);
        assertPontuacaoUsuarioAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPontuacaoUsuarioAllUpdatablePropertiesEquals(PontuacaoUsuario expected, PontuacaoUsuario actual) {
        assertPontuacaoUsuarioUpdatableFieldsEquals(expected, actual);
        assertPontuacaoUsuarioUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPontuacaoUsuarioAutoGeneratedPropertiesEquals(PontuacaoUsuario expected, PontuacaoUsuario actual) {
        assertThat(expected)
            .as("Verify PontuacaoUsuario auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPontuacaoUsuarioUpdatableFieldsEquals(PontuacaoUsuario expected, PontuacaoUsuario actual) {
        assertThat(expected)
            .as("Verify PontuacaoUsuario relevant properties")
            .satisfies(e -> assertThat(e.getDataAlteracao()).as("check dataAlteracao").isEqualTo(actual.getDataAlteracao()))
            .satisfies(e -> assertThat(e.getValorAlteracao()).as("check valorAlteracao").isEqualTo(actual.getValorAlteracao()))
            .satisfies(e -> assertThat(e.getMotivo()).as("check motivo").isEqualTo(actual.getMotivo()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPontuacaoUsuarioUpdatableRelationshipsEquals(PontuacaoUsuario expected, PontuacaoUsuario actual) {
        assertThat(expected)
            .as("Verify PontuacaoUsuario relationships")
            .satisfies(e -> assertThat(e.getUsuario()).as("check usuario").isEqualTo(actual.getUsuario()));
    }
}
