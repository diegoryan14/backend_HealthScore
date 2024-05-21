package com.healthscore.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AtividadeFisicaCriteriaTest {

    @Test
    void newAtividadeFisicaCriteriaHasAllFiltersNullTest() {
        var atividadeFisicaCriteria = new AtividadeFisicaCriteria();
        assertThat(atividadeFisicaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void atividadeFisicaCriteriaFluentMethodsCreatesFiltersTest() {
        var atividadeFisicaCriteria = new AtividadeFisicaCriteria();

        setAllFilters(atividadeFisicaCriteria);

        assertThat(atividadeFisicaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void atividadeFisicaCriteriaCopyCreatesNullFilterTest() {
        var atividadeFisicaCriteria = new AtividadeFisicaCriteria();
        var copy = atividadeFisicaCriteria.copy();

        assertThat(atividadeFisicaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(atividadeFisicaCriteria)
        );
    }

    @Test
    void atividadeFisicaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var atividadeFisicaCriteria = new AtividadeFisicaCriteria();
        setAllFilters(atividadeFisicaCriteria);

        var copy = atividadeFisicaCriteria.copy();

        assertThat(atividadeFisicaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(atividadeFisicaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var atividadeFisicaCriteria = new AtividadeFisicaCriteria();

        assertThat(atividadeFisicaCriteria).hasToString("AtividadeFisicaCriteria{}");
    }

    private static void setAllFilters(AtividadeFisicaCriteria atividadeFisicaCriteria) {
        atividadeFisicaCriteria.id();
        atividadeFisicaCriteria.tipoAtividade();
        atividadeFisicaCriteria.dataHorario();
        atividadeFisicaCriteria.duracao();
        atividadeFisicaCriteria.passosCalorias();
        atividadeFisicaCriteria.internalUserId();
        atividadeFisicaCriteria.distinct();
    }

    private static Condition<AtividadeFisicaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTipoAtividade()) &&
                condition.apply(criteria.getDataHorario()) &&
                condition.apply(criteria.getDuracao()) &&
                condition.apply(criteria.getPassosCalorias()) &&
                condition.apply(criteria.getInternalUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AtividadeFisicaCriteria> copyFiltersAre(
        AtividadeFisicaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTipoAtividade(), copy.getTipoAtividade()) &&
                condition.apply(criteria.getDataHorario(), copy.getDataHorario()) &&
                condition.apply(criteria.getDuracao(), copy.getDuracao()) &&
                condition.apply(criteria.getPassosCalorias(), copy.getPassosCalorias()) &&
                condition.apply(criteria.getInternalUserId(), copy.getInternalUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
