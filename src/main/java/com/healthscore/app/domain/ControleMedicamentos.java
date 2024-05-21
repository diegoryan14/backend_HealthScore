package com.healthscore.app.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ControleMedicamentos.
 */
@Entity
@Table(name = "controle_medicamentos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ControleMedicamentos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_medicamento")
    private String nomeMedicamento;

    @Column(name = "dosagem")
    private String dosagem;

    @Column(name = "horario_ingestao")
    private Instant horarioIngestao;

    @ManyToOne(fetch = FetchType.LAZY)
    private User internalUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ControleMedicamentos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeMedicamento() {
        return this.nomeMedicamento;
    }

    public ControleMedicamentos nomeMedicamento(String nomeMedicamento) {
        this.setNomeMedicamento(nomeMedicamento);
        return this;
    }

    public void setNomeMedicamento(String nomeMedicamento) {
        this.nomeMedicamento = nomeMedicamento;
    }

    public String getDosagem() {
        return this.dosagem;
    }

    public ControleMedicamentos dosagem(String dosagem) {
        this.setDosagem(dosagem);
        return this;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }

    public Instant getHorarioIngestao() {
        return this.horarioIngestao;
    }

    public ControleMedicamentos horarioIngestao(Instant horarioIngestao) {
        this.setHorarioIngestao(horarioIngestao);
        return this;
    }

    public void setHorarioIngestao(Instant horarioIngestao) {
        this.horarioIngestao = horarioIngestao;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public ControleMedicamentos internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ControleMedicamentos)) {
            return false;
        }
        return getId() != null && getId().equals(((ControleMedicamentos) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ControleMedicamentos{" +
            "id=" + getId() +
            ", nomeMedicamento='" + getNomeMedicamento() + "'" +
            ", dosagem='" + getDosagem() + "'" +
            ", horarioIngestao='" + getHorarioIngestao() + "'" +
            "}";
    }
}
