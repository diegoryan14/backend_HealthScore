package com.healthscore.app.web.rest;

import static com.healthscore.app.domain.AtividadeFisicaAsserts.*;
import static com.healthscore.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthscore.app.IntegrationTest;
import com.healthscore.app.domain.AtividadeFisica;
import com.healthscore.app.domain.User;
import com.healthscore.app.domain.enumeration.TipoAtividade;
import com.healthscore.app.repository.AtividadeFisicaRepository;
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
 * Integration tests for the {@link AtividadeFisicaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AtividadeFisicaResourceIT {

    private static final TipoAtividade DEFAULT_TIPO_ATIVIDADE = TipoAtividade.CORRIDA;
    private static final TipoAtividade UPDATED_TIPO_ATIVIDADE = TipoAtividade.FUTEBOL;

    private static final Instant DEFAULT_DATA_HORARIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_HORARIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_DURACAO = 1;
    private static final Integer UPDATED_DURACAO = 2;
    private static final Integer SMALLER_DURACAO = 1 - 1;

    private static final Integer DEFAULT_PASSOS_CALORIAS = 1;
    private static final Integer UPDATED_PASSOS_CALORIAS = 2;
    private static final Integer SMALLER_PASSOS_CALORIAS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/atividade-fisicas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AtividadeFisicaRepository atividadeFisicaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAtividadeFisicaMockMvc;

    private AtividadeFisica atividadeFisica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AtividadeFisica createEntity(EntityManager em) {
        AtividadeFisica atividadeFisica = new AtividadeFisica()
            .tipoAtividade(DEFAULT_TIPO_ATIVIDADE)
            .dataHorario(DEFAULT_DATA_HORARIO)
            .duracao(DEFAULT_DURACAO)
            .passosCalorias(DEFAULT_PASSOS_CALORIAS);
        return atividadeFisica;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AtividadeFisica createUpdatedEntity(EntityManager em) {
        AtividadeFisica atividadeFisica = new AtividadeFisica()
            .tipoAtividade(UPDATED_TIPO_ATIVIDADE)
            .dataHorario(UPDATED_DATA_HORARIO)
            .duracao(UPDATED_DURACAO)
            .passosCalorias(UPDATED_PASSOS_CALORIAS);
        return atividadeFisica;
    }

    @BeforeEach
    public void initTest() {
        atividadeFisica = createEntity(em);
    }

    @Test
    @Transactional
    void createAtividadeFisica() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AtividadeFisica
        var returnedAtividadeFisica = om.readValue(
            restAtividadeFisicaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(atividadeFisica)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AtividadeFisica.class
        );

        // Validate the AtividadeFisica in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAtividadeFisicaUpdatableFieldsEquals(returnedAtividadeFisica, getPersistedAtividadeFisica(returnedAtividadeFisica));
    }

    @Test
    @Transactional
    void createAtividadeFisicaWithExistingId() throws Exception {
        // Create the AtividadeFisica with an existing ID
        atividadeFisica.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAtividadeFisicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(atividadeFisica)))
            .andExpect(status().isBadRequest());

        // Validate the AtividadeFisica in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAtividadeFisicas() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList
        restAtividadeFisicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(atividadeFisica.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoAtividade").value(hasItem(DEFAULT_TIPO_ATIVIDADE.toString())))
            .andExpect(jsonPath("$.[*].dataHorario").value(hasItem(DEFAULT_DATA_HORARIO.toString())))
            .andExpect(jsonPath("$.[*].duracao").value(hasItem(DEFAULT_DURACAO)))
            .andExpect(jsonPath("$.[*].passosCalorias").value(hasItem(DEFAULT_PASSOS_CALORIAS)));
    }

    @Test
    @Transactional
    void getAtividadeFisica() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get the atividadeFisica
        restAtividadeFisicaMockMvc
            .perform(get(ENTITY_API_URL_ID, atividadeFisica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(atividadeFisica.getId().intValue()))
            .andExpect(jsonPath("$.tipoAtividade").value(DEFAULT_TIPO_ATIVIDADE.toString()))
            .andExpect(jsonPath("$.dataHorario").value(DEFAULT_DATA_HORARIO.toString()))
            .andExpect(jsonPath("$.duracao").value(DEFAULT_DURACAO))
            .andExpect(jsonPath("$.passosCalorias").value(DEFAULT_PASSOS_CALORIAS));
    }

    @Test
    @Transactional
    void getAtividadeFisicasByIdFiltering() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        Long id = atividadeFisica.getId();

        defaultAtividadeFisicaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAtividadeFisicaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAtividadeFisicaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByTipoAtividadeIsEqualToSomething() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where tipoAtividade equals to
        defaultAtividadeFisicaFiltering("tipoAtividade.equals=" + DEFAULT_TIPO_ATIVIDADE, "tipoAtividade.equals=" + UPDATED_TIPO_ATIVIDADE);
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByTipoAtividadeIsInShouldWork() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where tipoAtividade in
        defaultAtividadeFisicaFiltering(
            "tipoAtividade.in=" + DEFAULT_TIPO_ATIVIDADE + "," + UPDATED_TIPO_ATIVIDADE,
            "tipoAtividade.in=" + UPDATED_TIPO_ATIVIDADE
        );
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByTipoAtividadeIsNullOrNotNull() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where tipoAtividade is not null
        defaultAtividadeFisicaFiltering("tipoAtividade.specified=true", "tipoAtividade.specified=false");
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByDataHorarioIsEqualToSomething() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where dataHorario equals to
        defaultAtividadeFisicaFiltering("dataHorario.equals=" + DEFAULT_DATA_HORARIO, "dataHorario.equals=" + UPDATED_DATA_HORARIO);
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByDataHorarioIsInShouldWork() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where dataHorario in
        defaultAtividadeFisicaFiltering(
            "dataHorario.in=" + DEFAULT_DATA_HORARIO + "," + UPDATED_DATA_HORARIO,
            "dataHorario.in=" + UPDATED_DATA_HORARIO
        );
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByDataHorarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where dataHorario is not null
        defaultAtividadeFisicaFiltering("dataHorario.specified=true", "dataHorario.specified=false");
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByDuracaoIsEqualToSomething() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where duracao equals to
        defaultAtividadeFisicaFiltering("duracao.equals=" + DEFAULT_DURACAO, "duracao.equals=" + UPDATED_DURACAO);
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByDuracaoIsInShouldWork() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where duracao in
        defaultAtividadeFisicaFiltering("duracao.in=" + DEFAULT_DURACAO + "," + UPDATED_DURACAO, "duracao.in=" + UPDATED_DURACAO);
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByDuracaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where duracao is not null
        defaultAtividadeFisicaFiltering("duracao.specified=true", "duracao.specified=false");
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByDuracaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where duracao is greater than or equal to
        defaultAtividadeFisicaFiltering("duracao.greaterThanOrEqual=" + DEFAULT_DURACAO, "duracao.greaterThanOrEqual=" + UPDATED_DURACAO);
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByDuracaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where duracao is less than or equal to
        defaultAtividadeFisicaFiltering("duracao.lessThanOrEqual=" + DEFAULT_DURACAO, "duracao.lessThanOrEqual=" + SMALLER_DURACAO);
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByDuracaoIsLessThanSomething() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where duracao is less than
        defaultAtividadeFisicaFiltering("duracao.lessThan=" + UPDATED_DURACAO, "duracao.lessThan=" + DEFAULT_DURACAO);
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByDuracaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where duracao is greater than
        defaultAtividadeFisicaFiltering("duracao.greaterThan=" + SMALLER_DURACAO, "duracao.greaterThan=" + DEFAULT_DURACAO);
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByPassosCaloriasIsEqualToSomething() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where passosCalorias equals to
        defaultAtividadeFisicaFiltering(
            "passosCalorias.equals=" + DEFAULT_PASSOS_CALORIAS,
            "passosCalorias.equals=" + UPDATED_PASSOS_CALORIAS
        );
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByPassosCaloriasIsInShouldWork() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where passosCalorias in
        defaultAtividadeFisicaFiltering(
            "passosCalorias.in=" + DEFAULT_PASSOS_CALORIAS + "," + UPDATED_PASSOS_CALORIAS,
            "passosCalorias.in=" + UPDATED_PASSOS_CALORIAS
        );
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByPassosCaloriasIsNullOrNotNull() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where passosCalorias is not null
        defaultAtividadeFisicaFiltering("passosCalorias.specified=true", "passosCalorias.specified=false");
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByPassosCaloriasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where passosCalorias is greater than or equal to
        defaultAtividadeFisicaFiltering(
            "passosCalorias.greaterThanOrEqual=" + DEFAULT_PASSOS_CALORIAS,
            "passosCalorias.greaterThanOrEqual=" + UPDATED_PASSOS_CALORIAS
        );
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByPassosCaloriasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where passosCalorias is less than or equal to
        defaultAtividadeFisicaFiltering(
            "passosCalorias.lessThanOrEqual=" + DEFAULT_PASSOS_CALORIAS,
            "passosCalorias.lessThanOrEqual=" + SMALLER_PASSOS_CALORIAS
        );
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByPassosCaloriasIsLessThanSomething() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where passosCalorias is less than
        defaultAtividadeFisicaFiltering(
            "passosCalorias.lessThan=" + UPDATED_PASSOS_CALORIAS,
            "passosCalorias.lessThan=" + DEFAULT_PASSOS_CALORIAS
        );
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByPassosCaloriasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        // Get all the atividadeFisicaList where passosCalorias is greater than
        defaultAtividadeFisicaFiltering(
            "passosCalorias.greaterThan=" + SMALLER_PASSOS_CALORIAS,
            "passosCalorias.greaterThan=" + DEFAULT_PASSOS_CALORIAS
        );
    }

    @Test
    @Transactional
    void getAllAtividadeFisicasByInternalUserIsEqualToSomething() throws Exception {
        User internalUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            atividadeFisicaRepository.saveAndFlush(atividadeFisica);
            internalUser = UserResourceIT.createEntity(em);
        } else {
            internalUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(internalUser);
        em.flush();
        atividadeFisica.setInternalUser(internalUser);
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);
        Long internalUserId = internalUser.getId();
        // Get all the atividadeFisicaList where internalUser equals to internalUserId
        defaultAtividadeFisicaShouldBeFound("internalUserId.equals=" + internalUserId);

        // Get all the atividadeFisicaList where internalUser equals to (internalUserId + 1)
        defaultAtividadeFisicaShouldNotBeFound("internalUserId.equals=" + (internalUserId + 1));
    }

    private void defaultAtividadeFisicaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAtividadeFisicaShouldBeFound(shouldBeFound);
        defaultAtividadeFisicaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAtividadeFisicaShouldBeFound(String filter) throws Exception {
        restAtividadeFisicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(atividadeFisica.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoAtividade").value(hasItem(DEFAULT_TIPO_ATIVIDADE.toString())))
            .andExpect(jsonPath("$.[*].dataHorario").value(hasItem(DEFAULT_DATA_HORARIO.toString())))
            .andExpect(jsonPath("$.[*].duracao").value(hasItem(DEFAULT_DURACAO)))
            .andExpect(jsonPath("$.[*].passosCalorias").value(hasItem(DEFAULT_PASSOS_CALORIAS)));

        // Check, that the count call also returns 1
        restAtividadeFisicaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAtividadeFisicaShouldNotBeFound(String filter) throws Exception {
        restAtividadeFisicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAtividadeFisicaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAtividadeFisica() throws Exception {
        // Get the atividadeFisica
        restAtividadeFisicaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAtividadeFisica() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the atividadeFisica
        AtividadeFisica updatedAtividadeFisica = atividadeFisicaRepository.findById(atividadeFisica.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAtividadeFisica are not directly saved in db
        em.detach(updatedAtividadeFisica);
        updatedAtividadeFisica
            .tipoAtividade(UPDATED_TIPO_ATIVIDADE)
            .dataHorario(UPDATED_DATA_HORARIO)
            .duracao(UPDATED_DURACAO)
            .passosCalorias(UPDATED_PASSOS_CALORIAS);

        restAtividadeFisicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAtividadeFisica.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAtividadeFisica))
            )
            .andExpect(status().isOk());

        // Validate the AtividadeFisica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAtividadeFisicaToMatchAllProperties(updatedAtividadeFisica);
    }

    @Test
    @Transactional
    void putNonExistingAtividadeFisica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        atividadeFisica.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAtividadeFisicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, atividadeFisica.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(atividadeFisica))
            )
            .andExpect(status().isBadRequest());

        // Validate the AtividadeFisica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAtividadeFisica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        atividadeFisica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtividadeFisicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(atividadeFisica))
            )
            .andExpect(status().isBadRequest());

        // Validate the AtividadeFisica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAtividadeFisica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        atividadeFisica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtividadeFisicaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(atividadeFisica)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AtividadeFisica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAtividadeFisicaWithPatch() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the atividadeFisica using partial update
        AtividadeFisica partialUpdatedAtividadeFisica = new AtividadeFisica();
        partialUpdatedAtividadeFisica.setId(atividadeFisica.getId());

        partialUpdatedAtividadeFisica.dataHorario(UPDATED_DATA_HORARIO).duracao(UPDATED_DURACAO);

        restAtividadeFisicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAtividadeFisica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAtividadeFisica))
            )
            .andExpect(status().isOk());

        // Validate the AtividadeFisica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAtividadeFisicaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAtividadeFisica, atividadeFisica),
            getPersistedAtividadeFisica(atividadeFisica)
        );
    }

    @Test
    @Transactional
    void fullUpdateAtividadeFisicaWithPatch() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the atividadeFisica using partial update
        AtividadeFisica partialUpdatedAtividadeFisica = new AtividadeFisica();
        partialUpdatedAtividadeFisica.setId(atividadeFisica.getId());

        partialUpdatedAtividadeFisica
            .tipoAtividade(UPDATED_TIPO_ATIVIDADE)
            .dataHorario(UPDATED_DATA_HORARIO)
            .duracao(UPDATED_DURACAO)
            .passosCalorias(UPDATED_PASSOS_CALORIAS);

        restAtividadeFisicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAtividadeFisica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAtividadeFisica))
            )
            .andExpect(status().isOk());

        // Validate the AtividadeFisica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAtividadeFisicaUpdatableFieldsEquals(
            partialUpdatedAtividadeFisica,
            getPersistedAtividadeFisica(partialUpdatedAtividadeFisica)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAtividadeFisica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        atividadeFisica.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAtividadeFisicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, atividadeFisica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(atividadeFisica))
            )
            .andExpect(status().isBadRequest());

        // Validate the AtividadeFisica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAtividadeFisica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        atividadeFisica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtividadeFisicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(atividadeFisica))
            )
            .andExpect(status().isBadRequest());

        // Validate the AtividadeFisica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAtividadeFisica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        atividadeFisica.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAtividadeFisicaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(atividadeFisica)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AtividadeFisica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAtividadeFisica() throws Exception {
        // Initialize the database
        atividadeFisicaRepository.saveAndFlush(atividadeFisica);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the atividadeFisica
        restAtividadeFisicaMockMvc
            .perform(delete(ENTITY_API_URL_ID, atividadeFisica.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return atividadeFisicaRepository.count();
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

    protected AtividadeFisica getPersistedAtividadeFisica(AtividadeFisica atividadeFisica) {
        return atividadeFisicaRepository.findById(atividadeFisica.getId()).orElseThrow();
    }

    protected void assertPersistedAtividadeFisicaToMatchAllProperties(AtividadeFisica expectedAtividadeFisica) {
        assertAtividadeFisicaAllPropertiesEquals(expectedAtividadeFisica, getPersistedAtividadeFisica(expectedAtividadeFisica));
    }

    protected void assertPersistedAtividadeFisicaToMatchUpdatableProperties(AtividadeFisica expectedAtividadeFisica) {
        assertAtividadeFisicaAllUpdatablePropertiesEquals(expectedAtividadeFisica, getPersistedAtividadeFisica(expectedAtividadeFisica));
    }
}
