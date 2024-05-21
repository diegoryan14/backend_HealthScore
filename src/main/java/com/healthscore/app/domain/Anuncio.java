package com.healthscore.app.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Anuncio.
 */
@Entity
@Table(name = "anuncio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Anuncio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "data_publicacao")
    private Instant dataPublicacao;

    @Column(name = "data_inicio")
    private Instant dataInicio;

    @Column(name = "data_fim")
    private Instant dataFim;

    @Column(name = "preco")
    private Double preco;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Anuncio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Anuncio titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public Anuncio descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Instant getDataPublicacao() {
        return this.dataPublicacao;
    }

    public Anuncio dataPublicacao(Instant dataPublicacao) {
        this.setDataPublicacao(dataPublicacao);
        return this;
    }

    public void setDataPublicacao(Instant dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public Instant getDataInicio() {
        return this.dataInicio;
    }

    public Anuncio dataInicio(Instant dataInicio) {
        this.setDataInicio(dataInicio);
        return this;
    }

    public void setDataInicio(Instant dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Instant getDataFim() {
        return this.dataFim;
    }

    public Anuncio dataFim(Instant dataFim) {
        this.setDataFim(dataFim);
        return this;
    }

    public void setDataFim(Instant dataFim) {
        this.dataFim = dataFim;
    }

    public Double getPreco() {
        return this.preco;
    }

    public Anuncio preco(Double preco) {
        this.setPreco(preco);
        return this;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Anuncio)) {
            return false;
        }
        return getId() != null && getId().equals(((Anuncio) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Anuncio{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", dataPublicacao='" + getDataPublicacao() + "'" +
            ", dataInicio='" + getDataInicio() + "'" +
            ", dataFim='" + getDataFim() + "'" +
            ", preco=" + getPreco() +
            "}";
    }
}
