package com.healthscore.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.healthscore.app.domain.enumeration.Especializacao;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Especialista.
 */
@Entity
@Table(name = "especialista")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Especialista implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cpf")
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(name = "especializacao")
    private Especializacao especializacao;

    @Column(name = "data_formacao")
    private Instant dataFormacao;

    @Column(name = "telefone")
    private Integer telefone;

    @Column(name = "email")
    private String email;

    @Column(name = "data_nascimento")
    private Instant dataNascimento;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "especialista")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "internalUser", "especialista" }, allowSetters = true)
    private Set<ConsultaEspecialista> consultas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Especialista id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Especialista nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return this.cpf;
    }

    public Especialista cpf(String cpf) {
        this.setCpf(cpf);
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Especializacao getEspecializacao() {
        return this.especializacao;
    }

    public Especialista especializacao(Especializacao especializacao) {
        this.setEspecializacao(especializacao);
        return this;
    }

    public void setEspecializacao(Especializacao especializacao) {
        this.especializacao = especializacao;
    }

    public Instant getDataFormacao() {
        return this.dataFormacao;
    }

    public Especialista dataFormacao(Instant dataFormacao) {
        this.setDataFormacao(dataFormacao);
        return this;
    }

    public void setDataFormacao(Instant dataFormacao) {
        this.dataFormacao = dataFormacao;
    }

    public Integer getTelefone() {
        return this.telefone;
    }

    public Especialista telefone(Integer telefone) {
        this.setTelefone(telefone);
        return this;
    }

    public void setTelefone(Integer telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return this.email;
    }

    public Especialista email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getDataNascimento() {
        return this.dataNascimento;
    }

    public Especialista dataNascimento(Instant dataNascimento) {
        this.setDataNascimento(dataNascimento);
        return this;
    }

    public void setDataNascimento(Instant dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Set<ConsultaEspecialista> getConsultas() {
        return this.consultas;
    }

    public void setConsultas(Set<ConsultaEspecialista> consultaEspecialistas) {
        if (this.consultas != null) {
            this.consultas.forEach(i -> i.setEspecialista(null));
        }
        if (consultaEspecialistas != null) {
            consultaEspecialistas.forEach(i -> i.setEspecialista(this));
        }
        this.consultas = consultaEspecialistas;
    }

    public Especialista consultas(Set<ConsultaEspecialista> consultaEspecialistas) {
        this.setConsultas(consultaEspecialistas);
        return this;
    }

    public Especialista addConsultas(ConsultaEspecialista consultaEspecialista) {
        this.consultas.add(consultaEspecialista);
        consultaEspecialista.setEspecialista(this);
        return this;
    }

    public Especialista removeConsultas(ConsultaEspecialista consultaEspecialista) {
        this.consultas.remove(consultaEspecialista);
        consultaEspecialista.setEspecialista(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Especialista)) {
            return false;
        }
        return getId() != null && getId().equals(((Especialista) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Especialista{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", especializacao='" + getEspecializacao() + "'" +
            ", dataFormacao='" + getDataFormacao() + "'" +
            ", telefone=" + getTelefone() +
            ", email='" + getEmail() + "'" +
            ", dataNascimento='" + getDataNascimento() + "'" +
            "}";
    }
}
