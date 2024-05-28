package com.healthscore.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UsuarioCriteriaTest {

    @Test
    void newUsuarioCriteriaHasAllFiltersNullTest() {
        var usuarioCriteria = new UsuarioCriteria();
        assertThat(usuarioCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void usuarioCriteriaFluentMethodsCreatesFiltersTest() {
        var usuarioCriteria = new UsuarioCriteria();

        setAllFilters(usuarioCriteria);

        assertThat(usuarioCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void usuarioCriteriaCopyCreatesNullFilterTest() {
        var usuarioCriteria = new UsuarioCriteria();
        var copy = usuarioCriteria.copy();

        assertThat(usuarioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(usuarioCriteria)
        );
    }

    @Test
    void usuarioCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var usuarioCriteria = new UsuarioCriteria();
        setAllFilters(usuarioCriteria);

        var copy = usuarioCriteria.copy();

        assertThat(usuarioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(usuarioCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var usuarioCriteria = new UsuarioCriteria();

        assertThat(usuarioCriteria).hasToString("UsuarioCriteria{}");
    }

    private static void setAllFilters(UsuarioCriteria usuarioCriteria) {
        usuarioCriteria.id();
        usuarioCriteria.plano();
        usuarioCriteria.dataRegistro();
        usuarioCriteria.telefone();
        usuarioCriteria.email();
        usuarioCriteria.dataNascimento();
        usuarioCriteria.metaConsumoAgua();
        usuarioCriteria.metaSono();
        usuarioCriteria.metaCaloriasConsumidas();
        usuarioCriteria.metaCaloriasQueimadas();
        usuarioCriteria.pontosUser();
        usuarioCriteria.genero();
        usuarioCriteria.internalUserId();
        usuarioCriteria.distinct();
    }

    private static Condition<UsuarioCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPlano()) &&
                condition.apply(criteria.getDataRegistro()) &&
                condition.apply(criteria.getTelefone()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getDataNascimento()) &&
                condition.apply(criteria.getMetaConsumoAgua()) &&
                condition.apply(criteria.getMetaSono()) &&
                condition.apply(criteria.getMetaCaloriasConsumidas()) &&
                condition.apply(criteria.getMetaCaloriasQueimadas()) &&
                condition.apply(criteria.getPontosUser()) &&
                condition.apply(criteria.getGenero()) &&
                condition.apply(criteria.getInternalUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UsuarioCriteria> copyFiltersAre(UsuarioCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPlano(), copy.getPlano()) &&
                condition.apply(criteria.getDataRegistro(), copy.getDataRegistro()) &&
                condition.apply(criteria.getTelefone(), copy.getTelefone()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getDataNascimento(), copy.getDataNascimento()) &&
                condition.apply(criteria.getMetaConsumoAgua(), copy.getMetaConsumoAgua()) &&
                condition.apply(criteria.getMetaSono(), copy.getMetaSono()) &&
                condition.apply(criteria.getMetaCaloriasConsumidas(), copy.getMetaCaloriasConsumidas()) &&
                condition.apply(criteria.getMetaCaloriasQueimadas(), copy.getMetaCaloriasQueimadas()) &&
                condition.apply(criteria.getPontosUser(), copy.getPontosUser()) &&
                condition.apply(criteria.getGenero(), copy.getGenero()) &&
                condition.apply(criteria.getInternalUserId(), copy.getInternalUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
