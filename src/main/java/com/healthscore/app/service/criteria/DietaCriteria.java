package com.healthscore.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.healthscore.app.domain.Dieta} entity. This class is used
 * in {@link com.healthscore.app.web.rest.DietaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /dietas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DietaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter descricaoRefeicao;

    private InstantFilter dataHorarioRefeicao;

    private IntegerFilter caloriasConsumidas;

    private LongFilter internalUserId;

    private Boolean distinct;

    public DietaCriteria() {}

    public DietaCriteria(DietaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.descricaoRefeicao = other.optionalDescricaoRefeicao().map(StringFilter::copy).orElse(null);
        this.dataHorarioRefeicao = other.optionalDataHorarioRefeicao().map(InstantFilter::copy).orElse(null);
        this.caloriasConsumidas = other.optionalCaloriasConsumidas().map(IntegerFilter::copy).orElse(null);
        this.internalUserId = other.optionalInternalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DietaCriteria copy() {
        return new DietaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDescricaoRefeicao() {
        return descricaoRefeicao;
    }

    public Optional<StringFilter> optionalDescricaoRefeicao() {
        return Optional.ofNullable(descricaoRefeicao);
    }

    public StringFilter descricaoRefeicao() {
        if (descricaoRefeicao == null) {
            setDescricaoRefeicao(new StringFilter());
        }
        return descricaoRefeicao;
    }

    public void setDescricaoRefeicao(StringFilter descricaoRefeicao) {
        this.descricaoRefeicao = descricaoRefeicao;
    }

    public InstantFilter getDataHorarioRefeicao() {
        return dataHorarioRefeicao;
    }

    public Optional<InstantFilter> optionalDataHorarioRefeicao() {
        return Optional.ofNullable(dataHorarioRefeicao);
    }

    public InstantFilter dataHorarioRefeicao() {
        if (dataHorarioRefeicao == null) {
            setDataHorarioRefeicao(new InstantFilter());
        }
        return dataHorarioRefeicao;
    }

    public void setDataHorarioRefeicao(InstantFilter dataHorarioRefeicao) {
        this.dataHorarioRefeicao = dataHorarioRefeicao;
    }

    public IntegerFilter getCaloriasConsumidas() {
        return caloriasConsumidas;
    }

    public Optional<IntegerFilter> optionalCaloriasConsumidas() {
        return Optional.ofNullable(caloriasConsumidas);
    }

    public IntegerFilter caloriasConsumidas() {
        if (caloriasConsumidas == null) {
            setCaloriasConsumidas(new IntegerFilter());
        }
        return caloriasConsumidas;
    }

    public void setCaloriasConsumidas(IntegerFilter caloriasConsumidas) {
        this.caloriasConsumidas = caloriasConsumidas;
    }

    public LongFilter getInternalUserId() {
        return internalUserId;
    }

    public Optional<LongFilter> optionalInternalUserId() {
        return Optional.ofNullable(internalUserId);
    }

    public LongFilter internalUserId() {
        if (internalUserId == null) {
            setInternalUserId(new LongFilter());
        }
        return internalUserId;
    }

    public void setInternalUserId(LongFilter internalUserId) {
        this.internalUserId = internalUserId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DietaCriteria that = (DietaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(descricaoRefeicao, that.descricaoRefeicao) &&
            Objects.equals(dataHorarioRefeicao, that.dataHorarioRefeicao) &&
            Objects.equals(caloriasConsumidas, that.caloriasConsumidas) &&
            Objects.equals(internalUserId, that.internalUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descricaoRefeicao, dataHorarioRefeicao, caloriasConsumidas, internalUserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DietaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDescricaoRefeicao().map(f -> "descricaoRefeicao=" + f + ", ").orElse("") +
            optionalDataHorarioRefeicao().map(f -> "dataHorarioRefeicao=" + f + ", ").orElse("") +
            optionalCaloriasConsumidas().map(f -> "caloriasConsumidas=" + f + ", ").orElse("") +
            optionalInternalUserId().map(f -> "internalUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
