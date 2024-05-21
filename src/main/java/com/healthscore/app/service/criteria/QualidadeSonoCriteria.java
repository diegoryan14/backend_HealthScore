package com.healthscore.app.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.healthscore.app.domain.QualidadeSono} entity. This class is used
 * in {@link com.healthscore.app.web.rest.QualidadeSonoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /qualidade-sonos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QualidadeSonoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter data;

    private IntegerFilter horasSono;

    private LongFilter internalUserId;

    private Boolean distinct;

    public QualidadeSonoCriteria() {}

    public QualidadeSonoCriteria(QualidadeSonoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.data = other.optionalData().map(InstantFilter::copy).orElse(null);
        this.horasSono = other.optionalHorasSono().map(IntegerFilter::copy).orElse(null);
        this.internalUserId = other.optionalInternalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public QualidadeSonoCriteria copy() {
        return new QualidadeSonoCriteria(this);
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

    public InstantFilter getData() {
        return data;
    }

    public Optional<InstantFilter> optionalData() {
        return Optional.ofNullable(data);
    }

    public InstantFilter data() {
        if (data == null) {
            setData(new InstantFilter());
        }
        return data;
    }

    public void setData(InstantFilter data) {
        this.data = data;
    }

    public IntegerFilter getHorasSono() {
        return horasSono;
    }

    public Optional<IntegerFilter> optionalHorasSono() {
        return Optional.ofNullable(horasSono);
    }

    public IntegerFilter horasSono() {
        if (horasSono == null) {
            setHorasSono(new IntegerFilter());
        }
        return horasSono;
    }

    public void setHorasSono(IntegerFilter horasSono) {
        this.horasSono = horasSono;
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
        final QualidadeSonoCriteria that = (QualidadeSonoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(data, that.data) &&
            Objects.equals(horasSono, that.horasSono) &&
            Objects.equals(internalUserId, that.internalUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, data, horasSono, internalUserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QualidadeSonoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalData().map(f -> "data=" + f + ", ").orElse("") +
            optionalHorasSono().map(f -> "horasSono=" + f + ", ").orElse("") +
            optionalInternalUserId().map(f -> "internalUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
