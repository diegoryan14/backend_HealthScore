package com.healthscore.app.web.rest;

import static com.healthscore.app.domain.ConsumoAguaAsserts.*;
import static com.healthscore.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthscore.app.IntegrationTest;
import com.healthscore.app.domain.ConsumoAgua;
import com.healthscore.app.domain.User;
import com.healthscore.app.repository.ConsumoAguaRepository;
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
 * Integration tests for the {@link ConsumoAguaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConsumoAguaResourceIT {

    private static final Instant DEFAULT_DATA_CONSUMO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_CONSUMO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_QUANTIDADE_ML = 1;
    private static final Integer UPDATED_QUANTIDADE_ML = 2;
    private static final Integer SMALLER_QUANTIDADE_ML = 1 - 1;

    private static final String ENTITY_API_URL = "/api/consumo-aguas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConsumoAguaRepository consumoAguaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsumoAguaMockMvc;

    private ConsumoAgua consumoAgua;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConsumoAgua createEntity(EntityManager em) {
        ConsumoAgua consumoAgua = new ConsumoAgua().dataConsumo(DEFAULT_DATA_CONSUMO).quantidadeMl(DEFAULT_QUANTIDADE_ML);
        return consumoAgua;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConsumoAgua createUpdatedEntity(EntityManager em) {
        ConsumoAgua consumoAgua = new ConsumoAgua().dataConsumo(UPDATED_DATA_CONSUMO).quantidadeMl(UPDATED_QUANTIDADE_ML);
        return consumoAgua;
    }

    @BeforeEach
    public void initTest() {
        consumoAgua = createEntity(em);
    }

    @Test
    @Transactional
    void createConsumoAgua() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ConsumoAgua
        var returnedConsumoAgua = om.readValue(
            restConsumoAguaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consumoAgua)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConsumoAgua.class
        );

        // Validate the ConsumoAgua in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertConsumoAguaUpdatableFieldsEquals(returnedConsumoAgua, getPersistedConsumoAgua(returnedConsumoAgua));
    }

    @Test
    @Transactional
    void createConsumoAguaWithExistingId() throws Exception {
        // Create the ConsumoAgua with an existing ID
        consumoAgua.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsumoAguaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consumoAgua)))
            .andExpect(status().isBadRequest());

        // Validate the ConsumoAgua in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConsumoAguas() throws Exception {
        // Initialize the database
        consumoAguaRepository.saveAndFlush(consumoAgua);

        // Get all the consumoAguaList
        restConsumoAguaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consumoAgua.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataConsumo").value(hasItem(DEFAULT_DATA_CONSUMO.toString())))
            .andExpect(jsonPath("$.[*].quantidadeMl").value(hasItem(DEFAULT_QUANTIDADE_ML)));
    }

    @Test
    @Transactional
    void getConsumoAgua() throws Exception {
        // Initialize the database
        consumoAguaRepository.saveAndFlush(consumoAgua);

        // Get the consumoAgua
        restConsumoAguaMockMvc
            .perform(get(ENTITY_API_URL_ID, consumoAgua.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consumoAgua.getId().intValue()))
            .andExpect(jsonPath("$.dataConsumo").value(DEFAULT_DATA_CONSUMO.toString()))
            .andExpect(jsonPath("$.quantidadeMl").value(DEFAULT_QUANTIDADE_ML));
    }

    @Test
    @Transactional
    void getConsumoAguasByIdFiltering() throws Exception {
        // Initialize the database
        consumoAguaRepository.saveAndFlush(consumoAgua);

        Long id = consumoAgua.getId();

        defaultConsumoAguaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultConsumoAguaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultConsumoAguaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConsumoAguasByDataConsumoIsEqualToSomething() throws Exception {
        // Initialize the database
        consumoAguaRepository.saveAndFlush(consumoAgua);

        // Get all the consumoAguaList where dataConsumo equals to
        defaultConsumoAguaFiltering("dataConsumo.equals=" + DEFAULT_DATA_CONSUMO, "dataConsumo.equals=" + UPDATED_DATA_CONSUMO);
    }

    @Test
    @Transactional
    void getAllConsumoAguasByDataConsumoIsInShouldWork() throws Exception {
        // Initialize the database
        consumoAguaRepository.saveAndFlush(consumoAgua);

        // Get all the consumoAguaList where dataConsumo in
        defaultConsumoAguaFiltering(
            "dataConsumo.in=" + DEFAULT_DATA_CONSUMO + "," + UPDATED_DATA_CONSUMO,
            "dataConsumo.in=" + UPDATED_DATA_CONSUMO
        );
    }

    @Test
    @Transactional
    void getAllConsumoAguasByDataConsumoIsNullOrNotNull() throws Exception {
        // Initialize the database
        consumoAguaRepository.saveAndFlush(consumoAgua);

        // Get all the consumoAguaList where dataConsumo is not null
        defaultConsumoAguaFiltering("dataConsumo.specified=true", "dataConsumo.specified=false");
    }

    @Test
    @Transactional
    void getAllConsumoAguasByQuantidadeMlIsEqualToSomething() throws Exception {
        // Initialize the database
        consumoAguaRepository.saveAndFlush(consumoAgua);

        // Get all the consumoAguaList where quantidadeMl equals to
        defaultConsumoAguaFiltering("quantidadeMl.equals=" + DEFAULT_QUANTIDADE_ML, "quantidadeMl.equals=" + UPDATED_QUANTIDADE_ML);
    }

    @Test
    @Transactional
    void getAllConsumoAguasByQuantidadeMlIsInShouldWork() throws Exception {
        // Initialize the database
        consumoAguaRepository.saveAndFlush(consumoAgua);

        // Get all the consumoAguaList where quantidadeMl in
        defaultConsumoAguaFiltering(
            "quantidadeMl.in=" + DEFAULT_QUANTIDADE_ML + "," + UPDATED_QUANTIDADE_ML,
            "quantidadeMl.in=" + UPDATED_QUANTIDADE_ML
        );
    }

    @Test
    @Transactional
    void getAllConsumoAguasByQuantidadeMlIsNullOrNotNull() throws Exception {
        // Initialize the database
        consumoAguaRepository.saveAndFlush(consumoAgua);

        // Get all the consumoAguaList where quantidadeMl is not null
        defaultConsumoAguaFiltering("quantidadeMl.specified=true", "quantidadeMl.specified=false");
    }

    @Test
    @Transactional
    void getAllConsumoAguasByQuantidadeMlIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consumoAguaRepository.saveAndFlush(consumoAgua);

        // Get all the consumoAguaList where quantidadeMl is greater than or equal to
        defaultConsumoAguaFiltering(
            "quantidadeMl.greaterThanOrEqual=" + DEFAULT_QUANTIDADE_ML,
            "quantidadeMl.greaterThanOrEqual=" + UPDATED_QUANTIDADE_ML
        );
    }

    @Test
    @Transactional
    void getAllConsumoAguasByQuantidadeMlIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        consumoAguaRepository.saveAndFlush(consumoAgua);

        // Get all the consumoAguaList where quantidadeMl is less than or equal to
        defaultConsumoAguaFiltering(
            "quantidadeMl.lessThanOrEqual=" + DEFAULT_QUANTIDADE_ML,
            "quantidadeMl.lessThanOrEqual=" + SMALLER_QUANTIDADE_ML
        );
    }

    @Test
    @Transactional
    void getAllConsumoAguasByQuantidadeMlIsLessThanSomething() throws Exception {
        // Initialize the database
        consumoAguaRepository.saveAndFlush(consumoAgua);

        // Get all the consumoAguaList where quantidadeMl is less than
        defaultConsumoAguaFiltering("quantidadeMl.lessThan=" + UPDATED_QUANTIDADE_ML, "quantidadeMl.lessThan=" + DEFAULT_QUANTIDADE_ML);
    }

    @Test
    @Transactional
    void getAllConsumoAguasByQuantidadeMlIsGreaterThanSomething() throws Exception {
        // Initialize the database
        consumoAguaRepository.saveAndFlush(consumoAgua);

        // Get all the consumoAguaList where quantidadeMl is greater than
        defaultConsumoAguaFiltering(
            "quantidadeMl.greaterThan=" + SMALLER_QUANTIDADE_ML,
            "quantidadeMl.greaterThan=" + DEFAULT_QUANTIDADE_ML
        );
    }

    @Test
    @Transactional
    void getAllConsumoAguasByInternalUserIsEqualToSomething() throws Exception {
        User internalUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            consumoAguaRepository.saveAndFlush(consumoAgua);
            internalUser = UserResourceIT.createEntity(em);
        } else {
            internalUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(internalUser);
        em.flush();
        consumoAgua.setInternalUser(internalUser);
        consumoAguaRepository.saveAndFlush(consumoAgua);
        Long internalUserId = internalUser.getId();
        // Get all the consumoAguaList where internalUser equals to internalUserId
        defaultConsumoAguaShouldBeFound("internalUserId.equals=" + internalUserId);

        // Get all the consumoAguaList where internalUser equals to (internalUserId + 1)
        defaultConsumoAguaShouldNotBeFound("internalUserId.equals=" + (internalUserId + 1));
    }

    private void defaultConsumoAguaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultConsumoAguaShouldBeFound(shouldBeFound);
        defaultConsumoAguaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConsumoAguaShouldBeFound(String filter) throws Exception {
        restConsumoAguaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consumoAgua.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataConsumo").value(hasItem(DEFAULT_DATA_CONSUMO.toString())))
            .andExpect(jsonPath("$.[*].quantidadeMl").value(hasItem(DEFAULT_QUANTIDADE_ML)));

        // Check, that the count call also returns 1
        restConsumoAguaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConsumoAguaShouldNotBeFound(String filter) throws Exception {
        restConsumoAguaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConsumoAguaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConsumoAgua() throws Exception {
        // Get the consumoAgua
        restConsumoAguaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConsumoAgua() throws Exception {
        // Initialize the database
        consumoAguaRepository.saveAndFlush(consumoAgua);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consumoAgua
        ConsumoAgua updatedConsumoAgua = consumoAguaRepository.findById(consumoAgua.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConsumoAgua are not directly saved in db
        em.detach(updatedConsumoAgua);
        updatedConsumoAgua.dataConsumo(UPDATED_DATA_CONSUMO).quantidadeMl(UPDATED_QUANTIDADE_ML);

        restConsumoAguaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConsumoAgua.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedConsumoAgua))
            )
            .andExpect(status().isOk());

        // Validate the ConsumoAgua in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConsumoAguaToMatchAllProperties(updatedConsumoAgua);
    }

    @Test
    @Transactional
    void putNonExistingConsumoAgua() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consumoAgua.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsumoAguaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consumoAgua.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consumoAgua))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumoAgua in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsumoAgua() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consumoAgua.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumoAguaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consumoAgua))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumoAgua in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsumoAgua() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consumoAgua.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumoAguaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consumoAgua)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConsumoAgua in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsumoAguaWithPatch() throws Exception {
        // Initialize the database
        consumoAguaRepository.saveAndFlush(consumoAgua);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consumoAgua using partial update
        ConsumoAgua partialUpdatedConsumoAgua = new ConsumoAgua();
        partialUpdatedConsumoAgua.setId(consumoAgua.getId());

        restConsumoAguaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsumoAgua.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConsumoAgua))
            )
            .andExpect(status().isOk());

        // Validate the ConsumoAgua in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConsumoAguaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedConsumoAgua, consumoAgua),
            getPersistedConsumoAgua(consumoAgua)
        );
    }

    @Test
    @Transactional
    void fullUpdateConsumoAguaWithPatch() throws Exception {
        // Initialize the database
        consumoAguaRepository.saveAndFlush(consumoAgua);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consumoAgua using partial update
        ConsumoAgua partialUpdatedConsumoAgua = new ConsumoAgua();
        partialUpdatedConsumoAgua.setId(consumoAgua.getId());

        partialUpdatedConsumoAgua.dataConsumo(UPDATED_DATA_CONSUMO).quantidadeMl(UPDATED_QUANTIDADE_ML);

        restConsumoAguaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsumoAgua.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConsumoAgua))
            )
            .andExpect(status().isOk());

        // Validate the ConsumoAgua in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConsumoAguaUpdatableFieldsEquals(partialUpdatedConsumoAgua, getPersistedConsumoAgua(partialUpdatedConsumoAgua));
    }

    @Test
    @Transactional
    void patchNonExistingConsumoAgua() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consumoAgua.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsumoAguaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consumoAgua.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(consumoAgua))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumoAgua in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsumoAgua() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consumoAgua.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumoAguaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(consumoAgua))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsumoAgua in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsumoAgua() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consumoAgua.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumoAguaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(consumoAgua)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConsumoAgua in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsumoAgua() throws Exception {
        // Initialize the database
        consumoAguaRepository.saveAndFlush(consumoAgua);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the consumoAgua
        restConsumoAguaMockMvc
            .perform(delete(ENTITY_API_URL_ID, consumoAgua.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return consumoAguaRepository.count();
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

    protected ConsumoAgua getPersistedConsumoAgua(ConsumoAgua consumoAgua) {
        return consumoAguaRepository.findById(consumoAgua.getId()).orElseThrow();
    }

    protected void assertPersistedConsumoAguaToMatchAllProperties(ConsumoAgua expectedConsumoAgua) {
        assertConsumoAguaAllPropertiesEquals(expectedConsumoAgua, getPersistedConsumoAgua(expectedConsumoAgua));
    }

    protected void assertPersistedConsumoAguaToMatchUpdatableProperties(ConsumoAgua expectedConsumoAgua) {
        assertConsumoAguaAllUpdatablePropertiesEquals(expectedConsumoAgua, getPersistedConsumoAgua(expectedConsumoAgua));
    }
}
