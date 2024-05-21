package com.healthscore.app.domain;

import com.healthscore.app.domain.enumeration.TipoMeta;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MetasSaude.
 */
@Entity
@Table(name = "metas_saude")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MetasSaude implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_meta")
    private TipoMeta tipoMeta;

    @Column(name = "valor_meta")
    private Integer valorMeta;

    @Column(name = "data_inicio")
    private Instant dataInicio;

    @Column(name = "data_fim")
    private Instant dataFim;

    @ManyToOne(fetch = FetchType.LAZY)
    private User internalUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MetasSaude id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoMeta getTipoMeta() {
        return this.tipoMeta;
    }

    public MetasSaude tipoMeta(TipoMeta tipoMeta) {
        this.setTipoMeta(tipoMeta);
        return this;
    }

    public void setTipoMeta(TipoMeta tipoMeta) {
        this.tipoMeta = tipoMeta;
    }

    public Integer getValorMeta() {
        return this.valorMeta;
    }

    public MetasSaude valorMeta(Integer valorMeta) {
        this.setValorMeta(valorMeta);
        return this;
    }

    public void setValorMeta(Integer valorMeta) {
        this.valorMeta = valorMeta;
    }

    public Instant getDataInicio() {
        return this.dataInicio;
    }

    public MetasSaude dataInicio(Instant dataInicio) {
        this.setDataInicio(dataInicio);
        return this;
    }

    public void setDataInicio(Instant dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Instant getDataFim() {
        return this.dataFim;
    }

    public MetasSaude dataFim(Instant dataFim) {
        this.setDataFim(dataFim);
        return this;
    }

    public void setDataFim(Instant dataFim) {
        this.dataFim = dataFim;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public MetasSaude internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MetasSaude)) {
            return false;
        }
        return getId() != null && getId().equals(((MetasSaude) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MetasSaude{" +
            "id=" + getId() +
            ", tipoMeta='" + getTipoMeta() + "'" +
            ", valorMeta=" + getValorMeta() +
            ", dataInicio='" + getDataInicio() + "'" +
            ", dataFim='" + getDataFim() + "'" +
            "}";
    }
}
