package com.healthscore.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ConsultaEspecialistaCriteriaTest {

    @Test
    void newConsultaEspecialistaCriteriaHasAllFiltersNullTest() {
        var consultaEspecialistaCriteria = new ConsultaEspecialistaCriteria();
        assertThat(consultaEspecialistaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void consultaEspecialistaCriteriaFluentMethodsCreatesFiltersTest() {
        var consultaEspecialistaCriteria = new ConsultaEspecialistaCriteria();

        setAllFilters(consultaEspecialistaCriteria);

        assertThat(consultaEspecialistaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void consultaEspecialistaCriteriaCopyCreatesNullFilterTest() {
        var consultaEspecialistaCriteria = new ConsultaEspecialistaCriteria();
        var copy = consultaEspecialistaCriteria.copy();

        assertThat(consultaEspecialistaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(consultaEspecialistaCriteria)
        );
    }

    @Test
    void consultaEspecialistaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var consultaEspecialistaCriteria = new ConsultaEspecialistaCriteria();
        setAllFilters(consultaEspecialistaCriteria);

        var copy = consultaEspecialistaCriteria.copy();

        assertThat(consultaEspecialistaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(consultaEspecialistaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var consultaEspecialistaCriteria = new ConsultaEspecialistaCriteria();

        assertThat(consultaEspecialistaCriteria).hasToString("ConsultaEspecialistaCriteria{}");
    }

    private static void setAllFilters(ConsultaEspecialistaCriteria consultaEspecialistaCriteria) {
        consultaEspecialistaCriteria.id();
        consultaEspecialistaCriteria.tipoEspecialista();
        consultaEspecialistaCriteria.dataHorarioConsulta();
        consultaEspecialistaCriteria.statusConsulta();
        consultaEspecialistaCriteria.linkConsulta();
        consultaEspecialistaCriteria.internalUserId();
        consultaEspecialistaCriteria.especialistaId();
        consultaEspecialistaCriteria.distinct();
    }

    private static Condition<ConsultaEspecialistaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTipoEspecialista()) &&
                condition.apply(criteria.getDataHorarioConsulta()) &&
                condition.apply(criteria.getStatusConsulta()) &&
                condition.apply(criteria.getLinkConsulta()) &&
                condition.apply(criteria.getInternalUserId()) &&
                condition.apply(criteria.getEspecialistaId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ConsultaEspecialistaCriteria> copyFiltersAre(
        ConsultaEspecialistaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTipoEspecialista(), copy.getTipoEspecialista()) &&
                condition.apply(criteria.getDataHorarioConsulta(), copy.getDataHorarioConsulta()) &&
                condition.apply(criteria.getStatusConsulta(), copy.getStatusConsulta()) &&
                condition.apply(criteria.getLinkConsulta(), copy.getLinkConsulta()) &&
                condition.apply(criteria.getInternalUserId(), copy.getInternalUserId()) &&
                condition.apply(criteria.getEspecialistaId(), copy.getEspecialistaId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
