package com.healthscore.app.web.rest;

import static com.healthscore.app.domain.QualidadeSonoAsserts.*;
import static com.healthscore.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthscore.app.IntegrationTest;
import com.healthscore.app.domain.QualidadeSono;
import com.healthscore.app.domain.User;
import com.healthscore.app.repository.QualidadeSonoRepository;
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
 * Integration tests for the {@link QualidadeSonoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QualidadeSonoResourceIT {

    private static final Instant DEFAULT_DATA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_HORAS_SONO = 1;
    private static final Integer UPDATED_HORAS_SONO = 2;
    private static final Integer SMALLER_HORAS_SONO = 1 - 1;

    private static final String ENTITY_API_URL = "/api/qualidade-sonos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QualidadeSonoRepository qualidadeSonoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQualidadeSonoMockMvc;

    private QualidadeSono qualidadeSono;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QualidadeSono createEntity(EntityManager em) {
        QualidadeSono qualidadeSono = new QualidadeSono().data(DEFAULT_DATA).horasSono(DEFAULT_HORAS_SONO);
        return qualidadeSono;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QualidadeSono createUpdatedEntity(EntityManager em) {
        QualidadeSono qualidadeSono = new QualidadeSono().data(UPDATED_DATA).horasSono(UPDATED_HORAS_SONO);
        return qualidadeSono;
    }

    @BeforeEach
    public void initTest() {
        qualidadeSono = createEntity(em);
    }

    @Test
    @Transactional
    void createQualidadeSono() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the QualidadeSono
        var returnedQualidadeSono = om.readValue(
            restQualidadeSonoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(qualidadeSono)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QualidadeSono.class
        );

        // Validate the QualidadeSono in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertQualidadeSonoUpdatableFieldsEquals(returnedQualidadeSono, getPersistedQualidadeSono(returnedQualidadeSono));
    }

    @Test
    @Transactional
    void createQualidadeSonoWithExistingId() throws Exception {
        // Create the QualidadeSono with an existing ID
        qualidadeSono.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQualidadeSonoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(qualidadeSono)))
            .andExpect(status().isBadRequest());

        // Validate the QualidadeSono in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllQualidadeSonos() throws Exception {
        // Initialize the database
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);

        // Get all the qualidadeSonoList
        restQualidadeSonoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qualidadeSono.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].horasSono").value(hasItem(DEFAULT_HORAS_SONO)));
    }

    @Test
    @Transactional
    void getQualidadeSono() throws Exception {
        // Initialize the database
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);

        // Get the qualidadeSono
        restQualidadeSonoMockMvc
            .perform(get(ENTITY_API_URL_ID, qualidadeSono.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(qualidadeSono.getId().intValue()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.horasSono").value(DEFAULT_HORAS_SONO));
    }

    @Test
    @Transactional
    void getQualidadeSonosByIdFiltering() throws Exception {
        // Initialize the database
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);

        Long id = qualidadeSono.getId();

        defaultQualidadeSonoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultQualidadeSonoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultQualidadeSonoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllQualidadeSonosByDataIsEqualToSomething() throws Exception {
        // Initialize the database
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);

        // Get all the qualidadeSonoList where data equals to
        defaultQualidadeSonoFiltering("data.equals=" + DEFAULT_DATA, "data.equals=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllQualidadeSonosByDataIsInShouldWork() throws Exception {
        // Initialize the database
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);

        // Get all the qualidadeSonoList where data in
        defaultQualidadeSonoFiltering("data.in=" + DEFAULT_DATA + "," + UPDATED_DATA, "data.in=" + UPDATED_DATA);
    }

    @Test
    @Transactional
    void getAllQualidadeSonosByDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);

        // Get all the qualidadeSonoList where data is not null
        defaultQualidadeSonoFiltering("data.specified=true", "data.specified=false");
    }

    @Test
    @Transactional
    void getAllQualidadeSonosByHorasSonoIsEqualToSomething() throws Exception {
        // Initialize the database
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);

        // Get all the qualidadeSonoList where horasSono equals to
        defaultQualidadeSonoFiltering("horasSono.equals=" + DEFAULT_HORAS_SONO, "horasSono.equals=" + UPDATED_HORAS_SONO);
    }

    @Test
    @Transactional
    void getAllQualidadeSonosByHorasSonoIsInShouldWork() throws Exception {
        // Initialize the database
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);

        // Get all the qualidadeSonoList where horasSono in
        defaultQualidadeSonoFiltering(
            "horasSono.in=" + DEFAULT_HORAS_SONO + "," + UPDATED_HORAS_SONO,
            "horasSono.in=" + UPDATED_HORAS_SONO
        );
    }

    @Test
    @Transactional
    void getAllQualidadeSonosByHorasSonoIsNullOrNotNull() throws Exception {
        // Initialize the database
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);

        // Get all the qualidadeSonoList where horasSono is not null
        defaultQualidadeSonoFiltering("horasSono.specified=true", "horasSono.specified=false");
    }

    @Test
    @Transactional
    void getAllQualidadeSonosByHorasSonoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);

        // Get all the qualidadeSonoList where horasSono is greater than or equal to
        defaultQualidadeSonoFiltering(
            "horasSono.greaterThanOrEqual=" + DEFAULT_HORAS_SONO,
            "horasSono.greaterThanOrEqual=" + UPDATED_HORAS_SONO
        );
    }

    @Test
    @Transactional
    void getAllQualidadeSonosByHorasSonoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);

        // Get all the qualidadeSonoList where horasSono is less than or equal to
        defaultQualidadeSonoFiltering("horasSono.lessThanOrEqual=" + DEFAULT_HORAS_SONO, "horasSono.lessThanOrEqual=" + SMALLER_HORAS_SONO);
    }

    @Test
    @Transactional
    void getAllQualidadeSonosByHorasSonoIsLessThanSomething() throws Exception {
        // Initialize the database
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);

        // Get all the qualidadeSonoList where horasSono is less than
        defaultQualidadeSonoFiltering("horasSono.lessThan=" + UPDATED_HORAS_SONO, "horasSono.lessThan=" + DEFAULT_HORAS_SONO);
    }

    @Test
    @Transactional
    void getAllQualidadeSonosByHorasSonoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);

        // Get all the qualidadeSonoList where horasSono is greater than
        defaultQualidadeSonoFiltering("horasSono.greaterThan=" + SMALLER_HORAS_SONO, "horasSono.greaterThan=" + DEFAULT_HORAS_SONO);
    }

    @Test
    @Transactional
    void getAllQualidadeSonosByInternalUserIsEqualToSomething() throws Exception {
        User internalUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            qualidadeSonoRepository.saveAndFlush(qualidadeSono);
            internalUser = UserResourceIT.createEntity(em);
        } else {
            internalUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(internalUser);
        em.flush();
        qualidadeSono.setInternalUser(internalUser);
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);
        Long internalUserId = internalUser.getId();
        // Get all the qualidadeSonoList where internalUser equals to internalUserId
        defaultQualidadeSonoShouldBeFound("internalUserId.equals=" + internalUserId);

        // Get all the qualidadeSonoList where internalUser equals to (internalUserId + 1)
        defaultQualidadeSonoShouldNotBeFound("internalUserId.equals=" + (internalUserId + 1));
    }

    private void defaultQualidadeSonoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQualidadeSonoShouldBeFound(shouldBeFound);
        defaultQualidadeSonoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQualidadeSonoShouldBeFound(String filter) throws Exception {
        restQualidadeSonoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qualidadeSono.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].horasSono").value(hasItem(DEFAULT_HORAS_SONO)));

        // Check, that the count call also returns 1
        restQualidadeSonoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQualidadeSonoShouldNotBeFound(String filter) throws Exception {
        restQualidadeSonoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQualidadeSonoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQualidadeSono() throws Exception {
        // Get the qualidadeSono
        restQualidadeSonoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQualidadeSono() throws Exception {
        // Initialize the database
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the qualidadeSono
        QualidadeSono updatedQualidadeSono = qualidadeSonoRepository.findById(qualidadeSono.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQualidadeSono are not directly saved in db
        em.detach(updatedQualidadeSono);
        updatedQualidadeSono.data(UPDATED_DATA).horasSono(UPDATED_HORAS_SONO);

        restQualidadeSonoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedQualidadeSono.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedQualidadeSono))
            )
            .andExpect(status().isOk());

        // Validate the QualidadeSono in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQualidadeSonoToMatchAllProperties(updatedQualidadeSono);
    }

    @Test
    @Transactional
    void putNonExistingQualidadeSono() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qualidadeSono.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQualidadeSonoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, qualidadeSono.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(qualidadeSono))
            )
            .andExpect(status().isBadRequest());

        // Validate the QualidadeSono in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQualidadeSono() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qualidadeSono.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQualidadeSonoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(qualidadeSono))
            )
            .andExpect(status().isBadRequest());

        // Validate the QualidadeSono in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQualidadeSono() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qualidadeSono.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQualidadeSonoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(qualidadeSono)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QualidadeSono in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQualidadeSonoWithPatch() throws Exception {
        // Initialize the database
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the qualidadeSono using partial update
        QualidadeSono partialUpdatedQualidadeSono = new QualidadeSono();
        partialUpdatedQualidadeSono.setId(qualidadeSono.getId());

        partialUpdatedQualidadeSono.data(UPDATED_DATA);

        restQualidadeSonoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQualidadeSono.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQualidadeSono))
            )
            .andExpect(status().isOk());

        // Validate the QualidadeSono in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQualidadeSonoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedQualidadeSono, qualidadeSono),
            getPersistedQualidadeSono(qualidadeSono)
        );
    }

    @Test
    @Transactional
    void fullUpdateQualidadeSonoWithPatch() throws Exception {
        // Initialize the database
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the qualidadeSono using partial update
        QualidadeSono partialUpdatedQualidadeSono = new QualidadeSono();
        partialUpdatedQualidadeSono.setId(qualidadeSono.getId());

        partialUpdatedQualidadeSono.data(UPDATED_DATA).horasSono(UPDATED_HORAS_SONO);

        restQualidadeSonoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQualidadeSono.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQualidadeSono))
            )
            .andExpect(status().isOk());

        // Validate the QualidadeSono in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQualidadeSonoUpdatableFieldsEquals(partialUpdatedQualidadeSono, getPersistedQualidadeSono(partialUpdatedQualidadeSono));
    }

    @Test
    @Transactional
    void patchNonExistingQualidadeSono() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qualidadeSono.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQualidadeSonoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, qualidadeSono.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(qualidadeSono))
            )
            .andExpect(status().isBadRequest());

        // Validate the QualidadeSono in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQualidadeSono() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qualidadeSono.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQualidadeSonoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(qualidadeSono))
            )
            .andExpect(status().isBadRequest());

        // Validate the QualidadeSono in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQualidadeSono() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        qualidadeSono.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQualidadeSonoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(qualidadeSono)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the QualidadeSono in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQualidadeSono() throws Exception {
        // Initialize the database
        qualidadeSonoRepository.saveAndFlush(qualidadeSono);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the qualidadeSono
        restQualidadeSonoMockMvc
            .perform(delete(ENTITY_API_URL_ID, qualidadeSono.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return qualidadeSonoRepository.count();
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

    protected QualidadeSono getPersistedQualidadeSono(QualidadeSono qualidadeSono) {
        return qualidadeSonoRepository.findById(qualidadeSono.getId()).orElseThrow();
    }

    protected void assertPersistedQualidadeSonoToMatchAllProperties(QualidadeSono expectedQualidadeSono) {
        assertQualidadeSonoAllPropertiesEquals(expectedQualidadeSono, getPersistedQualidadeSono(expectedQualidadeSono));
    }

    protected void assertPersistedQualidadeSonoToMatchUpdatableProperties(QualidadeSono expectedQualidadeSono) {
        assertQualidadeSonoAllUpdatablePropertiesEquals(expectedQualidadeSono, getPersistedQualidadeSono(expectedQualidadeSono));
    }
}
