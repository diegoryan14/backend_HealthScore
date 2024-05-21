package com.healthscore.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class QualidadeSonoCriteriaTest {

    @Test
    void newQualidadeSonoCriteriaHasAllFiltersNullTest() {
        var qualidadeSonoCriteria = new QualidadeSonoCriteria();
        assertThat(qualidadeSonoCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void qualidadeSonoCriteriaFluentMethodsCreatesFiltersTest() {
        var qualidadeSonoCriteria = new QualidadeSonoCriteria();

        setAllFilters(qualidadeSonoCriteria);

        assertThat(qualidadeSonoCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void qualidadeSonoCriteriaCopyCreatesNullFilterTest() {
        var qualidadeSonoCriteria = new QualidadeSonoCriteria();
        var copy = qualidadeSonoCriteria.copy();

        assertThat(qualidadeSonoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(qualidadeSonoCriteria)
        );
    }

    @Test
    void qualidadeSonoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var qualidadeSonoCriteria = new QualidadeSonoCriteria();
        setAllFilters(qualidadeSonoCriteria);

        var copy = qualidadeSonoCriteria.copy();

        assertThat(qualidadeSonoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(qualidadeSonoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var qualidadeSonoCriteria = new QualidadeSonoCriteria();

        assertThat(qualidadeSonoCriteria).hasToString("QualidadeSonoCriteria{}");
    }

    private static void setAllFilters(QualidadeSonoCriteria qualidadeSonoCriteria) {
        qualidadeSonoCriteria.id();
        qualidadeSonoCriteria.data();
        qualidadeSonoCriteria.horasSono();
        qualidadeSonoCriteria.internalUserId();
        qualidadeSonoCriteria.distinct();
    }

    private static Condition<QualidadeSonoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getData()) &&
                condition.apply(criteria.getHorasSono()) &&
                condition.apply(criteria.getInternalUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<QualidadeSonoCriteria> copyFiltersAre(
        QualidadeSonoCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getData(), copy.getData()) &&
                condition.apply(criteria.getHorasSono(), copy.getHorasSono()) &&
                condition.apply(criteria.getInternalUserId(), copy.getInternalUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
