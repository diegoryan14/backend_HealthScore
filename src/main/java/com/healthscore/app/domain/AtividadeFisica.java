package com.healthscore.app.domain;

import com.healthscore.app.domain.enumeration.TipoAtividade;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AtividadeFisica.
 */
@Entity
@Table(name = "atividade_fisica")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AtividadeFisica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_atividade")
    private TipoAtividade tipoAtividade;

    @Column(name = "data_horario")
    private Instant dataHorario;

    @Column(name = "duracao")
    private Integer duracao;

    @Column(name = "passos_calorias")
    private Integer passosCalorias;

    @ManyToOne(fetch = FetchType.LAZY)
    private User internalUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AtividadeFisica id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoAtividade getTipoAtividade() {
        return this.tipoAtividade;
    }

    public AtividadeFisica tipoAtividade(TipoAtividade tipoAtividade) {
        this.setTipoAtividade(tipoAtividade);
        return this;
    }

    public void setTipoAtividade(TipoAtividade tipoAtividade) {
        this.tipoAtividade = tipoAtividade;
    }

    public Instant getDataHorario() {
        return this.dataHorario;
    }

    public AtividadeFisica dataHorario(Instant dataHorario) {
        this.setDataHorario(dataHorario);
        return this;
    }

    public void setDataHorario(Instant dataHorario) {
        this.dataHorario = dataHorario;
    }

    public Integer getDuracao() {
        return this.duracao;
    }

    public AtividadeFisica duracao(Integer duracao) {
        this.setDuracao(duracao);
        return this;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public Integer getPassosCalorias() {
        return this.passosCalorias;
    }

    public AtividadeFisica passosCalorias(Integer passosCalorias) {
        this.setPassosCalorias(passosCalorias);
        return this;
    }

    public void setPassosCalorias(Integer passosCalorias) {
        this.passosCalorias = passosCalorias;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public AtividadeFisica internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AtividadeFisica)) {
            return false;
        }
        return getId() != null && getId().equals(((AtividadeFisica) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AtividadeFisica{" +
            "id=" + getId() +
            ", tipoAtividade='" + getTipoAtividade() + "'" +
            ", dataHorario='" + getDataHorario() + "'" +
            ", duracao=" + getDuracao() +
            ", passosCalorias=" + getPassosCalorias() +
            "}";
    }
}
