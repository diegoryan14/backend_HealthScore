package com.healthscore.app.service.criteria;

import com.healthscore.app.domain.enumeration.TipoMeta;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.healthscore.app.domain.MetasSaude} entity. This class is used
 * in {@link com.healthscore.app.web.rest.MetasSaudeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /metas-saudes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MetasSaudeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoMeta
     */
    public static class TipoMetaFilter extends Filter<TipoMeta> {

        public TipoMetaFilter() {}

        public TipoMetaFilter(TipoMetaFilter filter) {
            super(filter);
        }

        @Override
        public TipoMetaFilter copy() {
            return new TipoMetaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TipoMetaFilter tipoMeta;

    private IntegerFilter valorMeta;

    private InstantFilter dataInicio;

    private InstantFilter dataFim;

    private LongFilter internalUserId;

    private Boolean distinct;

    public MetasSaudeCriteria() {}

    public MetasSaudeCriteria(MetasSaudeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tipoMeta = other.optionalTipoMeta().map(TipoMetaFilter::copy).orElse(null);
        this.valorMeta = other.optionalValorMeta().map(IntegerFilter::copy).orElse(null);
        this.dataInicio = other.optionalDataInicio().map(InstantFilter::copy).orElse(null);
        this.dataFim = other.optionalDataFim().map(InstantFilter::copy).orElse(null);
        this.internalUserId = other.optionalInternalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MetasSaudeCriteria copy() {
        return new MetasSaudeCriteria(this);
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

    public TipoMetaFilter getTipoMeta() {
        return tipoMeta;
    }

    public Optional<TipoMetaFilter> optionalTipoMeta() {
        return Optional.ofNullable(tipoMeta);
    }

    public TipoMetaFilter tipoMeta() {
        if (tipoMeta == null) {
            setTipoMeta(new TipoMetaFilter());
        }
        return tipoMeta;
    }

    public void setTipoMeta(TipoMetaFilter tipoMeta) {
        this.tipoMeta = tipoMeta;
    }

    public IntegerFilter getValorMeta() {
        return valorMeta;
    }

    public Optional<IntegerFilter> optionalValorMeta() {
        return Optional.ofNullable(valorMeta);
    }

    public IntegerFilter valorMeta() {
        if (valorMeta == null) {
            setValorMeta(new IntegerFilter());
        }
        return valorMeta;
    }

    public void setValorMeta(IntegerFilter valorMeta) {
        this.valorMeta = valorMeta;
    }

    public InstantFilter getDataInicio() {
        return dataInicio;
    }

    public Optional<InstantFilter> optionalDataInicio() {
        return Optional.ofNullable(dataInicio);
    }

    public InstantFilter dataInicio() {
        if (dataInicio == null) {
            setDataInicio(new InstantFilter());
        }
        return dataInicio;
    }

    public void setDataInicio(InstantFilter dataInicio) {
        this.dataInicio = dataInicio;
    }

    public InstantFilter getDataFim() {
        return dataFim;
    }

    public Optional<InstantFilter> optionalDataFim() {
        return Optional.ofNullable(dataFim);
    }

    public InstantFilter dataFim() {
        if (dataFim == null) {
            setDataFim(new InstantFilter());
        }
        return dataFim;
    }

    public void setDataFim(InstantFilter dataFim) {
        this.dataFim = dataFim;
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
        final MetasSaudeCriteria that = (MetasSaudeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipoMeta, that.tipoMeta) &&
            Objects.equals(valorMeta, that.valorMeta) &&
            Objects.equals(dataInicio, that.dataInicio) &&
            Objects.equals(dataFim, that.dataFim) &&
            Objects.equals(internalUserId, that.internalUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipoMeta, valorMeta, dataInicio, dataFim, internalUserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MetasSaudeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTipoMeta().map(f -> "tipoMeta=" + f + ", ").orElse("") +
            optionalValorMeta().map(f -> "valorMeta=" + f + ", ").orElse("") +
            optionalDataInicio().map(f -> "dataInicio=" + f + ", ").orElse("") +
            optionalDataFim().map(f -> "dataFim=" + f + ", ").orElse("") +
            optionalInternalUserId().map(f -> "internalUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
