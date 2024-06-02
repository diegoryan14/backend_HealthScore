package com.healthscore.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PontuacaoUsuario.
 */
@Entity
@Table(name = "pontuacao_usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PontuacaoUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "data_alteracao")
    private Instant dataAlteracao;

    @Column(name = "valor_alteracao")
    private Integer valorAlteracao;

    @Column(name = "motivo")
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "internalUser" }, allowSetters = true)
    private Usuario usuario;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PontuacaoUsuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDataAlteracao() {
        return this.dataAlteracao;
    }

    public PontuacaoUsuario dataAlteracao(Instant dataAlteracao) {
        this.setDataAlteracao(dataAlteracao);
        return this;
    }

    public void setDataAlteracao(Instant dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public Integer getValorAlteracao() {
        return this.valorAlteracao;
    }

    public PontuacaoUsuario valorAlteracao(Integer valorAlteracao) {
        this.setValorAlteracao(valorAlteracao);
        return this;
    }

    public void setValorAlteracao(Integer valorAlteracao) {
        this.valorAlteracao = valorAlteracao;
    }

    public String getMotivo() {
        return this.motivo;
    }

    public PontuacaoUsuario motivo(String motivo) {
        this.setMotivo(motivo);
        return this;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public PontuacaoUsuario usuario(Usuario usuario) {
        this.setUsuario(usuario);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PontuacaoUsuario)) {
            return false;
        }
        return getId() != null && getId().equals(((PontuacaoUsuario) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PontuacaoUsuario{" +
            "id=" + getId() +
            ", dataAlteracao='" + getDataAlteracao() + "'" +
            ", valorAlteracao=" + getValorAlteracao() +
            ", motivo='" + getMotivo() + "'" +
            "}";
    }
}
