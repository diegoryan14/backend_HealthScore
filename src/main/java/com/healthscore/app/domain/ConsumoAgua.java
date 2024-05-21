package com.healthscore.app.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ConsumoAgua.
 */
@Entity
@Table(name = "consumo_agua")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConsumoAgua implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "data_consumo")
    private Instant dataConsumo;

    @Column(name = "quantidade_ml")
    private Integer quantidadeMl;

    @ManyToOne(fetch = FetchType.LAZY)
    private User internalUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ConsumoAgua id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataConsumo() {
        return this.dataConsumo;
    }

    public ConsumoAgua dataConsumo(Instant dataConsumo) {
        this.setDataConsumo(dataConsumo);
        return this;
    }

    public void setDataConsumo(Instant dataConsumo) {
        this.dataConsumo = dataConsumo;
    }

    public Integer getQuantidadeMl() {
        return this.quantidadeMl;
    }

    public ConsumoAgua quantidadeMl(Integer quantidadeMl) {
        this.setQuantidadeMl(quantidadeMl);
        return this;
    }

    public void setQuantidadeMl(Integer quantidadeMl) {
        this.quantidadeMl = quantidadeMl;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public ConsumoAgua internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsumoAgua)) {
            return false;
        }
        return getId() != null && getId().equals(((ConsumoAgua) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsumoAgua{" +
            "id=" + getId() +
            ", dataConsumo='" + getDataConsumo() + "'" +
            ", quantidadeMl=" + getQuantidadeMl() +
            "}";
    }
}
