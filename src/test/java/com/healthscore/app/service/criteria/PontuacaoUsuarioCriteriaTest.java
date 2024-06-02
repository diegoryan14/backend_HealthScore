package com.healthscore.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PontuacaoUsuarioCriteriaTest {

    @Test
    void newPontuacaoUsuarioCriteriaHasAllFiltersNullTest() {
        var pontuacaoUsuarioCriteria = new PontuacaoUsuarioCriteria();
        assertThat(pontuacaoUsuarioCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void pontuacaoUsuarioCriteriaFluentMethodsCreatesFiltersTest() {
        var pontuacaoUsuarioCriteria = new PontuacaoUsuarioCriteria();

        setAllFilters(pontuacaoUsuarioCriteria);

        assertThat(pontuacaoUsuarioCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void pontuacaoUsuarioCriteriaCopyCreatesNullFilterTest() {
        var pontuacaoUsuarioCriteria = new PontuacaoUsuarioCriteria();
        var copy = pontuacaoUsuarioCriteria.copy();

        assertThat(pontuacaoUsuarioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(pontuacaoUsuarioCriteria)
        );
    }

    @Test
    void pontuacaoUsuarioCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pontuacaoUsuarioCriteria = new PontuacaoUsuarioCriteria();
        setAllFilters(pontuacaoUsuarioCriteria);

        var copy = pontuacaoUsuarioCriteria.copy();

        assertThat(pontuacaoUsuarioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(pontuacaoUsuarioCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pontuacaoUsuarioCriteria = new PontuacaoUsuarioCriteria();

        assertThat(pontuacaoUsuarioCriteria).hasToString("PontuacaoUsuarioCriteria{}");
    }

    private static void setAllFilters(PontuacaoUsuarioCriteria pontuacaoUsuarioCriteria) {
        pontuacaoUsuarioCriteria.id();
        pontuacaoUsuarioCriteria.dataAlteracao();
        pontuacaoUsuarioCriteria.valorAlteracao();
        pontuacaoUsuarioCriteria.motivo();
        pontuacaoUsuarioCriteria.usuarioId();
        pontuacaoUsuarioCriteria.distinct();
    }

    private static Condition<PontuacaoUsuarioCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDataAlteracao()) &&
                condition.apply(criteria.getValorAlteracao()) &&
                condition.apply(criteria.getMotivo()) &&
                condition.apply(criteria.getUsuarioId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PontuacaoUsuarioCriteria> copyFiltersAre(
        PontuacaoUsuarioCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDataAlteracao(), copy.getDataAlteracao()) &&
                condition.apply(criteria.getValorAlteracao(), copy.getValorAlteracao()) &&
                condition.apply(criteria.getMotivo(), copy.getMotivo()) &&
                condition.apply(criteria.getUsuarioId(), copy.getUsuarioId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
