package com.healthscore.app.web.rest;

import static com.healthscore.app.domain.MetasSaudeAsserts.*;
import static com.healthscore.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthscore.app.IntegrationTest;
import com.healthscore.app.domain.MetasSaude;
import com.healthscore.app.domain.User;
import com.healthscore.app.domain.enumeration.TipoMeta;
import com.healthscore.app.repository.MetasSaudeRepository;
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
 * Integration tests for the {@link MetasSaudeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MetasSaudeResourceIT {

    private static final TipoMeta DEFAULT_TIPO_META = TipoMeta.PERDA_DE_PESO;
    private static final TipoMeta UPDATED_TIPO_META = TipoMeta.GANHO_DE_MASSA_MUSCULAR;

    private static final Integer DEFAULT_VALOR_META = 1;
    private static final Integer UPDATED_VALOR_META = 2;
    private static final Integer SMALLER_VALOR_META = 1 - 1;

    private static final Instant DEFAULT_DATA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATA_FIM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_FIM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/metas-saudes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MetasSaudeRepository metasSaudeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMetasSaudeMockMvc;

    private MetasSaude metasSaude;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetasSaude createEntity(EntityManager em) {
        MetasSaude metasSaude = new MetasSaude()
            .tipoMeta(DEFAULT_TIPO_META)
            .valorMeta(DEFAULT_VALOR_META)
            .dataInicio(DEFAULT_DATA_INICIO)
            .dataFim(DEFAULT_DATA_FIM);
        return metasSaude;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetasSaude createUpdatedEntity(EntityManager em) {
        MetasSaude metasSaude = new MetasSaude()
            .tipoMeta(UPDATED_TIPO_META)
            .valorMeta(UPDATED_VALOR_META)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM);
        return metasSaude;
    }

    @BeforeEach
    public void initTest() {
        metasSaude = createEntity(em);
    }

    @Test
    @Transactional
    void createMetasSaude() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MetasSaude
        var returnedMetasSaude = om.readValue(
            restMetasSaudeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metasSaude)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MetasSaude.class
        );

        // Validate the MetasSaude in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMetasSaudeUpdatableFieldsEquals(returnedMetasSaude, getPersistedMetasSaude(returnedMetasSaude));
    }

    @Test
    @Transactional
    void createMetasSaudeWithExistingId() throws Exception {
        // Create the MetasSaude with an existing ID
        metasSaude.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetasSaudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metasSaude)))
            .andExpect(status().isBadRequest());

        // Validate the MetasSaude in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMetasSaudes() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get all the metasSaudeList
        restMetasSaudeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metasSaude.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoMeta").value(hasItem(DEFAULT_TIPO_META.toString())))
            .andExpect(jsonPath("$.[*].valorMeta").value(hasItem(DEFAULT_VALOR_META)))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].dataFim").value(hasItem(DEFAULT_DATA_FIM.toString())));
    }

    @Test
    @Transactional
    void getMetasSaude() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get the metasSaude
        restMetasSaudeMockMvc
            .perform(get(ENTITY_API_URL_ID, metasSaude.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(metasSaude.getId().intValue()))
            .andExpect(jsonPath("$.tipoMeta").value(DEFAULT_TIPO_META.toString()))
            .andExpect(jsonPath("$.valorMeta").value(DEFAULT_VALOR_META))
            .andExpect(jsonPath("$.dataInicio").value(DEFAULT_DATA_INICIO.toString()))
            .andExpect(jsonPath("$.dataFim").value(DEFAULT_DATA_FIM.toString()));
    }

    @Test
    @Transactional
    void getMetasSaudesByIdFiltering() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        Long id = metasSaude.getId();

        defaultMetasSaudeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMetasSaudeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMetasSaudeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMetasSaudesByTipoMetaIsEqualToSomething() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get all the metasSaudeList where tipoMeta equals to
        defaultMetasSaudeFiltering("tipoMeta.equals=" + DEFAULT_TIPO_META, "tipoMeta.equals=" + UPDATED_TIPO_META);
    }

    @Test
    @Transactional
    void getAllMetasSaudesByTipoMetaIsInShouldWork() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get all the metasSaudeList where tipoMeta in
        defaultMetasSaudeFiltering("tipoMeta.in=" + DEFAULT_TIPO_META + "," + UPDATED_TIPO_META, "tipoMeta.in=" + UPDATED_TIPO_META);
    }

    @Test
    @Transactional
    void getAllMetasSaudesByTipoMetaIsNullOrNotNull() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get all the metasSaudeList where tipoMeta is not null
        defaultMetasSaudeFiltering("tipoMeta.specified=true", "tipoMeta.specified=false");
    }

    @Test
    @Transactional
    void getAllMetasSaudesByValorMetaIsEqualToSomething() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get all the metasSaudeList where valorMeta equals to
        defaultMetasSaudeFiltering("valorMeta.equals=" + DEFAULT_VALOR_META, "valorMeta.equals=" + UPDATED_VALOR_META);
    }

    @Test
    @Transactional
    void getAllMetasSaudesByValorMetaIsInShouldWork() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get all the metasSaudeList where valorMeta in
        defaultMetasSaudeFiltering("valorMeta.in=" + DEFAULT_VALOR_META + "," + UPDATED_VALOR_META, "valorMeta.in=" + UPDATED_VALOR_META);
    }

    @Test
    @Transactional
    void getAllMetasSaudesByValorMetaIsNullOrNotNull() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get all the metasSaudeList where valorMeta is not null
        defaultMetasSaudeFiltering("valorMeta.specified=true", "valorMeta.specified=false");
    }

    @Test
    @Transactional
    void getAllMetasSaudesByValorMetaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get all the metasSaudeList where valorMeta is greater than or equal to
        defaultMetasSaudeFiltering(
            "valorMeta.greaterThanOrEqual=" + DEFAULT_VALOR_META,
            "valorMeta.greaterThanOrEqual=" + UPDATED_VALOR_META
        );
    }

    @Test
    @Transactional
    void getAllMetasSaudesByValorMetaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get all the metasSaudeList where valorMeta is less than or equal to
        defaultMetasSaudeFiltering("valorMeta.lessThanOrEqual=" + DEFAULT_VALOR_META, "valorMeta.lessThanOrEqual=" + SMALLER_VALOR_META);
    }

    @Test
    @Transactional
    void getAllMetasSaudesByValorMetaIsLessThanSomething() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get all the metasSaudeList where valorMeta is less than
        defaultMetasSaudeFiltering("valorMeta.lessThan=" + UPDATED_VALOR_META, "valorMeta.lessThan=" + DEFAULT_VALOR_META);
    }

    @Test
    @Transactional
    void getAllMetasSaudesByValorMetaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get all the metasSaudeList where valorMeta is greater than
        defaultMetasSaudeFiltering("valorMeta.greaterThan=" + SMALLER_VALOR_META, "valorMeta.greaterThan=" + DEFAULT_VALOR_META);
    }

    @Test
    @Transactional
    void getAllMetasSaudesByDataInicioIsEqualToSomething() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get all the metasSaudeList where dataInicio equals to
        defaultMetasSaudeFiltering("dataInicio.equals=" + DEFAULT_DATA_INICIO, "dataInicio.equals=" + UPDATED_DATA_INICIO);
    }

    @Test
    @Transactional
    void getAllMetasSaudesByDataInicioIsInShouldWork() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get all the metasSaudeList where dataInicio in
        defaultMetasSaudeFiltering(
            "dataInicio.in=" + DEFAULT_DATA_INICIO + "," + UPDATED_DATA_INICIO,
            "dataInicio.in=" + UPDATED_DATA_INICIO
        );
    }

    @Test
    @Transactional
    void getAllMetasSaudesByDataInicioIsNullOrNotNull() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get all the metasSaudeList where dataInicio is not null
        defaultMetasSaudeFiltering("dataInicio.specified=true", "dataInicio.specified=false");
    }

    @Test
    @Transactional
    void getAllMetasSaudesByDataFimIsEqualToSomething() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get all the metasSaudeList where dataFim equals to
        defaultMetasSaudeFiltering("dataFim.equals=" + DEFAULT_DATA_FIM, "dataFim.equals=" + UPDATED_DATA_FIM);
    }

    @Test
    @Transactional
    void getAllMetasSaudesByDataFimIsInShouldWork() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get all the metasSaudeList where dataFim in
        defaultMetasSaudeFiltering("dataFim.in=" + DEFAULT_DATA_FIM + "," + UPDATED_DATA_FIM, "dataFim.in=" + UPDATED_DATA_FIM);
    }

    @Test
    @Transactional
    void getAllMetasSaudesByDataFimIsNullOrNotNull() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        // Get all the metasSaudeList where dataFim is not null
        defaultMetasSaudeFiltering("dataFim.specified=true", "dataFim.specified=false");
    }

    @Test
    @Transactional
    void getAllMetasSaudesByInternalUserIsEqualToSomething() throws Exception {
        User internalUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            metasSaudeRepository.saveAndFlush(metasSaude);
            internalUser = UserResourceIT.createEntity(em);
        } else {
            internalUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(internalUser);
        em.flush();
        metasSaude.setInternalUser(internalUser);
        metasSaudeRepository.saveAndFlush(metasSaude);
        Long internalUserId = internalUser.getId();
        // Get all the metasSaudeList where internalUser equals to internalUserId
        defaultMetasSaudeShouldBeFound("internalUserId.equals=" + internalUserId);

        // Get all the metasSaudeList where internalUser equals to (internalUserId + 1)
        defaultMetasSaudeShouldNotBeFound("internalUserId.equals=" + (internalUserId + 1));
    }

    private void defaultMetasSaudeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMetasSaudeShouldBeFound(shouldBeFound);
        defaultMetasSaudeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMetasSaudeShouldBeFound(String filter) throws Exception {
        restMetasSaudeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metasSaude.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoMeta").value(hasItem(DEFAULT_TIPO_META.toString())))
            .andExpect(jsonPath("$.[*].valorMeta").value(hasItem(DEFAULT_VALOR_META)))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].dataFim").value(hasItem(DEFAULT_DATA_FIM.toString())));

        // Check, that the count call also returns 1
        restMetasSaudeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMetasSaudeShouldNotBeFound(String filter) throws Exception {
        restMetasSaudeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMetasSaudeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMetasSaude() throws Exception {
        // Get the metasSaude
        restMetasSaudeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMetasSaude() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metasSaude
        MetasSaude updatedMetasSaude = metasSaudeRepository.findById(metasSaude.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMetasSaude are not directly saved in db
        em.detach(updatedMetasSaude);
        updatedMetasSaude
            .tipoMeta(UPDATED_TIPO_META)
            .valorMeta(UPDATED_VALOR_META)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM);

        restMetasSaudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMetasSaude.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMetasSaude))
            )
            .andExpect(status().isOk());

        // Validate the MetasSaude in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMetasSaudeToMatchAllProperties(updatedMetasSaude);
    }

    @Test
    @Transactional
    void putNonExistingMetasSaude() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metasSaude.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetasSaudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metasSaude.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metasSaude))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetasSaude in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMetasSaude() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metasSaude.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetasSaudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metasSaude))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetasSaude in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMetasSaude() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metasSaude.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetasSaudeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metasSaude)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetasSaude in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMetasSaudeWithPatch() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metasSaude using partial update
        MetasSaude partialUpdatedMetasSaude = new MetasSaude();
        partialUpdatedMetasSaude.setId(metasSaude.getId());

        partialUpdatedMetasSaude.valorMeta(UPDATED_VALOR_META);

        restMetasSaudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetasSaude.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetasSaude))
            )
            .andExpect(status().isOk());

        // Validate the MetasSaude in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetasSaudeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMetasSaude, metasSaude),
            getPersistedMetasSaude(metasSaude)
        );
    }

    @Test
    @Transactional
    void fullUpdateMetasSaudeWithPatch() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metasSaude using partial update
        MetasSaude partialUpdatedMetasSaude = new MetasSaude();
        partialUpdatedMetasSaude.setId(metasSaude.getId());

        partialUpdatedMetasSaude
            .tipoMeta(UPDATED_TIPO_META)
            .valorMeta(UPDATED_VALOR_META)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM);

        restMetasSaudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetasSaude.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetasSaude))
            )
            .andExpect(status().isOk());

        // Validate the MetasSaude in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetasSaudeUpdatableFieldsEquals(partialUpdatedMetasSaude, getPersistedMetasSaude(partialUpdatedMetasSaude));
    }

    @Test
    @Transactional
    void patchNonExistingMetasSaude() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metasSaude.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetasSaudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, metasSaude.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metasSaude))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetasSaude in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMetasSaude() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metasSaude.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetasSaudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metasSaude))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetasSaude in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMetasSaude() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        metasSaude.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetasSaudeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(metasSaude)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetasSaude in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMetasSaude() throws Exception {
        // Initialize the database
        metasSaudeRepository.saveAndFlush(metasSaude);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the metasSaude
        restMetasSaudeMockMvc
            .perform(delete(ENTITY_API_URL_ID, metasSaude.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return metasSaudeRepository.count();
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

    protected MetasSaude getPersistedMetasSaude(MetasSaude metasSaude) {
        return metasSaudeRepository.findById(metasSaude.getId()).orElseThrow();
    }

    protected void assertPersistedMetasSaudeToMatchAllProperties(MetasSaude expectedMetasSaude) {
        assertMetasSaudeAllPropertiesEquals(expectedMetasSaude, getPersistedMetasSaude(expectedMetasSaude));
    }

    protected void assertPersistedMetasSaudeToMatchUpdatableProperties(MetasSaude expectedMetasSaude) {
        assertMetasSaudeAllUpdatablePropertiesEquals(expectedMetasSaude, getPersistedMetasSaude(expectedMetasSaude));
    }
}
