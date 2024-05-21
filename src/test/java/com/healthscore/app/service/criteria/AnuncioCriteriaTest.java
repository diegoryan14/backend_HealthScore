package com.healthscore.app.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AnuncioCriteriaTest {

    @Test
    void newAnuncioCriteriaHasAllFiltersNullTest() {
        var anuncioCriteria = new AnuncioCriteria();
        assertThat(anuncioCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void anuncioCriteriaFluentMethodsCreatesFiltersTest() {
        var anuncioCriteria = new AnuncioCriteria();

        setAllFilters(anuncioCriteria);

        assertThat(anuncioCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void anuncioCriteriaCopyCreatesNullFilterTest() {
        var anuncioCriteria = new AnuncioCriteria();
        var copy = anuncioCriteria.copy();

        assertThat(anuncioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(anuncioCriteria)
        );
    }

    @Test
    void anuncioCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var anuncioCriteria = new AnuncioCriteria();
        setAllFilters(anuncioCriteria);

        var copy = anuncioCriteria.copy();

        assertThat(anuncioCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(anuncioCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var anuncioCriteria = new AnuncioCriteria();

        assertThat(anuncioCriteria).hasToString("AnuncioCriteria{}");
    }

    private static void setAllFilters(AnuncioCriteria anuncioCriteria) {
        anuncioCriteria.id();
        anuncioCriteria.titulo();
        anuncioCriteria.descricao();
        anuncioCriteria.dataPublicacao();
        anuncioCriteria.dataInicio();
        anuncioCriteria.dataFim();
        anuncioCriteria.preco();
        anuncioCriteria.distinct();
    }

    private static Condition<AnuncioCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitulo()) &&
                condition.apply(criteria.getDescricao()) &&
                condition.apply(criteria.getDataPublicacao()) &&
                condition.apply(criteria.getDataInicio()) &&
                condition.apply(criteria.getDataFim()) &&
                condition.apply(criteria.getPreco()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AnuncioCriteria> copyFiltersAre(AnuncioCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitulo(), copy.getTitulo()) &&
                condition.apply(criteria.getDescricao(), copy.getDescricao()) &&
                condition.apply(criteria.getDataPublicacao(), copy.getDataPublicacao()) &&
                condition.apply(criteria.getDataInicio(), copy.getDataInicio()) &&
                condition.apply(criteria.getDataFim(), copy.getDataFim()) &&
                condition.apply(criteria.getPreco(), copy.getPreco()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
