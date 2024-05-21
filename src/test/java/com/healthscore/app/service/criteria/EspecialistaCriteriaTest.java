package com.healthscore.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EspecialistaCriteriaTest {

    @Test
    void newEspecialistaCriteriaHasAllFiltersNullTest() {
        var especialistaCriteria = new EspecialistaCriteria();
        assertThat(especialistaCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void especialistaCriteriaFluentMethodsCreatesFiltersTest() {
        var especialistaCriteria = new EspecialistaCriteria();

        setAllFilters(especialistaCriteria);

        assertThat(especialistaCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void especialistaCriteriaCopyCreatesNullFilterTest() {
        var especialistaCriteria = new EspecialistaCriteria();
        var copy = especialistaCriteria.copy();

        assertThat(especialistaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(especialistaCriteria)
        );
    }

    @Test
    void especialistaCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var especialistaCriteria = new EspecialistaCriteria();
        setAllFilters(especialistaCriteria);

        var copy = especialistaCriteria.copy();

        assertThat(especialistaCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(especialistaCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var especialistaCriteria = new EspecialistaCriteria();

        assertThat(especialistaCriteria).hasToString("EspecialistaCriteria{}");
    }

    private static void setAllFilters(EspecialistaCriteria especialistaCriteria) {
        especialistaCriteria.id();
        especialistaCriteria.nome();
        especialistaCriteria.cpf();
        especialistaCriteria.especializacao();
        especialistaCriteria.dataFormacao();
        especialistaCriteria.telefone();
        especialistaCriteria.email();
        especialistaCriteria.dataNascimento();
        especialistaCriteria.consultasId();
        especialistaCriteria.distinct();
    }

    private static Condition<EspecialistaCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNome()) &&
                condition.apply(criteria.getCpf()) &&
                condition.apply(criteria.getEspecializacao()) &&
                condition.apply(criteria.getDataFormacao()) &&
                condition.apply(criteria.getTelefone()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getDataNascimento()) &&
                condition.apply(criteria.getConsultasId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EspecialistaCriteria> copyFiltersAre(
        EspecialistaCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNome(), copy.getNome()) &&
                condition.apply(criteria.getCpf(), copy.getCpf()) &&
                condition.apply(criteria.getEspecializacao(), copy.getEspecializacao()) &&
                condition.apply(criteria.getDataFormacao(), copy.getDataFormacao()) &&
                condition.apply(criteria.getTelefone(), copy.getTelefone()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getDataNascimento(), copy.getDataNascimento()) &&
                condition.apply(criteria.getConsultasId(), copy.getConsultasId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
