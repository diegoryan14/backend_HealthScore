package com.healthscore.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DietaCriteriaTest {

    @Test
    void newDietaCriteriaHasAllFiltersNullTest() {
        var dietaCriteria = new DietaCriteria();
        assertThat(dietaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void dietaCriteriaFluentMethodsCreatesFiltersTest() {
        var dietaCriteria = new DietaCriteria();

        setAllFilters(dietaCriteria);

        assertThat(dietaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void dietaCriteriaCopyCreatesNullFilterTest() {
        var dietaCriteria = new DietaCriteria();
        var copy = dietaCriteria.copy();

        assertThat(dietaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(dietaCriteria)
        );
    }

    @Test
    void dietaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var dietaCriteria = new DietaCriteria();
        setAllFilters(dietaCriteria);

        var copy = dietaCriteria.copy();

        assertThat(dietaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(dietaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var dietaCriteria = new DietaCriteria();

        assertThat(dietaCriteria).hasToString("DietaCriteria{}");
    }

    private static void setAllFilters(DietaCriteria dietaCriteria) {
        dietaCriteria.id();
        dietaCriteria.descricaoRefeicao();
        dietaCriteria.dataHorarioRefeicao();
        dietaCriteria.caloriasConsumidas();
        dietaCriteria.internalUserId();
        dietaCriteria.distinct();
    }

    private static Condition<DietaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDescricaoRefeicao()) &&
                condition.apply(criteria.getDataHorarioRefeicao()) &&
                condition.apply(criteria.getCaloriasConsumidas()) &&
                condition.apply(criteria.getInternalUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DietaCriteria> copyFiltersAre(DietaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDescricaoRefeicao(), copy.getDescricaoRefeicao()) &&
                condition.apply(criteria.getDataHorarioRefeicao(), copy.getDataHorarioRefeicao()) &&
                condition.apply(criteria.getCaloriasConsumidas(), copy.getCaloriasConsumidas()) &&
                condition.apply(criteria.getInternalUserId(), copy.getInternalUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
