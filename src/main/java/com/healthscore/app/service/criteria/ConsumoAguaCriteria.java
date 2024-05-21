package com.healthscore.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.healthscore.app.domain.ConsumoAgua} entity. This class is used
 * in {@link com.healthscore.app.web.rest.ConsumoAguaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /consumo-aguas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsumoAguaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dataConsumo;

    private IntegerFilter quantidadeMl;

    private LongFilter internalUserId;

    private Boolean distinct;

    public ConsumoAguaCriteria() {}

    public ConsumoAguaCriteria(ConsumoAguaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dataConsumo = other.optionalDataConsumo().map(InstantFilter::copy).orElse(null);
        this.quantidadeMl = other.optionalQuantidadeMl().map(IntegerFilter::copy).orElse(null);
        this.internalUserId = other.optionalInternalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ConsumoAguaCriteria copy() {
        return new ConsumoAguaCriteria(this);
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

    public InstantFilter getDataConsumo() {
        return dataConsumo;
    }

    public Optional<InstantFilter> optionalDataConsumo() {
        return Optional.ofNullable(dataConsumo);
    }

    public InstantFilter dataConsumo() {
        if (dataConsumo == null) {
            setDataConsumo(new InstantFilter());
        }
        return dataConsumo;
    }

    public void setDataConsumo(InstantFilter dataConsumo) {
        this.dataConsumo = dataConsumo;
    }

    public IntegerFilter getQuantidadeMl() {
        return quantidadeMl;
    }

    public Optional<IntegerFilter> optionalQuantidadeMl() {
        return Optional.ofNullable(quantidadeMl);
    }

    public IntegerFilter quantidadeMl() {
        if (quantidadeMl == null) {
            setQuantidadeMl(new IntegerFilter());
        }
        return quantidadeMl;
    }

    public void setQuantidadeMl(IntegerFilter quantidadeMl) {
        this.quantidadeMl = quantidadeMl;
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
        final ConsumoAguaCriteria that = (ConsumoAguaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataConsumo, that.dataConsumo) &&
            Objects.equals(quantidadeMl, that.quantidadeMl) &&
            Objects.equals(internalUserId, that.internalUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataConsumo, quantidadeMl, internalUserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsumoAguaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDataConsumo().map(f -> "dataConsumo=" + f + ", ").orElse("") +
            optionalQuantidadeMl().map(f -> "quantidadeMl=" + f + ", ").orElse("") +
            optionalInternalUserId().map(f -> "internalUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
