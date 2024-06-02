package com.healthscore.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.healthscore.app.domain.PontuacaoUsuario} entity. This class is used
 * in {@link com.healthscore.app.web.rest.PontuacaoUsuarioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pontuacao-usuarios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PontuacaoUsuarioCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dataAlteracao;

    private IntegerFilter valorAlteracao;

    private StringFilter motivo;

    private LongFilter usuarioId;

    private Boolean distinct;

    public PontuacaoUsuarioCriteria() {}

    public PontuacaoUsuarioCriteria(PontuacaoUsuarioCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.dataAlteracao = other.optionalDataAlteracao().map(InstantFilter::copy).orElse(null);
        this.valorAlteracao = other.optionalValorAlteracao().map(IntegerFilter::copy).orElse(null);
        this.motivo = other.optionalMotivo().map(StringFilter::copy).orElse(null);
        this.usuarioId = other.optionalUsuarioId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PontuacaoUsuarioCriteria copy() {
        return new PontuacaoUsuarioCriteria(this);
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

    public InstantFilter getDataAlteracao() {
        return dataAlteracao;
    }

    public Optional<InstantFilter> optionalDataAlteracao() {
        return Optional.ofNullable(dataAlteracao);
    }

    public InstantFilter dataAlteracao() {
        if (dataAlteracao == null) {
            setDataAlteracao(new InstantFilter());
        }
        return dataAlteracao;
    }

    public void setDataAlteracao(InstantFilter dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public IntegerFilter getValorAlteracao() {
        return valorAlteracao;
    }

    public Optional<IntegerFilter> optionalValorAlteracao() {
        return Optional.ofNullable(valorAlteracao);
    }

    public IntegerFilter valorAlteracao() {
        if (valorAlteracao == null) {
            setValorAlteracao(new IntegerFilter());
        }
        return valorAlteracao;
    }

    public void setValorAlteracao(IntegerFilter valorAlteracao) {
        this.valorAlteracao = valorAlteracao;
    }

    public StringFilter getMotivo() {
        return motivo;
    }

    public Optional<StringFilter> optionalMotivo() {
        return Optional.ofNullable(motivo);
    }

    public StringFilter motivo() {
        if (motivo == null) {
            setMotivo(new StringFilter());
        }
        return motivo;
    }

    public void setMotivo(StringFilter motivo) {
        this.motivo = motivo;
    }

    public LongFilter getUsuarioId() {
        return usuarioId;
    }

    public Optional<LongFilter> optionalUsuarioId() {
        return Optional.ofNullable(usuarioId);
    }

    public LongFilter usuarioId() {
        if (usuarioId == null) {
            setUsuarioId(new LongFilter());
        }
        return usuarioId;
    }

    public void setUsuarioId(LongFilter usuarioId) {
        this.usuarioId = usuarioId;
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
        final PontuacaoUsuarioCriteria that = (PontuacaoUsuarioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(dataAlteracao, that.dataAlteracao) &&
            Objects.equals(valorAlteracao, that.valorAlteracao) &&
            Objects.equals(motivo, that.motivo) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataAlteracao, valorAlteracao, motivo, usuarioId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PontuacaoUsuarioCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDataAlteracao().map(f -> "dataAlteracao=" + f + ", ").orElse("") +
            optionalValorAlteracao().map(f -> "valorAlteracao=" + f + ", ").orElse("") +
            optionalMotivo().map(f -> "motivo=" + f + ", ").orElse("") +
            optionalUsuarioId().map(f -> "usuarioId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
