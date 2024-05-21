package com.healthscore.app.web.rest;

import static com.healthscore.app.domain.AnuncioAsserts.*;
import static com.healthscore.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthscore.app.IntegrationTest;
import com.healthscore.app.domain.Anuncio;
import com.healthscore.app.repository.AnuncioRepository;
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
 * Integration tests for the {@link AnuncioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnuncioResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATA_PUBLICACAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_PUBLICACAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATA_FIM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_FIM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_PRECO = 1D;
    private static final Double UPDATED_PRECO = 2D;
    private static final Double SMALLER_PRECO = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/anuncios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AnuncioRepository anuncioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnuncioMockMvc;

    private Anuncio anuncio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Anuncio createEntity(EntityManager em) {
        Anuncio anuncio = new Anuncio()
            .titulo(DEFAULT_TITULO)
            .descricao(DEFAULT_DESCRICAO)
            .dataPublicacao(DEFAULT_DATA_PUBLICACAO)
            .dataInicio(DEFAULT_DATA_INICIO)
            .dataFim(DEFAULT_DATA_FIM)
            .preco(DEFAULT_PRECO);
        return anuncio;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Anuncio createUpdatedEntity(EntityManager em) {
        Anuncio anuncio = new Anuncio()
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .dataPublicacao(UPDATED_DATA_PUBLICACAO)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM)
            .preco(UPDATED_PRECO);
        return anuncio;
    }

    @BeforeEach
    public void initTest() {
        anuncio = createEntity(em);
    }

    @Test
    @Transactional
    void createAnuncio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Anuncio
        var returnedAnuncio = om.readValue(
            restAnuncioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(anuncio)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Anuncio.class
        );

        // Validate the Anuncio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAnuncioUpdatableFieldsEquals(returnedAnuncio, getPersistedAnuncio(returnedAnuncio));
    }

    @Test
    @Transactional
    void createAnuncioWithExistingId() throws Exception {
        // Create the Anuncio with an existing ID
        anuncio.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnuncioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(anuncio)))
            .andExpect(status().isBadRequest());

        // Validate the Anuncio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAnuncios() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList
        restAnuncioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(anuncio.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].dataPublicacao").value(hasItem(DEFAULT_DATA_PUBLICACAO.toString())))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].dataFim").value(hasItem(DEFAULT_DATA_FIM.toString())))
            .andExpect(jsonPath("$.[*].preco").value(hasItem(DEFAULT_PRECO.doubleValue())));
    }

    @Test
    @Transactional
    void getAnuncio() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get the anuncio
        restAnuncioMockMvc
            .perform(get(ENTITY_API_URL_ID, anuncio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(anuncio.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.dataPublicacao").value(DEFAULT_DATA_PUBLICACAO.toString()))
            .andExpect(jsonPath("$.dataInicio").value(DEFAULT_DATA_INICIO.toString()))
            .andExpect(jsonPath("$.dataFim").value(DEFAULT_DATA_FIM.toString()))
            .andExpect(jsonPath("$.preco").value(DEFAULT_PRECO.doubleValue()));
    }

    @Test
    @Transactional
    void getAnunciosByIdFiltering() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        Long id = anuncio.getId();

        defaultAnuncioFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAnuncioFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAnuncioFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAnunciosByTituloIsEqualToSomething() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where titulo equals to
        defaultAnuncioFiltering("titulo.equals=" + DEFAULT_TITULO, "titulo.equals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllAnunciosByTituloIsInShouldWork() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where titulo in
        defaultAnuncioFiltering("titulo.in=" + DEFAULT_TITULO + "," + UPDATED_TITULO, "titulo.in=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllAnunciosByTituloIsNullOrNotNull() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where titulo is not null
        defaultAnuncioFiltering("titulo.specified=true", "titulo.specified=false");
    }

    @Test
    @Transactional
    void getAllAnunciosByTituloContainsSomething() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where titulo contains
        defaultAnuncioFiltering("titulo.contains=" + DEFAULT_TITULO, "titulo.contains=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllAnunciosByTituloNotContainsSomething() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where titulo does not contain
        defaultAnuncioFiltering("titulo.doesNotContain=" + UPDATED_TITULO, "titulo.doesNotContain=" + DEFAULT_TITULO);
    }

    @Test
    @Transactional
    void getAllAnunciosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where descricao equals to
        defaultAnuncioFiltering("descricao.equals=" + DEFAULT_DESCRICAO, "descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllAnunciosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where descricao in
        defaultAnuncioFiltering("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO, "descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllAnunciosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where descricao is not null
        defaultAnuncioFiltering("descricao.specified=true", "descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllAnunciosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where descricao contains
        defaultAnuncioFiltering("descricao.contains=" + DEFAULT_DESCRICAO, "descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllAnunciosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where descricao does not contain
        defaultAnuncioFiltering("descricao.doesNotContain=" + UPDATED_DESCRICAO, "descricao.doesNotContain=" + DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllAnunciosByDataPublicacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where dataPublicacao equals to
        defaultAnuncioFiltering("dataPublicacao.equals=" + DEFAULT_DATA_PUBLICACAO, "dataPublicacao.equals=" + UPDATED_DATA_PUBLICACAO);
    }

    @Test
    @Transactional
    void getAllAnunciosByDataPublicacaoIsInShouldWork() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where dataPublicacao in
        defaultAnuncioFiltering(
            "dataPublicacao.in=" + DEFAULT_DATA_PUBLICACAO + "," + UPDATED_DATA_PUBLICACAO,
            "dataPublicacao.in=" + UPDATED_DATA_PUBLICACAO
        );
    }

    @Test
    @Transactional
    void getAllAnunciosByDataPublicacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where dataPublicacao is not null
        defaultAnuncioFiltering("dataPublicacao.specified=true", "dataPublicacao.specified=false");
    }

    @Test
    @Transactional
    void getAllAnunciosByDataInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where dataInicio equals to
        defaultAnuncioFiltering("dataInicio.equals=" + DEFAULT_DATA_INICIO, "dataInicio.equals=" + UPDATED_DATA_INICIO);
    }

    @Test
    @Transactional
    void getAllAnunciosByDataInicioIsInShouldWork() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where dataInicio in
        defaultAnuncioFiltering("dataInicio.in=" + DEFAULT_DATA_INICIO + "," + UPDATED_DATA_INICIO, "dataInicio.in=" + UPDATED_DATA_INICIO);
    }

    @Test
    @Transactional
    void getAllAnunciosByDataInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where dataInicio is not null
        defaultAnuncioFiltering("dataInicio.specified=true", "dataInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllAnunciosByDataFimIsEqualToSomething() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where dataFim equals to
        defaultAnuncioFiltering("dataFim.equals=" + DEFAULT_DATA_FIM, "dataFim.equals=" + UPDATED_DATA_FIM);
    }

    @Test
    @Transactional
    void getAllAnunciosByDataFimIsInShouldWork() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where dataFim in
        defaultAnuncioFiltering("dataFim.in=" + DEFAULT_DATA_FIM + "," + UPDATED_DATA_FIM, "dataFim.in=" + UPDATED_DATA_FIM);
    }

    @Test
    @Transactional
    void getAllAnunciosByDataFimIsNullOrNotNull() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where dataFim is not null
        defaultAnuncioFiltering("dataFim.specified=true", "dataFim.specified=false");
    }

    @Test
    @Transactional
    void getAllAnunciosByPrecoIsEqualToSomething() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where preco equals to
        defaultAnuncioFiltering("preco.equals=" + DEFAULT_PRECO, "preco.equals=" + UPDATED_PRECO);
    }

    @Test
    @Transactional
    void getAllAnunciosByPrecoIsInShouldWork() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where preco in
        defaultAnuncioFiltering("preco.in=" + DEFAULT_PRECO + "," + UPDATED_PRECO, "preco.in=" + UPDATED_PRECO);
    }

    @Test
    @Transactional
    void getAllAnunciosByPrecoIsNullOrNotNull() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where preco is not null
        defaultAnuncioFiltering("preco.specified=true", "preco.specified=false");
    }

    @Test
    @Transactional
    void getAllAnunciosByPrecoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where preco is greater than or equal to
        defaultAnuncioFiltering("preco.greaterThanOrEqual=" + DEFAULT_PRECO, "preco.greaterThanOrEqual=" + UPDATED_PRECO);
    }

    @Test
    @Transactional
    void getAllAnunciosByPrecoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where preco is less than or equal to
        defaultAnuncioFiltering("preco.lessThanOrEqual=" + DEFAULT_PRECO, "preco.lessThanOrEqual=" + SMALLER_PRECO);
    }

    @Test
    @Transactional
    void getAllAnunciosByPrecoIsLessThanSomething() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where preco is less than
        defaultAnuncioFiltering("preco.lessThan=" + UPDATED_PRECO, "preco.lessThan=" + DEFAULT_PRECO);
    }

    @Test
    @Transactional
    void getAllAnunciosByPrecoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        // Get all the anuncioList where preco is greater than
        defaultAnuncioFiltering("preco.greaterThan=" + SMALLER_PRECO, "preco.greaterThan=" + DEFAULT_PRECO);
    }

    private void defaultAnuncioFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAnuncioShouldBeFound(shouldBeFound);
        defaultAnuncioShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnuncioShouldBeFound(String filter) throws Exception {
        restAnuncioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(anuncio.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].dataPublicacao").value(hasItem(DEFAULT_DATA_PUBLICACAO.toString())))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].dataFim").value(hasItem(DEFAULT_DATA_FIM.toString())))
            .andExpect(jsonPath("$.[*].preco").value(hasItem(DEFAULT_PRECO.doubleValue())));

        // Check, that the count call also returns 1
        restAnuncioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnuncioShouldNotBeFound(String filter) throws Exception {
        restAnuncioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnuncioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAnuncio() throws Exception {
        // Get the anuncio
        restAnuncioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAnuncio() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the anuncio
        Anuncio updatedAnuncio = anuncioRepository.findById(anuncio.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAnuncio are not directly saved in db
        em.detach(updatedAnuncio);
        updatedAnuncio
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .dataPublicacao(UPDATED_DATA_PUBLICACAO)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM)
            .preco(UPDATED_PRECO);

        restAnuncioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAnuncio.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAnuncio))
            )
            .andExpect(status().isOk());

        // Validate the Anuncio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAnuncioToMatchAllProperties(updatedAnuncio);
    }

    @Test
    @Transactional
    void putNonExistingAnuncio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        anuncio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnuncioMockMvc
            .perform(put(ENTITY_API_URL_ID, anuncio.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(anuncio)))
            .andExpect(status().isBadRequest());

        // Validate the Anuncio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnuncio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        anuncio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnuncioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(anuncio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Anuncio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnuncio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        anuncio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnuncioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(anuncio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Anuncio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnuncioWithPatch() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the anuncio using partial update
        Anuncio partialUpdatedAnuncio = new Anuncio();
        partialUpdatedAnuncio.setId(anuncio.getId());

        partialUpdatedAnuncio.dataPublicacao(UPDATED_DATA_PUBLICACAO).dataFim(UPDATED_DATA_FIM);

        restAnuncioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnuncio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnuncio))
            )
            .andExpect(status().isOk());

        // Validate the Anuncio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnuncioUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAnuncio, anuncio), getPersistedAnuncio(anuncio));
    }

    @Test
    @Transactional
    void fullUpdateAnuncioWithPatch() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the anuncio using partial update
        Anuncio partialUpdatedAnuncio = new Anuncio();
        partialUpdatedAnuncio.setId(anuncio.getId());

        partialUpdatedAnuncio
            .titulo(UPDATED_TITULO)
            .descricao(UPDATED_DESCRICAO)
            .dataPublicacao(UPDATED_DATA_PUBLICACAO)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM)
            .preco(UPDATED_PRECO);

        restAnuncioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnuncio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnuncio))
            )
            .andExpect(status().isOk());

        // Validate the Anuncio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnuncioUpdatableFieldsEquals(partialUpdatedAnuncio, getPersistedAnuncio(partialUpdatedAnuncio));
    }

    @Test
    @Transactional
    void patchNonExistingAnuncio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        anuncio.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnuncioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, anuncio.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(anuncio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Anuncio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnuncio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        anuncio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnuncioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(anuncio))
            )
            .andExpect(status().isBadRequest());

        // Validate the Anuncio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnuncio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        anuncio.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnuncioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(anuncio)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Anuncio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnuncio() throws Exception {
        // Initialize the database
        anuncioRepository.saveAndFlush(anuncio);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the anuncio
        restAnuncioMockMvc
            .perform(delete(ENTITY_API_URL_ID, anuncio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return anuncioRepository.count();
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

    protected Anuncio getPersistedAnuncio(Anuncio anuncio) {
        return anuncioRepository.findById(anuncio.getId()).orElseThrow();
    }

    protected void assertPersistedAnuncioToMatchAllProperties(Anuncio expectedAnuncio) {
        assertAnuncioAllPropertiesEquals(expectedAnuncio, getPersistedAnuncio(expectedAnuncio));
    }

    protected void assertPersistedAnuncioToMatchUpdatableProperties(Anuncio expectedAnuncio) {
        assertAnuncioAllUpdatablePropertiesEquals(expectedAnuncio, getPersistedAnuncio(expectedAnuncio));
    }
}
