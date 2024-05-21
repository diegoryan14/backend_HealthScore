package com.healthscore.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ConsumoAguaCriteriaTest {

    @Test
    void newConsumoAguaCriteriaHasAllFiltersNullTest() {
        var consumoAguaCriteria = new ConsumoAguaCriteria();
        assertThat(consumoAguaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void consumoAguaCriteriaFluentMethodsCreatesFiltersTest() {
        var consumoAguaCriteria = new ConsumoAguaCriteria();

        setAllFilters(consumoAguaCriteria);

        assertThat(consumoAguaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void consumoAguaCriteriaCopyCreatesNullFilterTest() {
        var consumoAguaCriteria = new ConsumoAguaCriteria();
        var copy = consumoAguaCriteria.copy();

        assertThat(consumoAguaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(consumoAguaCriteria)
        );
    }

    @Test
    void consumoAguaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var consumoAguaCriteria = new ConsumoAguaCriteria();
        setAllFilters(consumoAguaCriteria);

        var copy = consumoAguaCriteria.copy();

        assertThat(consumoAguaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(consumoAguaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var consumoAguaCriteria = new ConsumoAguaCriteria();

        assertThat(consumoAguaCriteria).hasToString("ConsumoAguaCriteria{}");
    }

    private static void setAllFilters(ConsumoAguaCriteria consumoAguaCriteria) {
        consumoAguaCriteria.id();
        consumoAguaCriteria.dataConsumo();
        consumoAguaCriteria.quantidadeMl();
        consumoAguaCriteria.internalUserId();
        consumoAguaCriteria.distinct();
    }

    private static Condition<ConsumoAguaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDataConsumo()) &&
                condition.apply(criteria.getQuantidadeMl()) &&
                condition.apply(criteria.getInternalUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ConsumoAguaCriteria> copyFiltersAre(ConsumoAguaCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDataConsumo(), copy.getDataConsumo()) &&
                condition.apply(criteria.getQuantidadeMl(), copy.getQuantidadeMl()) &&
                condition.apply(criteria.getInternalUserId(), copy.getInternalUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
