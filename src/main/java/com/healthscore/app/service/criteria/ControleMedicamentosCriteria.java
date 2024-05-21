package com.healthscore.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.healthscore.app.domain.ControleMedicamentos} entity. This class is used
 * in {@link com.healthscore.app.web.rest.ControleMedicamentosResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /controle-medicamentos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ControleMedicamentosCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nomeMedicamento;

    private StringFilter dosagem;

    private InstantFilter horarioIngestao;

    private LongFilter internalUserId;

    private Boolean distinct;

    public ControleMedicamentosCriteria() {}

    public ControleMedicamentosCriteria(ControleMedicamentosCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nomeMedicamento = other.optionalNomeMedicamento().map(StringFilter::copy).orElse(null);
        this.dosagem = other.optionalDosagem().map(StringFilter::copy).orElse(null);
        this.horarioIngestao = other.optionalHorarioIngestao().map(InstantFilter::copy).orElse(null);
        this.internalUserId = other.optionalInternalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ControleMedicamentosCriteria copy() {
        return new ControleMedicamentosCriteria(this);
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

    public StringFilter getNomeMedicamento() {
        return nomeMedicamento;
    }

    public Optional<StringFilter> optionalNomeMedicamento() {
        return Optional.ofNullable(nomeMedicamento);
    }

    public StringFilter nomeMedicamento() {
        if (nomeMedicamento == null) {
            setNomeMedicamento(new StringFilter());
        }
        return nomeMedicamento;
    }

    public void setNomeMedicamento(StringFilter nomeMedicamento) {
        this.nomeMedicamento = nomeMedicamento;
    }

    public StringFilter getDosagem() {
        return dosagem;
    }

    public Optional<StringFilter> optionalDosagem() {
        return Optional.ofNullable(dosagem);
    }

    public StringFilter dosagem() {
        if (dosagem == null) {
            setDosagem(new StringFilter());
        }
        return dosagem;
    }

    public void setDosagem(StringFilter dosagem) {
        this.dosagem = dosagem;
    }

    public InstantFilter getHorarioIngestao() {
        return horarioIngestao;
    }

    public Optional<InstantFilter> optionalHorarioIngestao() {
        return Optional.ofNullable(horarioIngestao);
    }

    public InstantFilter horarioIngestao() {
        if (horarioIngestao == null) {
            setHorarioIngestao(new InstantFilter());
        }
        return horarioIngestao;
    }

    public void setHorarioIngestao(InstantFilter horarioIngestao) {
        this.horarioIngestao = horarioIngestao;
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
        final ControleMedicamentosCriteria that = (ControleMedicamentosCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nomeMedicamento, that.nomeMedicamento) &&
            Objects.equals(dosagem, that.dosagem) &&
            Objects.equals(horarioIngestao, that.horarioIngestao) &&
            Objects.equals(internalUserId, that.internalUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomeMedicamento, dosagem, horarioIngestao, internalUserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ControleMedicamentosCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNomeMedicamento().map(f -> "nomeMedicamento=" + f + ", ").orElse("") +
            optionalDosagem().map(f -> "dosagem=" + f + ", ").orElse("") +
            optionalHorarioIngestao().map(f -> "horarioIngestao=" + f + ", ").orElse("") +
            optionalInternalUserId().map(f -> "internalUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
