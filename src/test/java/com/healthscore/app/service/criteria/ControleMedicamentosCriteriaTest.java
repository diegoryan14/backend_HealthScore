package com.healthscore.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ControleMedicamentosCriteriaTest {

    @Test
    void newControleMedicamentosCriteriaHasAllFiltersNullTest() {
        var controleMedicamentosCriteria = new ControleMedicamentosCriteria();
        assertThat(controleMedicamentosCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void controleMedicamentosCriteriaFluentMethodsCreatesFiltersTest() {
        var controleMedicamentosCriteria = new ControleMedicamentosCriteria();

        setAllFilters(controleMedicamentosCriteria);

        assertThat(controleMedicamentosCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void controleMedicamentosCriteriaCopyCreatesNullFilterTest() {
        var controleMedicamentosCriteria = new ControleMedicamentosCriteria();
        var copy = controleMedicamentosCriteria.copy();

        assertThat(controleMedicamentosCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(controleMedicamentosCriteria)
        );
    }

    @Test
    void controleMedicamentosCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var controleMedicamentosCriteria = new ControleMedicamentosCriteria();
        setAllFilters(controleMedicamentosCriteria);

        var copy = controleMedicamentosCriteria.copy();

        assertThat(controleMedicamentosCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(controleMedicamentosCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var controleMedicamentosCriteria = new ControleMedicamentosCriteria();

        assertThat(controleMedicamentosCriteria).hasToString("ControleMedicamentosCriteria{}");
    }

    private static void setAllFilters(ControleMedicamentosCriteria controleMedicamentosCriteria) {
        controleMedicamentosCriteria.id();
        controleMedicamentosCriteria.nomeMedicamento();
        controleMedicamentosCriteria.dosagem();
        controleMedicamentosCriteria.horarioIngestao();
        controleMedicamentosCriteria.internalUserId();
        controleMedicamentosCriteria.distinct();
    }

    private static Condition<ControleMedicamentosCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNomeMedicamento()) &&
                condition.apply(criteria.getDosagem()) &&
                condition.apply(criteria.getHorarioIngestao()) &&
                condition.apply(criteria.getInternalUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ControleMedicamentosCriteria> copyFiltersAre(
        ControleMedicamentosCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNomeMedicamento(), copy.getNomeMedicamento()) &&
                condition.apply(criteria.getDosagem(), copy.getDosagem()) &&
                condition.apply(criteria.getHorarioIngestao(), copy.getHorarioIngestao()) &&
                condition.apply(criteria.getInternalUserId(), copy.getInternalUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
