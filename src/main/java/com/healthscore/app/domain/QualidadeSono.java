package com.healthscore.app.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A QualidadeSono.
 */
@Entity
@Table(name = "qualidade_sono")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QualidadeSono implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "data")
    private Instant data;

    @Column(name = "horas_sono")
    private Integer horasSono;

    @ManyToOne(fetch = FetchType.LAZY)
    private User internalUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QualidadeSono id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getData() {
        return this.data;
    }

    public QualidadeSono data(Instant data) {
        this.setData(data);
        return this;
    }

    public void setData(Instant data) {
        this.data = data;
    }

    public Integer getHorasSono() {
        return this.horasSono;
    }

    public QualidadeSono horasSono(Integer horasSono) {
        this.setHorasSono(horasSono);
        return this;
    }

    public void setHorasSono(Integer horasSono) {
        this.horasSono = horasSono;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public QualidadeSono internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QualidadeSono)) {
            return false;
        }
        return getId() != null && getId().equals(((QualidadeSono) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QualidadeSono{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            ", horasSono=" + getHorasSono() +
            "}";
    }
}
