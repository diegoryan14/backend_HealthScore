package com.healthscore.app.web.rest;

import static com.healthscore.app.domain.UsuarioAsserts.*;
import static com.healthscore.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthscore.app.IntegrationTest;
import com.healthscore.app.domain.User;
import com.healthscore.app.domain.Usuario;
import com.healthscore.app.domain.enumeration.Genero;
import com.healthscore.app.domain.enumeration.TipoPlano;
import com.healthscore.app.repository.UsuarioRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UsuarioResourceIT {

    private static final TipoPlano DEFAULT_PLANO = TipoPlano.GRATUITO;
    private static final TipoPlano UPDATED_PLANO = TipoPlano.PREMIUM;

    private static final Instant DEFAULT_DATA_REGISTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_REGISTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_TELEFONE = 1;
    private static final Integer UPDATED_TELEFONE = 2;
    private static final Integer SMALLER_TELEFONE = 1 - 1;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATA_NASCIMENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_NASCIMENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_META_CONSUMO_AGUA = 1;
    private static final Integer UPDATED_META_CONSUMO_AGUA = 2;
    private static final Integer SMALLER_META_CONSUMO_AGUA = 1 - 1;

    private static final Double DEFAULT_META_SONO = 1D;
    private static final Double UPDATED_META_SONO = 2D;
    private static final Double SMALLER_META_SONO = 1D - 1D;

    private static final Double DEFAULT_META_CALORIAS_CONSUMIDAS = 1D;
    private static final Double UPDATED_META_CALORIAS_CONSUMIDAS = 2D;
    private static final Double SMALLER_META_CALORIAS_CONSUMIDAS = 1D - 1D;

    private static final Double DEFAULT_META_CALORIAS_QUEIMADAS = 1D;
    private static final Double UPDATED_META_CALORIAS_QUEIMADAS = 2D;
    private static final Double SMALLER_META_CALORIAS_QUEIMADAS = 1D - 1D;

    private static final Integer DEFAULT_PONTOS_USER = 1;
    private static final Integer UPDATED_PONTOS_USER = 2;
    private static final Integer SMALLER_PONTOS_USER = 1 - 1;

    private static final Genero DEFAULT_GENERO = Genero.MASCULINO;
    private static final Genero UPDATED_GENERO = Genero.FEMININO;

    private static final String ENTITY_API_URL = "/api/usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUsuarioMockMvc;

    private Usuario usuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .plano(DEFAULT_PLANO)
            .dataRegistro(DEFAULT_DATA_REGISTRO)
            .telefone(DEFAULT_TELEFONE)
            .email(DEFAULT_EMAIL)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .metaConsumoAgua(DEFAULT_META_CONSUMO_AGUA)
            .metaSono(DEFAULT_META_SONO)
            .metaCaloriasConsumidas(DEFAULT_META_CALORIAS_CONSUMIDAS)
            .metaCaloriasQueimadas(DEFAULT_META_CALORIAS_QUEIMADAS)
            .pontosUser(DEFAULT_PONTOS_USER)
            .genero(DEFAULT_GENERO);
        return usuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Usuario createUpdatedEntity(EntityManager em) {
        Usuario usuario = new Usuario()
            .plano(UPDATED_PLANO)
            .dataRegistro(UPDATED_DATA_REGISTRO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .metaConsumoAgua(UPDATED_META_CONSUMO_AGUA)
            .metaSono(UPDATED_META_SONO)
            .metaCaloriasConsumidas(UPDATED_META_CALORIAS_CONSUMIDAS)
            .metaCaloriasQueimadas(UPDATED_META_CALORIAS_QUEIMADAS)
            .pontosUser(UPDATED_PONTOS_USER)
            .genero(UPDATED_GENERO);
        return usuario;
    }

    @BeforeEach
    public void initTest() {
        usuario = createEntity(em);
    }

    @Test
    @Transactional
    void createUsuario() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Usuario
        var returnedUsuario = om.readValue(
            restUsuarioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuario)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Usuario.class
        );

        // Validate the Usuario in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUsuarioUpdatableFieldsEquals(returnedUsuario, getPersistedUsuario(returnedUsuario));
    }

    @Test
    @Transactional
    void createUsuarioWithExistingId() throws Exception {
        // Create the Usuario with an existing ID
        usuario.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuario)))
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUsuarios() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].plano").value(hasItem(DEFAULT_PLANO.toString())))
            .andExpect(jsonPath("$.[*].dataRegistro").value(hasItem(DEFAULT_DATA_REGISTRO.toString())))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].metaConsumoAgua").value(hasItem(DEFAULT_META_CONSUMO_AGUA)))
            .andExpect(jsonPath("$.[*].metaSono").value(hasItem(DEFAULT_META_SONO.doubleValue())))
            .andExpect(jsonPath("$.[*].metaCaloriasConsumidas").value(hasItem(DEFAULT_META_CALORIAS_CONSUMIDAS.doubleValue())))
            .andExpect(jsonPath("$.[*].metaCaloriasQueimadas").value(hasItem(DEFAULT_META_CALORIAS_QUEIMADAS.doubleValue())))
            .andExpect(jsonPath("$.[*].pontosUser").value(hasItem(DEFAULT_PONTOS_USER)))
            .andExpect(jsonPath("$.[*].genero").value(hasItem(DEFAULT_GENERO.toString())));
    }

    @Test
    @Transactional
    void getUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get the usuario
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, usuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(usuario.getId().intValue()))
            .andExpect(jsonPath("$.plano").value(DEFAULT_PLANO.toString()))
            .andExpect(jsonPath("$.dataRegistro").value(DEFAULT_DATA_REGISTRO.toString()))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.metaConsumoAgua").value(DEFAULT_META_CONSUMO_AGUA))
            .andExpect(jsonPath("$.metaSono").value(DEFAULT_META_SONO.doubleValue()))
            .andExpect(jsonPath("$.metaCaloriasConsumidas").value(DEFAULT_META_CALORIAS_CONSUMIDAS.doubleValue()))
            .andExpect(jsonPath("$.metaCaloriasQueimadas").value(DEFAULT_META_CALORIAS_QUEIMADAS.doubleValue()))
            .andExpect(jsonPath("$.pontosUser").value(DEFAULT_PONTOS_USER))
            .andExpect(jsonPath("$.genero").value(DEFAULT_GENERO.toString()));
    }

    @Test
    @Transactional
    void getUsuariosByIdFiltering() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        Long id = usuario.getId();

        defaultUsuarioFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUsuarioFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUsuarioFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUsuariosByPlanoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where plano equals to
        defaultUsuarioFiltering("plano.equals=" + DEFAULT_PLANO, "plano.equals=" + UPDATED_PLANO);
    }

    @Test
    @Transactional
    void getAllUsuariosByPlanoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where plano in
        defaultUsuarioFiltering("plano.in=" + DEFAULT_PLANO + "," + UPDATED_PLANO, "plano.in=" + UPDATED_PLANO);
    }

    @Test
    @Transactional
    void getAllUsuariosByPlanoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where plano is not null
        defaultUsuarioFiltering("plano.specified=true", "plano.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByDataRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dataRegistro equals to
        defaultUsuarioFiltering("dataRegistro.equals=" + DEFAULT_DATA_REGISTRO, "dataRegistro.equals=" + UPDATED_DATA_REGISTRO);
    }

    @Test
    @Transactional
    void getAllUsuariosByDataRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dataRegistro in
        defaultUsuarioFiltering(
            "dataRegistro.in=" + DEFAULT_DATA_REGISTRO + "," + UPDATED_DATA_REGISTRO,
            "dataRegistro.in=" + UPDATED_DATA_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByDataRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dataRegistro is not null
        defaultUsuarioFiltering("dataRegistro.specified=true", "dataRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByTelefoneIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where telefone equals to
        defaultUsuarioFiltering("telefone.equals=" + DEFAULT_TELEFONE, "telefone.equals=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllUsuariosByTelefoneIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where telefone in
        defaultUsuarioFiltering("telefone.in=" + DEFAULT_TELEFONE + "," + UPDATED_TELEFONE, "telefone.in=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllUsuariosByTelefoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where telefone is not null
        defaultUsuarioFiltering("telefone.specified=true", "telefone.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByTelefoneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where telefone is greater than or equal to
        defaultUsuarioFiltering("telefone.greaterThanOrEqual=" + DEFAULT_TELEFONE, "telefone.greaterThanOrEqual=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllUsuariosByTelefoneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where telefone is less than or equal to
        defaultUsuarioFiltering("telefone.lessThanOrEqual=" + DEFAULT_TELEFONE, "telefone.lessThanOrEqual=" + SMALLER_TELEFONE);
    }

    @Test
    @Transactional
    void getAllUsuariosByTelefoneIsLessThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where telefone is less than
        defaultUsuarioFiltering("telefone.lessThan=" + UPDATED_TELEFONE, "telefone.lessThan=" + DEFAULT_TELEFONE);
    }

    @Test
    @Transactional
    void getAllUsuariosByTelefoneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where telefone is greater than
        defaultUsuarioFiltering("telefone.greaterThan=" + SMALLER_TELEFONE, "telefone.greaterThan=" + DEFAULT_TELEFONE);
    }

    @Test
    @Transactional
    void getAllUsuariosByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where email equals to
        defaultUsuarioFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsuariosByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where email in
        defaultUsuarioFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsuariosByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where email is not null
        defaultUsuarioFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByEmailContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where email contains
        defaultUsuarioFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsuariosByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where email does not contain
        defaultUsuarioFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllUsuariosByDataNascimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dataNascimento equals to
        defaultUsuarioFiltering("dataNascimento.equals=" + DEFAULT_DATA_NASCIMENTO, "dataNascimento.equals=" + UPDATED_DATA_NASCIMENTO);
    }

    @Test
    @Transactional
    void getAllUsuariosByDataNascimentoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dataNascimento in
        defaultUsuarioFiltering(
            "dataNascimento.in=" + DEFAULT_DATA_NASCIMENTO + "," + UPDATED_DATA_NASCIMENTO,
            "dataNascimento.in=" + UPDATED_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByDataNascimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where dataNascimento is not null
        defaultUsuarioFiltering("dataNascimento.specified=true", "dataNascimento.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaConsumoAguaIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaConsumoAgua equals to
        defaultUsuarioFiltering(
            "metaConsumoAgua.equals=" + DEFAULT_META_CONSUMO_AGUA,
            "metaConsumoAgua.equals=" + UPDATED_META_CONSUMO_AGUA
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaConsumoAguaIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaConsumoAgua in
        defaultUsuarioFiltering(
            "metaConsumoAgua.in=" + DEFAULT_META_CONSUMO_AGUA + "," + UPDATED_META_CONSUMO_AGUA,
            "metaConsumoAgua.in=" + UPDATED_META_CONSUMO_AGUA
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaConsumoAguaIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaConsumoAgua is not null
        defaultUsuarioFiltering("metaConsumoAgua.specified=true", "metaConsumoAgua.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaConsumoAguaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaConsumoAgua is greater than or equal to
        defaultUsuarioFiltering(
            "metaConsumoAgua.greaterThanOrEqual=" + DEFAULT_META_CONSUMO_AGUA,
            "metaConsumoAgua.greaterThanOrEqual=" + UPDATED_META_CONSUMO_AGUA
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaConsumoAguaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaConsumoAgua is less than or equal to
        defaultUsuarioFiltering(
            "metaConsumoAgua.lessThanOrEqual=" + DEFAULT_META_CONSUMO_AGUA,
            "metaConsumoAgua.lessThanOrEqual=" + SMALLER_META_CONSUMO_AGUA
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaConsumoAguaIsLessThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaConsumoAgua is less than
        defaultUsuarioFiltering(
            "metaConsumoAgua.lessThan=" + UPDATED_META_CONSUMO_AGUA,
            "metaConsumoAgua.lessThan=" + DEFAULT_META_CONSUMO_AGUA
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaConsumoAguaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaConsumoAgua is greater than
        defaultUsuarioFiltering(
            "metaConsumoAgua.greaterThan=" + SMALLER_META_CONSUMO_AGUA,
            "metaConsumoAgua.greaterThan=" + DEFAULT_META_CONSUMO_AGUA
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaSonoIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaSono equals to
        defaultUsuarioFiltering("metaSono.equals=" + DEFAULT_META_SONO, "metaSono.equals=" + UPDATED_META_SONO);
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaSonoIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaSono in
        defaultUsuarioFiltering("metaSono.in=" + DEFAULT_META_SONO + "," + UPDATED_META_SONO, "metaSono.in=" + UPDATED_META_SONO);
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaSonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaSono is not null
        defaultUsuarioFiltering("metaSono.specified=true", "metaSono.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaSonoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaSono is greater than or equal to
        defaultUsuarioFiltering("metaSono.greaterThanOrEqual=" + DEFAULT_META_SONO, "metaSono.greaterThanOrEqual=" + UPDATED_META_SONO);
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaSonoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaSono is less than or equal to
        defaultUsuarioFiltering("metaSono.lessThanOrEqual=" + DEFAULT_META_SONO, "metaSono.lessThanOrEqual=" + SMALLER_META_SONO);
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaSonoIsLessThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaSono is less than
        defaultUsuarioFiltering("metaSono.lessThan=" + UPDATED_META_SONO, "metaSono.lessThan=" + DEFAULT_META_SONO);
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaSonoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaSono is greater than
        defaultUsuarioFiltering("metaSono.greaterThan=" + SMALLER_META_SONO, "metaSono.greaterThan=" + DEFAULT_META_SONO);
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaCaloriasConsumidasIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaCaloriasConsumidas equals to
        defaultUsuarioFiltering(
            "metaCaloriasConsumidas.equals=" + DEFAULT_META_CALORIAS_CONSUMIDAS,
            "metaCaloriasConsumidas.equals=" + UPDATED_META_CALORIAS_CONSUMIDAS
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaCaloriasConsumidasIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaCaloriasConsumidas in
        defaultUsuarioFiltering(
            "metaCaloriasConsumidas.in=" + DEFAULT_META_CALORIAS_CONSUMIDAS + "," + UPDATED_META_CALORIAS_CONSUMIDAS,
            "metaCaloriasConsumidas.in=" + UPDATED_META_CALORIAS_CONSUMIDAS
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaCaloriasConsumidasIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaCaloriasConsumidas is not null
        defaultUsuarioFiltering("metaCaloriasConsumidas.specified=true", "metaCaloriasConsumidas.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaCaloriasConsumidasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaCaloriasConsumidas is greater than or equal to
        defaultUsuarioFiltering(
            "metaCaloriasConsumidas.greaterThanOrEqual=" + DEFAULT_META_CALORIAS_CONSUMIDAS,
            "metaCaloriasConsumidas.greaterThanOrEqual=" + UPDATED_META_CALORIAS_CONSUMIDAS
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaCaloriasConsumidasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaCaloriasConsumidas is less than or equal to
        defaultUsuarioFiltering(
            "metaCaloriasConsumidas.lessThanOrEqual=" + DEFAULT_META_CALORIAS_CONSUMIDAS,
            "metaCaloriasConsumidas.lessThanOrEqual=" + SMALLER_META_CALORIAS_CONSUMIDAS
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaCaloriasConsumidasIsLessThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaCaloriasConsumidas is less than
        defaultUsuarioFiltering(
            "metaCaloriasConsumidas.lessThan=" + UPDATED_META_CALORIAS_CONSUMIDAS,
            "metaCaloriasConsumidas.lessThan=" + DEFAULT_META_CALORIAS_CONSUMIDAS
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaCaloriasConsumidasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaCaloriasConsumidas is greater than
        defaultUsuarioFiltering(
            "metaCaloriasConsumidas.greaterThan=" + SMALLER_META_CALORIAS_CONSUMIDAS,
            "metaCaloriasConsumidas.greaterThan=" + DEFAULT_META_CALORIAS_CONSUMIDAS
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaCaloriasQueimadasIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaCaloriasQueimadas equals to
        defaultUsuarioFiltering(
            "metaCaloriasQueimadas.equals=" + DEFAULT_META_CALORIAS_QUEIMADAS,
            "metaCaloriasQueimadas.equals=" + UPDATED_META_CALORIAS_QUEIMADAS
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaCaloriasQueimadasIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaCaloriasQueimadas in
        defaultUsuarioFiltering(
            "metaCaloriasQueimadas.in=" + DEFAULT_META_CALORIAS_QUEIMADAS + "," + UPDATED_META_CALORIAS_QUEIMADAS,
            "metaCaloriasQueimadas.in=" + UPDATED_META_CALORIAS_QUEIMADAS
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaCaloriasQueimadasIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaCaloriasQueimadas is not null
        defaultUsuarioFiltering("metaCaloriasQueimadas.specified=true", "metaCaloriasQueimadas.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaCaloriasQueimadasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaCaloriasQueimadas is greater than or equal to
        defaultUsuarioFiltering(
            "metaCaloriasQueimadas.greaterThanOrEqual=" + DEFAULT_META_CALORIAS_QUEIMADAS,
            "metaCaloriasQueimadas.greaterThanOrEqual=" + UPDATED_META_CALORIAS_QUEIMADAS
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaCaloriasQueimadasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaCaloriasQueimadas is less than or equal to
        defaultUsuarioFiltering(
            "metaCaloriasQueimadas.lessThanOrEqual=" + DEFAULT_META_CALORIAS_QUEIMADAS,
            "metaCaloriasQueimadas.lessThanOrEqual=" + SMALLER_META_CALORIAS_QUEIMADAS
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaCaloriasQueimadasIsLessThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaCaloriasQueimadas is less than
        defaultUsuarioFiltering(
            "metaCaloriasQueimadas.lessThan=" + UPDATED_META_CALORIAS_QUEIMADAS,
            "metaCaloriasQueimadas.lessThan=" + DEFAULT_META_CALORIAS_QUEIMADAS
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByMetaCaloriasQueimadasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where metaCaloriasQueimadas is greater than
        defaultUsuarioFiltering(
            "metaCaloriasQueimadas.greaterThan=" + SMALLER_META_CALORIAS_QUEIMADAS,
            "metaCaloriasQueimadas.greaterThan=" + DEFAULT_META_CALORIAS_QUEIMADAS
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByPontosUserIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where pontosUser equals to
        defaultUsuarioFiltering("pontosUser.equals=" + DEFAULT_PONTOS_USER, "pontosUser.equals=" + UPDATED_PONTOS_USER);
    }

    @Test
    @Transactional
    void getAllUsuariosByPontosUserIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where pontosUser in
        defaultUsuarioFiltering("pontosUser.in=" + DEFAULT_PONTOS_USER + "," + UPDATED_PONTOS_USER, "pontosUser.in=" + UPDATED_PONTOS_USER);
    }

    @Test
    @Transactional
    void getAllUsuariosByPontosUserIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where pontosUser is not null
        defaultUsuarioFiltering("pontosUser.specified=true", "pontosUser.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByPontosUserIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where pontosUser is greater than or equal to
        defaultUsuarioFiltering(
            "pontosUser.greaterThanOrEqual=" + DEFAULT_PONTOS_USER,
            "pontosUser.greaterThanOrEqual=" + UPDATED_PONTOS_USER
        );
    }

    @Test
    @Transactional
    void getAllUsuariosByPontosUserIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where pontosUser is less than or equal to
        defaultUsuarioFiltering("pontosUser.lessThanOrEqual=" + DEFAULT_PONTOS_USER, "pontosUser.lessThanOrEqual=" + SMALLER_PONTOS_USER);
    }

    @Test
    @Transactional
    void getAllUsuariosByPontosUserIsLessThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where pontosUser is less than
        defaultUsuarioFiltering("pontosUser.lessThan=" + UPDATED_PONTOS_USER, "pontosUser.lessThan=" + DEFAULT_PONTOS_USER);
    }

    @Test
    @Transactional
    void getAllUsuariosByPontosUserIsGreaterThanSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where pontosUser is greater than
        defaultUsuarioFiltering("pontosUser.greaterThan=" + SMALLER_PONTOS_USER, "pontosUser.greaterThan=" + DEFAULT_PONTOS_USER);
    }

    @Test
    @Transactional
    void getAllUsuariosByGeneroIsEqualToSomething() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where genero equals to
        defaultUsuarioFiltering("genero.equals=" + DEFAULT_GENERO, "genero.equals=" + UPDATED_GENERO);
    }

    @Test
    @Transactional
    void getAllUsuariosByGeneroIsInShouldWork() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where genero in
        defaultUsuarioFiltering("genero.in=" + DEFAULT_GENERO + "," + UPDATED_GENERO, "genero.in=" + UPDATED_GENERO);
    }

    @Test
    @Transactional
    void getAllUsuariosByGeneroIsNullOrNotNull() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        // Get all the usuarioList where genero is not null
        defaultUsuarioFiltering("genero.specified=true", "genero.specified=false");
    }

    @Test
    @Transactional
    void getAllUsuariosByInternalUserIsEqualToSomething() throws Exception {
        User internalUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            usuarioRepository.saveAndFlush(usuario);
            internalUser = UserResourceIT.createEntity(em);
        } else {
            internalUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(internalUser);
        em.flush();
        usuario.setInternalUser(internalUser);
        usuarioRepository.saveAndFlush(usuario);
        Long internalUserId = internalUser.getId();
        // Get all the usuarioList where internalUser equals to internalUserId
        defaultUsuarioShouldBeFound("internalUserId.equals=" + internalUserId);

        // Get all the usuarioList where internalUser equals to (internalUserId + 1)
        defaultUsuarioShouldNotBeFound("internalUserId.equals=" + (internalUserId + 1));
    }

    private void defaultUsuarioFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUsuarioShouldBeFound(shouldBeFound);
        defaultUsuarioShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUsuarioShouldBeFound(String filter) throws Exception {
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(usuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].plano").value(hasItem(DEFAULT_PLANO.toString())))
            .andExpect(jsonPath("$.[*].dataRegistro").value(hasItem(DEFAULT_DATA_REGISTRO.toString())))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].metaConsumoAgua").value(hasItem(DEFAULT_META_CONSUMO_AGUA)))
            .andExpect(jsonPath("$.[*].metaSono").value(hasItem(DEFAULT_META_SONO.doubleValue())))
            .andExpect(jsonPath("$.[*].metaCaloriasConsumidas").value(hasItem(DEFAULT_META_CALORIAS_CONSUMIDAS.doubleValue())))
            .andExpect(jsonPath("$.[*].metaCaloriasQueimadas").value(hasItem(DEFAULT_META_CALORIAS_QUEIMADAS.doubleValue())))
            .andExpect(jsonPath("$.[*].pontosUser").value(hasItem(DEFAULT_PONTOS_USER)))
            .andExpect(jsonPath("$.[*].genero").value(hasItem(DEFAULT_GENERO.toString())));

        // Check, that the count call also returns 1
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUsuarioShouldNotBeFound(String filter) throws Exception {
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUsuario() throws Exception {
        // Get the usuario
        restUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the usuario
        Usuario updatedUsuario = usuarioRepository.findById(usuario.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUsuario are not directly saved in db
        em.detach(updatedUsuario);
        updatedUsuario
            .plano(UPDATED_PLANO)
            .dataRegistro(UPDATED_DATA_REGISTRO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .metaConsumoAgua(UPDATED_META_CONSUMO_AGUA)
            .metaSono(UPDATED_META_SONO)
            .metaCaloriasConsumidas(UPDATED_META_CALORIAS_CONSUMIDAS)
            .metaCaloriasQueimadas(UPDATED_META_CALORIAS_QUEIMADAS)
            .pontosUser(UPDATED_PONTOS_USER)
            .genero(UPDATED_GENERO);

        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUsuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUsuarioToMatchAllProperties(updatedUsuario);
    }

    @Test
    @Transactional
    void putNonExistingUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuario.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(put(ENTITY_API_URL_ID, usuario.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuario)))
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(usuario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario
            .dataRegistro(UPDATED_DATA_REGISTRO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL)
            .metaConsumoAgua(UPDATED_META_CONSUMO_AGUA)
            .genero(UPDATED_GENERO);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUsuarioUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedUsuario, usuario), getPersistedUsuario(usuario));
    }

    @Test
    @Transactional
    void fullUpdateUsuarioWithPatch() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the usuario using partial update
        Usuario partialUpdatedUsuario = new Usuario();
        partialUpdatedUsuario.setId(usuario.getId());

        partialUpdatedUsuario
            .plano(UPDATED_PLANO)
            .dataRegistro(UPDATED_DATA_REGISTRO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .metaConsumoAgua(UPDATED_META_CONSUMO_AGUA)
            .metaSono(UPDATED_META_SONO)
            .metaCaloriasConsumidas(UPDATED_META_CALORIAS_CONSUMIDAS)
            .metaCaloriasQueimadas(UPDATED_META_CALORIAS_QUEIMADAS)
            .pontosUser(UPDATED_PONTOS_USER)
            .genero(UPDATED_GENERO);

        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUsuario))
            )
            .andExpect(status().isOk());

        // Validate the Usuario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUsuarioUpdatableFieldsEquals(partialUpdatedUsuario, getPersistedUsuario(partialUpdatedUsuario));
    }

    @Test
    @Transactional
    void patchNonExistingUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuario.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, usuario.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(usuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the Usuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        usuario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUsuarioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(usuario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Usuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUsuario() throws Exception {
        // Initialize the database
        usuarioRepository.saveAndFlush(usuario);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the usuario
        restUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, usuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return usuarioRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Usuario getPersistedUsuario(Usuario usuario) {
        return usuarioRepository.findById(usuario.getId()).orElseThrow();
    }

    protected void assertPersistedUsuarioToMatchAllProperties(Usuario expectedUsuario) {
        assertUsuarioAllPropertiesEquals(expectedUsuario, getPersistedUsuario(expectedUsuario));
    }

    protected void assertPersistedUsuarioToMatchUpdatableProperties(Usuario expectedUsuario) {
        assertUsuarioAllUpdatablePropertiesEquals(expectedUsuario, getPersistedUsuario(expectedUsuario));
    }
}
