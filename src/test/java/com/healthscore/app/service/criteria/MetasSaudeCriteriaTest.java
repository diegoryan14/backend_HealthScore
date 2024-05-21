package com.healthscore.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MetasSaudeCriteriaTest {

    @Test
    void newMetasSaudeCriteriaHasAllFiltersNullTest() {
        var metasSaudeCriteria = new MetasSaudeCriteria();
        assertThat(metasSaudeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void metasSaudeCriteriaFluentMethodsCreatesFiltersTest() {
        var metasSaudeCriteria = new MetasSaudeCriteria();

        setAllFilters(metasSaudeCriteria);

        assertThat(metasSaudeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void metasSaudeCriteriaCopyCreatesNullFilterTest() {
        var metasSaudeCriteria = new MetasSaudeCriteria();
        var copy = metasSaudeCriteria.copy();

        assertThat(metasSaudeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(metasSaudeCriteria)
        );
    }

    @Test
    void metasSaudeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var metasSaudeCriteria = new MetasSaudeCriteria();
        setAllFilters(metasSaudeCriteria);

        var copy = metasSaudeCriteria.copy();

        assertThat(metasSaudeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(metasSaudeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var metasSaudeCriteria = new MetasSaudeCriteria();

        assertThat(metasSaudeCriteria).hasToString("MetasSaudeCriteria{}");
    }

    private static void setAllFilters(MetasSaudeCriteria metasSaudeCriteria) {
        metasSaudeCriteria.id();
        metasSaudeCriteria.tipoMeta();
        metasSaudeCriteria.valorMeta();
        metasSaudeCriteria.dataInicio();
        metasSaudeCriteria.dataFim();
        metasSaudeCriteria.internalUserId();
        metasSaudeCriteria.distinct();
    }

    private static Condition<MetasSaudeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTipoMeta()) &&
                condition.apply(criteria.getValorMeta()) &&
                condition.apply(criteria.getDataInicio()) &&
                condition.apply(criteria.getDataFim()) &&
                condition.apply(criteria.getInternalUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MetasSaudeCriteria> copyFiltersAre(MetasSaudeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTipoMeta(), copy.getTipoMeta()) &&
                condition.apply(criteria.getValorMeta(), copy.getValorMeta()) &&
                condition.apply(criteria.getDataInicio(), copy.getDataInicio()) &&
                condition.apply(criteria.getDataFim(), copy.getDataFim()) &&
                condition.apply(criteria.getInternalUserId(), copy.getInternalUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
