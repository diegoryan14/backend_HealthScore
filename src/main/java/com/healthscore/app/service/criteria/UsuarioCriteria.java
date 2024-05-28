package com.healthscore.app.service.criteria;

import com.healthscore.app.domain.enumeration.Genero;
import com.healthscore.app.domain.enumeration.TipoPlano;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.healthscore.app.domain.Usuario} entity. This class is used
 * in {@link com.healthscore.app.web.rest.UsuarioResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /usuarios?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UsuarioCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoPlano
     */
    public static class TipoPlanoFilter extends Filter<TipoPlano> {

        public TipoPlanoFilter() {}

        public TipoPlanoFilter(TipoPlanoFilter filter) {
            super(filter);
        }

        @Override
        public TipoPlanoFilter copy() {
            return new TipoPlanoFilter(this);
        }
    }

    /**
     * Class for filtering Genero
     */
    public static class GeneroFilter extends Filter<Genero> {

        public GeneroFilter() {}

        public GeneroFilter(GeneroFilter filter) {
            super(filter);
        }

        @Override
        public GeneroFilter copy() {
            return new GeneroFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TipoPlanoFilter plano;

    private InstantFilter dataRegistro;

    private IntegerFilter telefone;

    private StringFilter email;

    private InstantFilter dataNascimento;

    private IntegerFilter metaConsumoAgua;

    private DoubleFilter metaSono;

    private DoubleFilter metaCaloriasConsumidas;

    private DoubleFilter metaCaloriasQueimadas;

    private IntegerFilter pontosUser;

    private GeneroFilter genero;

    private LongFilter internalUserId;

    private Boolean distinct;

    public UsuarioCriteria() {}

    public UsuarioCriteria(UsuarioCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.plano = other.optionalPlano().map(TipoPlanoFilter::copy).orElse(null);
        this.dataRegistro = other.optionalDataRegistro().map(InstantFilter::copy).orElse(null);
        this.telefone = other.optionalTelefone().map(IntegerFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.dataNascimento = other.optionalDataNascimento().map(InstantFilter::copy).orElse(null);
        this.metaConsumoAgua = other.optionalMetaConsumoAgua().map(IntegerFilter::copy).orElse(null);
        this.metaSono = other.optionalMetaSono().map(DoubleFilter::copy).orElse(null);
        this.metaCaloriasConsumidas = other.optionalMetaCaloriasConsumidas().map(DoubleFilter::copy).orElse(null);
        this.metaCaloriasQueimadas = other.optionalMetaCaloriasQueimadas().map(DoubleFilter::copy).orElse(null);
        this.pontosUser = other.optionalPontosUser().map(IntegerFilter::copy).orElse(null);
        this.genero = other.optionalGenero().map(GeneroFilter::copy).orElse(null);
        this.internalUserId = other.optionalInternalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public UsuarioCriteria copy() {
        return new UsuarioCriteria(this);
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

    public TipoPlanoFilter getPlano() {
        return plano;
    }

    public Optional<TipoPlanoFilter> optionalPlano() {
        return Optional.ofNullable(plano);
    }

    public TipoPlanoFilter plano() {
        if (plano == null) {
            setPlano(new TipoPlanoFilter());
        }
        return plano;
    }

    public void setPlano(TipoPlanoFilter plano) {
        this.plano = plano;
    }

    public InstantFilter getDataRegistro() {
        return dataRegistro;
    }

    public Optional<InstantFilter> optionalDataRegistro() {
        return Optional.ofNullable(dataRegistro);
    }

    public InstantFilter dataRegistro() {
        if (dataRegistro == null) {
            setDataRegistro(new InstantFilter());
        }
        return dataRegistro;
    }

    public void setDataRegistro(InstantFilter dataRegistro) {
        this.dataRegistro = dataRegistro;
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

    public IntegerFilter getMetaConsumoAgua() {
        return metaConsumoAgua;
    }

    public Optional<IntegerFilter> optionalMetaConsumoAgua() {
        return Optional.ofNullable(metaConsumoAgua);
    }

    public IntegerFilter metaConsumoAgua() {
        if (metaConsumoAgua == null) {
            setMetaConsumoAgua(new IntegerFilter());
        }
        return metaConsumoAgua;
    }

    public void setMetaConsumoAgua(IntegerFilter metaConsumoAgua) {
        this.metaConsumoAgua = metaConsumoAgua;
    }

    public DoubleFilter getMetaSono() {
        return metaSono;
    }

    public Optional<DoubleFilter> optionalMetaSono() {
        return Optional.ofNullable(metaSono);
    }

    public DoubleFilter metaSono() {
        if (metaSono == null) {
            setMetaSono(new DoubleFilter());
        }
        return metaSono;
    }

    public void setMetaSono(DoubleFilter metaSono) {
        this.metaSono = metaSono;
    }

    public DoubleFilter getMetaCaloriasConsumidas() {
        return metaCaloriasConsumidas;
    }

    public Optional<DoubleFilter> optionalMetaCaloriasConsumidas() {
        return Optional.ofNullable(metaCaloriasConsumidas);
    }

    public DoubleFilter metaCaloriasConsumidas() {
        if (metaCaloriasConsumidas == null) {
            setMetaCaloriasConsumidas(new DoubleFilter());
        }
        return metaCaloriasConsumidas;
    }

    public void setMetaCaloriasConsumidas(DoubleFilter metaCaloriasConsumidas) {
        this.metaCaloriasConsumidas = metaCaloriasConsumidas;
    }

    public DoubleFilter getMetaCaloriasQueimadas() {
        return metaCaloriasQueimadas;
    }

    public Optional<DoubleFilter> optionalMetaCaloriasQueimadas() {
        return Optional.ofNullable(metaCaloriasQueimadas);
    }

    public DoubleFilter metaCaloriasQueimadas() {
        if (metaCaloriasQueimadas == null) {
            setMetaCaloriasQueimadas(new DoubleFilter());
        }
        return metaCaloriasQueimadas;
    }

    public void setMetaCaloriasQueimadas(DoubleFilter metaCaloriasQueimadas) {
        this.metaCaloriasQueimadas = metaCaloriasQueimadas;
    }

    public IntegerFilter getPontosUser() {
        return pontosUser;
    }

    public Optional<IntegerFilter> optionalPontosUser() {
        return Optional.ofNullable(pontosUser);
    }

    public IntegerFilter pontosUser() {
        if (pontosUser == null) {
            setPontosUser(new IntegerFilter());
        }
        return pontosUser;
    }

    public void setPontosUser(IntegerFilter pontosUser) {
        this.pontosUser = pontosUser;
    }

    public GeneroFilter getGenero() {
        return genero;
    }

    public Optional<GeneroFilter> optionalGenero() {
        return Optional.ofNullable(genero);
    }

    public GeneroFilter genero() {
        if (genero == null) {
            setGenero(new GeneroFilter());
        }
        return genero;
    }

    public void setGenero(GeneroFilter genero) {
        this.genero = genero;
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
        final UsuarioCriteria that = (UsuarioCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(plano, that.plano) &&
            Objects.equals(dataRegistro, that.dataRegistro) &&
            Objects.equals(telefone, that.telefone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(dataNascimento, that.dataNascimento) &&
            Objects.equals(metaConsumoAgua, that.metaConsumoAgua) &&
            Objects.equals(metaSono, that.metaSono) &&
            Objects.equals(metaCaloriasConsumidas, that.metaCaloriasConsumidas) &&
            Objects.equals(metaCaloriasQueimadas, that.metaCaloriasQueimadas) &&
            Objects.equals(pontosUser, that.pontosUser) &&
            Objects.equals(genero, that.genero) &&
            Objects.equals(internalUserId, that.internalUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            plano,
            dataRegistro,
            telefone,
            email,
            dataNascimento,
            metaConsumoAgua,
            metaSono,
            metaCaloriasConsumidas,
            metaCaloriasQueimadas,
            pontosUser,
            genero,
            internalUserId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPlano().map(f -> "plano=" + f + ", ").orElse("") +
            optionalDataRegistro().map(f -> "dataRegistro=" + f + ", ").orElse("") +
            optionalTelefone().map(f -> "telefone=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalDataNascimento().map(f -> "dataNascimento=" + f + ", ").orElse("") +
            optionalMetaConsumoAgua().map(f -> "metaConsumoAgua=" + f + ", ").orElse("") +
            optionalMetaSono().map(f -> "metaSono=" + f + ", ").orElse("") +
            optionalMetaCaloriasConsumidas().map(f -> "metaCaloriasConsumidas=" + f + ", ").orElse("") +
            optionalMetaCaloriasQueimadas().map(f -> "metaCaloriasQueimadas=" + f + ", ").orElse("") +
            optionalPontosUser().map(f -> "pontosUser=" + f + ", ").orElse("") +
            optionalGenero().map(f -> "genero=" + f + ", ").orElse("") +
            optionalInternalUserId().map(f -> "internalUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
