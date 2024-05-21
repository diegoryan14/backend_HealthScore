package com.healthscore.app.service.criteria;

import com.healthscore.app.domain.enumeration.TipoAtividade;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.healthscore.app.domain.AtividadeFisica} entity. This class is used
 * in {@link com.healthscore.app.web.rest.AtividadeFisicaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /atividade-fisicas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AtividadeFisicaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoAtividade
     */
    public static class TipoAtividadeFilter extends Filter<TipoAtividade> {

        public TipoAtividadeFilter() {}

        public TipoAtividadeFilter(TipoAtividadeFilter filter) {
            super(filter);
        }

        @Override
        public TipoAtividadeFilter copy() {
            return new TipoAtividadeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TipoAtividadeFilter tipoAtividade;

    private InstantFilter dataHorario;

    private IntegerFilter duracao;

    private IntegerFilter passosCalorias;

    private LongFilter internalUserId;

    private Boolean distinct;

    public AtividadeFisicaCriteria() {}

    public AtividadeFisicaCriteria(AtividadeFisicaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.tipoAtividade = other.optionalTipoAtividade().map(TipoAtividadeFilter::copy).orElse(null);
        this.dataHorario = other.optionalDataHorario().map(InstantFilter::copy).orElse(null);
        this.duracao = other.optionalDuracao().map(IntegerFilter::copy).orElse(null);
        this.passosCalorias = other.optionalPassosCalorias().map(IntegerFilter::copy).orElse(null);
        this.internalUserId = other.optionalInternalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AtividadeFisicaCriteria copy() {
        return new AtividadeFisicaCriteria(this);
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

    public TipoAtividadeFilter getTipoAtividade() {
        return tipoAtividade;
    }

    public Optional<TipoAtividadeFilter> optionalTipoAtividade() {
        return Optional.ofNullable(tipoAtividade);
    }

    public TipoAtividadeFilter tipoAtividade() {
        if (tipoAtividade == null) {
            setTipoAtividade(new TipoAtividadeFilter());
        }
        return tipoAtividade;
    }

    public void setTipoAtividade(TipoAtividadeFilter tipoAtividade) {
        this.tipoAtividade = tipoAtividade;
    }

    public InstantFilter getDataHorario() {
        return dataHorario;
    }

    public Optional<InstantFilter> optionalDataHorario() {
        return Optional.ofNullable(dataHorario);
    }

    public InstantFilter dataHorario() {
        if (dataHorario == null) {
            setDataHorario(new InstantFilter());
        }
        return dataHorario;
    }

    public void setDataHorario(InstantFilter dataHorario) {
        this.dataHorario = dataHorario;
    }

    public IntegerFilter getDuracao() {
        return duracao;
    }

    public Optional<IntegerFilter> optionalDuracao() {
        return Optional.ofNullable(duracao);
    }

    public IntegerFilter duracao() {
        if (duracao == null) {
            setDuracao(new IntegerFilter());
        }
        return duracao;
    }

    public void setDuracao(IntegerFilter duracao) {
        this.duracao = duracao;
    }

    public IntegerFilter getPassosCalorias() {
        return passosCalorias;
    }

    public Optional<IntegerFilter> optionalPassosCalorias() {
        return Optional.ofNullable(passosCalorias);
    }

    public IntegerFilter passosCalorias() {
        if (passosCalorias == null) {
            setPassosCalorias(new IntegerFilter());
        }
        return passosCalorias;
    }

    public void setPassosCalorias(IntegerFilter passosCalorias) {
        this.passosCalorias = passosCalorias;
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
        final AtividadeFisicaCriteria that = (AtividadeFisicaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tipoAtividade, that.tipoAtividade) &&
            Objects.equals(dataHorario, that.dataHorario) &&
            Objects.equals(duracao, that.duracao) &&
            Objects.equals(passosCalorias, that.passosCalorias) &&
            Objects.equals(internalUserId, that.internalUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipoAtividade, dataHorario, duracao, passosCalorias, internalUserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AtividadeFisicaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTipoAtividade().map(f -> "tipoAtividade=" + f + ", ").orElse("") +
            optionalDataHorario().map(f -> "dataHorario=" + f + ", ").orElse("") +
            optionalDuracao().map(f -> "duracao=" + f + ", ").orElse("") +
            optionalPassosCalorias().map(f -> "passosCalorias=" + f + ", ").orElse("") +
            optionalInternalUserId().map(f -> "internalUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
