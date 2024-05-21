package com.healthscore.app.service.criteria;

import com.healthscore.app.domain.enumeration.Especializacao;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.healthscore.app.domain.Especialista} entity. This class is used
 * in {@link com.healthscore.app.web.rest.EspecialistaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /especialistas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EspecialistaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Especializacao
     */
    public static class EspecializacaoFilter extends Filter<Especializacao> {

        public EspecializacaoFilter() {}

        public EspecializacaoFilter(EspecializacaoFilter filter) {
            super(filter);
        }

        @Override
        public EspecializacaoFilter copy() {
            return new EspecializacaoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter cpf;

    private EspecializacaoFilter especializacao;

    private InstantFilter dataFormacao;

    private IntegerFilter telefone;

    private StringFilter email;

    private InstantFilter dataNascimento;

    private LongFilter consultasId;

    private Boolean distinct;

    public EspecialistaCriteria() {}

    public EspecialistaCriteria(EspecialistaCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nome = other.optionalNome().map(StringFilter::copy).orElse(null);
        this.cpf = other.optionalCpf().map(StringFilter::copy).orElse(null);
        this.especializacao = other.optionalEspecializacao().map(EspecializacaoFilter::copy).orElse(null);
        this.dataFormacao = other.optionalDataFormacao().map(InstantFilter::copy).orElse(null);
        this.telefone = other.optionalTelefone().map(IntegerFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.dataNascimento = other.optionalDataNascimento().map(InstantFilter::copy).orElse(null);
        this.consultasId = other.optionalConsultasId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EspecialistaCriteria copy() {
        return new EspecialistaCriteria(this);
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

    public StringFilter getNome() {
        return nome;
    }

    public Optional<StringFilter> optionalNome() {
        return Optional.ofNullable(nome);
    }

    public StringFilter nome() {
        if (nome == null) {
            setNome(new StringFilter());
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getCpf() {
        return cpf;
    }

    public Optional<StringFilter> optionalCpf() {
        return Optional.ofNullable(cpf);
    }

    public StringFilter cpf() {
        if (cpf == null) {
            setCpf(new StringFilter());
        }
        return cpf;
    }

    public void setCpf(StringFilter cpf) {
        this.cpf = cpf;
    }

    public EspecializacaoFilter getEspecializacao() {
        return especializacao;
    }

    public Optional<EspecializacaoFilter> optionalEspecializacao() {
        return Optional.ofNullable(especializacao);
    }

    public EspecializacaoFilter especializacao() {
        if (especializacao == null) {
            setEspecializacao(new EspecializacaoFilter());
        }
        return especializacao;
    }

    public void setEspecializacao(EspecializacaoFilter especializacao) {
        this.especializacao = especializacao;
    }

    public InstantFilter getDataFormacao() {
        return dataFormacao;
    }

    public Optional<InstantFilter> optionalDataFormacao() {
        return Optional.ofNullable(dataFormacao);
    }

    public InstantFilter dataFormacao() {
        if (dataFormacao == null) {
            setDataFormacao(new InstantFilter());
        }
        return dataFormacao;
    }

    public void setDataFormacao(InstantFilter dataFormacao) {
        this.dataFormacao = dataFormacao;
    }

    public IntegerFilter getTelefone() {
        return telefone;
    }

    public Optional<IntegerFilter> optionalTelefone() {
        return Optional.ofNullable(telefone);
    }

    public IntegerFilter telefone() {
        if (telefone == null) {
            setTelefone(new IntegerFilter());
        }
        return telefone;
    }

    public void setTelefone(IntegerFilter telefone) {
        this.telefone = telefone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public InstantFilter getDataNascimento() {
        return dataNascimento;
    }

    public Optional<InstantFilter> optionalDataNascimento() {
        return Optional.ofNullable(dataNascimento);
    }

    public InstantFilter dataNascimento() {
        if (dataNascimento == null) {
            setDataNascimento(new InstantFilter());
        }
        return dataNascimento;
    }

    public void setDataNascimento(InstantFilter dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public LongFilter getConsultasId() {
        return consultasId;
    }

    public Optional<LongFilter> optionalConsultasId() {
        return Optional.ofNullable(consultasId);
    }

    public LongFilter consultasId() {
        if (consultasId == null) {
            setConsultasId(new LongFilter());
        }
        return consultasId;
    }

    public void setConsultasId(LongFilter consultasId) {
        this.consultasId = consultasId;
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
        final EspecialistaCriteria that = (EspecialistaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(cpf, that.cpf) &&
            Objects.equals(especializacao, that.especializacao) &&
            Objects.equals(dataFormacao, that.dataFormacao) &&
            Objects.equals(telefone, that.telefone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(dataNascimento, that.dataNascimento) &&
            Objects.equals(consultasId, that.consultasId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, cpf, especializacao, dataFormacao, telefone, email, dataNascimento, consultasId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EspecialistaCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNome().map(f -> "nome=" + f + ", ").orElse("") +
            optionalCpf().map(f -> "cpf=" + f + ", ").orElse("") +
            optionalEspecializacao().map(f -> "especializacao=" + f + ", ").orElse("") +
            optionalDataFormacao().map(f -> "dataFormacao=" + f + ", ").orElse("") +
            optionalTelefone().map(f -> "telefone=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalDataNascimento().map(f -> "dataNascimento=" + f + ", ").orElse("") +
            optionalConsultasId().map(f -> "consultasId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
