package com.healthscore.app.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Dieta.
 */
@Entity
@Table(name = "dieta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Dieta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "descricao_refeicao")
    private String descricaoRefeicao;

    @Column(name = "data_horario_refeicao")
    private Instant dataHorarioRefeicao;

    @Column(name = "calorias_consumidas")
    private Integer caloriasConsumidas;

    @ManyToOne(fetch = FetchType.LAZY)
    private User internalUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dieta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricaoRefeicao() {
        return this.descricaoRefeicao;
    }

    public Dieta descricaoRefeicao(String descricaoRefeicao) {
        this.setDescricaoRefeicao(descricaoRefeicao);
        return this;
    }

    public void setDescricaoRefeicao(String descricaoRefeicao) {
        this.descricaoRefeicao = descricaoRefeicao;
    }

    public Instant getDataHorarioRefeicao() {
        return this.dataHorarioRefeicao;
    }

    public Dieta dataHorarioRefeicao(Instant dataHorarioRefeicao) {
        this.setDataHorarioRefeicao(dataHorarioRefeicao);
        return this;
    }

    public void setDataHorarioRefeicao(Instant dataHorarioRefeicao) {
        this.dataHorarioRefeicao = dataHorarioRefeicao;
    }

    public Integer getCaloriasConsumidas() {
        return this.caloriasConsumidas;
    }

    public Dieta caloriasConsumidas(Integer caloriasConsumidas) {
        this.setCaloriasConsumidas(caloriasConsumidas);
        return this;
    }

    public void setCaloriasConsumidas(Integer caloriasConsumidas) {
        this.caloriasConsumidas = caloriasConsumidas;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public Dieta internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dieta)) {
            return false;
        }
        return getId() != null && getId().equals(((Dieta) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dieta{" +
            "id=" + getId() +
            ", descricaoRefeicao='" + getDescricaoRefeicao() + "'" +
            ", dataHorarioRefeicao='" + getDataHorarioRefeicao() + "'" +
            ", caloriasConsumidas=" + getCaloriasConsumidas() +
            "}";
    }
}
