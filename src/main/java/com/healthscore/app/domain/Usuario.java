package com.healthscore.app.domain;

import com.healthscore.app.domain.enumeration.Genero;
import com.healthscore.app.domain.enumeration.TipoPlano;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "plano")
    private TipoPlano plano;

    @Column(name = "data_registro")
    private Instant dataRegistro;

    @Column(name = "telefone")
    private Integer telefone;

    @Column(name = "email")
    private String email;

    @Column(name = "data_nascimento")
    private Instant dataNascimento;

    @Column(name = "meta_consumo_agua")
    private Integer metaConsumoAgua;

    @Column(name = "meta_sono")
    private Double metaSono;

    @Column(name = "meta_calorias_consumidas")
    private Double metaCaloriasConsumidas;

    @Column(name = "meta_calorias_queimadas")
    private Double metaCaloriasQueimadas;

    @Column(name = "pontos_user")
    private Integer pontosUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero")
    private Genero genero;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User internalUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Usuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoPlano getPlano() {
        return this.plano;
    }

    public Usuario plano(TipoPlano plano) {
        this.setPlano(plano);
        return this;
    }

    public void setPlano(TipoPlano plano) {
        this.plano = plano;
    }

    public Instant getDataRegistro() {
        return this.dataRegistro;
    }

    public Usuario dataRegistro(Instant dataRegistro) {
        this.setDataRegistro(dataRegistro);
        return this;
    }

    public void setDataRegistro(Instant dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Integer getTelefone() {
        return this.telefone;
    }

    public Usuario telefone(Integer telefone) {
        this.setTelefone(telefone);
        return this;
    }

    public void setTelefone(Integer telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return this.email;
    }

    public Usuario email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getDataNascimento() {
        return this.dataNascimento;
    }

    public Usuario dataNascimento(Instant dataNascimento) {
        this.setDataNascimento(dataNascimento);
        return this;
    }

    public void setDataNascimento(Instant dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Integer getMetaConsumoAgua() {
        return this.metaConsumoAgua;
    }

    public Usuario metaConsumoAgua(Integer metaConsumoAgua) {
        this.setMetaConsumoAgua(metaConsumoAgua);
        return this;
    }

    public void setMetaConsumoAgua(Integer metaConsumoAgua) {
        this.metaConsumoAgua = metaConsumoAgua;
    }

    public Double getMetaSono() {
        return this.metaSono;
    }

    public Usuario metaSono(Double metaSono) {
        this.setMetaSono(metaSono);
        return this;
    }

    public void setMetaSono(Double metaSono) {
        this.metaSono = metaSono;
    }

    public Double getMetaCaloriasConsumidas() {
        return this.metaCaloriasConsumidas;
    }

    public Usuario metaCaloriasConsumidas(Double metaCaloriasConsumidas) {
        this.setMetaCaloriasConsumidas(metaCaloriasConsumidas);
        return this;
    }

    public void setMetaCaloriasConsumidas(Double metaCaloriasConsumidas) {
        this.metaCaloriasConsumidas = metaCaloriasConsumidas;
    }

    public Double getMetaCaloriasQueimadas() {
        return this.metaCaloriasQueimadas;
    }

    public Usuario metaCaloriasQueimadas(Double metaCaloriasQueimadas) {
        this.setMetaCaloriasQueimadas(metaCaloriasQueimadas);
        return this;
    }

    public void setMetaCaloriasQueimadas(Double metaCaloriasQueimadas) {
        this.metaCaloriasQueimadas = metaCaloriasQueimadas;
    }

    public Integer getPontosUser() {
        return this.pontosUser;
    }

    public Usuario pontosUser(Integer pontosUser) {
        this.setPontosUser(pontosUser);
        return this;
    }

    public void setPontosUser(Integer pontosUser) {
        this.pontosUser = pontosUser;
    }

    public Genero getGenero() {
        return this.genero;
    }

    public Usuario genero(Genero genero) {
        this.setGenero(genero);
        return this;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public Usuario internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        return getId() != null && getId().equals(((Usuario) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Usuario{" +
            "id=" + getId() +
            ", plano='" + getPlano() + "'" +
            ", dataRegistro='" + getDataRegistro() + "'" +
            ", telefone=" + getTelefone() +
            ", email='" + getEmail() + "'" +
            ", dataNascimento='" + getDataNascimento() + "'" +
            ", metaConsumoAgua=" + getMetaConsumoAgua() +
            ", metaSono=" + getMetaSono() +
            ", metaCaloriasConsumidas=" + getMetaCaloriasConsumidas() +
            ", metaCaloriasQueimadas=" + getMetaCaloriasQueimadas() +
            ", pontosUser=" + getPontosUser() +
            ", genero='" + getGenero() + "'" +
            "}";
    }
}
