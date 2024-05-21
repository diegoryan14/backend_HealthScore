package com.healthscore.app.web.rest;

import static com.healthscore.app.domain.DietaAsserts.*;
import static com.healthscore.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthscore.app.IntegrationTest;
import com.healthscore.app.domain.Dieta;
import com.healthscore.app.domain.User;
import com.healthscore.app.repository.DietaRepository;
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
 * Integration tests for the {@link DietaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DietaResourceIT {

    private static final String DEFAULT_DESCRICAO_REFEICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO_REFEICAO = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATA_HORARIO_REFEICAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_HORARIO_REFEICAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_CALORIAS_CONSUMIDAS = 1;
    private static final Integer UPDATED_CALORIAS_CONSUMIDAS = 2;
    private static final Integer SMALLER_CALORIAS_CONSUMIDAS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/dietas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DietaRepository dietaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDietaMockMvc;

    private Dieta dieta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dieta createEntity(EntityManager em) {
        Dieta dieta = new Dieta()
            .descricaoRefeicao(DEFAULT_DESCRICAO_REFEICAO)
            .dataHorarioRefeicao(DEFAULT_DATA_HORARIO_REFEICAO)
            .caloriasConsumidas(DEFAULT_CALORIAS_CONSUMIDAS);
        return dieta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dieta createUpdatedEntity(EntityManager em) {
        Dieta dieta = new Dieta()
            .descricaoRefeicao(UPDATED_DESCRICAO_REFEICAO)
            .dataHorarioRefeicao(UPDATED_DATA_HORARIO_REFEICAO)
            .caloriasConsumidas(UPDATED_CALORIAS_CONSUMIDAS);
        return dieta;
    }

    @BeforeEach
    public void initTest() {
        dieta = createEntity(em);
    }

    @Test
    @Transactional
    void createDieta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Dieta
        var returnedDieta = om.readValue(
            restDietaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dieta)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Dieta.class
        );

        // Validate the Dieta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDietaUpdatableFieldsEquals(returnedDieta, getPersistedDieta(returnedDieta));
    }

    @Test
    @Transactional
    void createDietaWithExistingId() throws Exception {
        // Create the Dieta with an existing ID
        dieta.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDietaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dieta)))
            .andExpect(status().isBadRequest());

        // Validate the Dieta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDietas() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList
        restDietaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dieta.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricaoRefeicao").value(hasItem(DEFAULT_DESCRICAO_REFEICAO)))
            .andExpect(jsonPath("$.[*].dataHorarioRefeicao").value(hasItem(DEFAULT_DATA_HORARIO_REFEICAO.toString())))
            .andExpect(jsonPath("$.[*].caloriasConsumidas").value(hasItem(DEFAULT_CALORIAS_CONSUMIDAS)));
    }

    @Test
    @Transactional
    void getDieta() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        // Get the dieta
        restDietaMockMvc
            .perform(get(ENTITY_API_URL_ID, dieta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dieta.getId().intValue()))
            .andExpect(jsonPath("$.descricaoRefeicao").value(DEFAULT_DESCRICAO_REFEICAO))
            .andExpect(jsonPath("$.dataHorarioRefeicao").value(DEFAULT_DATA_HORARIO_REFEICAO.toString()))
            .andExpect(jsonPath("$.caloriasConsumidas").value(DEFAULT_CALORIAS_CONSUMIDAS));
    }

    @Test
    @Transactional
    void getDietasByIdFiltering() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        Long id = dieta.getId();

        defaultDietaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDietaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDietaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDietasByDescricaoRefeicaoIsEqualToSomething() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where descricaoRefeicao equals to
        defaultDietaFiltering(
            "descricaoRefeicao.equals=" + DEFAULT_DESCRICAO_REFEICAO,
            "descricaoRefeicao.equals=" + UPDATED_DESCRICAO_REFEICAO
        );
    }

    @Test
    @Transactional
    void getAllDietasByDescricaoRefeicaoIsInShouldWork() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where descricaoRefeicao in
        defaultDietaFiltering(
            "descricaoRefeicao.in=" + DEFAULT_DESCRICAO_REFEICAO + "," + UPDATED_DESCRICAO_REFEICAO,
            "descricaoRefeicao.in=" + UPDATED_DESCRICAO_REFEICAO
        );
    }

    @Test
    @Transactional
    void getAllDietasByDescricaoRefeicaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where descricaoRefeicao is not null
        defaultDietaFiltering("descricaoRefeicao.specified=true", "descricaoRefeicao.specified=false");
    }

    @Test
    @Transactional
    void getAllDietasByDescricaoRefeicaoContainsSomething() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where descricaoRefeicao contains
        defaultDietaFiltering(
            "descricaoRefeicao.contains=" + DEFAULT_DESCRICAO_REFEICAO,
            "descricaoRefeicao.contains=" + UPDATED_DESCRICAO_REFEICAO
        );
    }

    @Test
    @Transactional
    void getAllDietasByDescricaoRefeicaoNotContainsSomething() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where descricaoRefeicao does not contain
        defaultDietaFiltering(
            "descricaoRefeicao.doesNotContain=" + UPDATED_DESCRICAO_REFEICAO,
            "descricaoRefeicao.doesNotContain=" + DEFAULT_DESCRICAO_REFEICAO
        );
    }

    @Test
    @Transactional
    void getAllDietasByDataHorarioRefeicaoIsEqualToSomething() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where dataHorarioRefeicao equals to
        defaultDietaFiltering(
            "dataHorarioRefeicao.equals=" + DEFAULT_DATA_HORARIO_REFEICAO,
            "dataHorarioRefeicao.equals=" + UPDATED_DATA_HORARIO_REFEICAO
        );
    }

    @Test
    @Transactional
    void getAllDietasByDataHorarioRefeicaoIsInShouldWork() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where dataHorarioRefeicao in
        defaultDietaFiltering(
            "dataHorarioRefeicao.in=" + DEFAULT_DATA_HORARIO_REFEICAO + "," + UPDATED_DATA_HORARIO_REFEICAO,
            "dataHorarioRefeicao.in=" + UPDATED_DATA_HORARIO_REFEICAO
        );
    }

    @Test
    @Transactional
    void getAllDietasByDataHorarioRefeicaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where dataHorarioRefeicao is not null
        defaultDietaFiltering("dataHorarioRefeicao.specified=true", "dataHorarioRefeicao.specified=false");
    }

    @Test
    @Transactional
    void getAllDietasByCaloriasConsumidasIsEqualToSomething() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where caloriasConsumidas equals to
        defaultDietaFiltering(
            "caloriasConsumidas.equals=" + DEFAULT_CALORIAS_CONSUMIDAS,
            "caloriasConsumidas.equals=" + UPDATED_CALORIAS_CONSUMIDAS
        );
    }

    @Test
    @Transactional
    void getAllDietasByCaloriasConsumidasIsInShouldWork() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where caloriasConsumidas in
        defaultDietaFiltering(
            "caloriasConsumidas.in=" + DEFAULT_CALORIAS_CONSUMIDAS + "," + UPDATED_CALORIAS_CONSUMIDAS,
            "caloriasConsumidas.in=" + UPDATED_CALORIAS_CONSUMIDAS
        );
    }

    @Test
    @Transactional
    void getAllDietasByCaloriasConsumidasIsNullOrNotNull() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where caloriasConsumidas is not null
        defaultDietaFiltering("caloriasConsumidas.specified=true", "caloriasConsumidas.specified=false");
    }

    @Test
    @Transactional
    void getAllDietasByCaloriasConsumidasIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where caloriasConsumidas is greater than or equal to
        defaultDietaFiltering(
            "caloriasConsumidas.greaterThanOrEqual=" + DEFAULT_CALORIAS_CONSUMIDAS,
            "caloriasConsumidas.greaterThanOrEqual=" + UPDATED_CALORIAS_CONSUMIDAS
        );
    }

    @Test
    @Transactional
    void getAllDietasByCaloriasConsumidasIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where caloriasConsumidas is less than or equal to
        defaultDietaFiltering(
            "caloriasConsumidas.lessThanOrEqual=" + DEFAULT_CALORIAS_CONSUMIDAS,
            "caloriasConsumidas.lessThanOrEqual=" + SMALLER_CALORIAS_CONSUMIDAS
        );
    }

    @Test
    @Transactional
    void getAllDietasByCaloriasConsumidasIsLessThanSomething() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where caloriasConsumidas is less than
        defaultDietaFiltering(
            "caloriasConsumidas.lessThan=" + UPDATED_CALORIAS_CONSUMIDAS,
            "caloriasConsumidas.lessThan=" + DEFAULT_CALORIAS_CONSUMIDAS
        );
    }

    @Test
    @Transactional
    void getAllDietasByCaloriasConsumidasIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        // Get all the dietaList where caloriasConsumidas is greater than
        defaultDietaFiltering(
            "caloriasConsumidas.greaterThan=" + SMALLER_CALORIAS_CONSUMIDAS,
            "caloriasConsumidas.greaterThan=" + DEFAULT_CALORIAS_CONSUMIDAS
        );
    }

    @Test
    @Transactional
    void getAllDietasByInternalUserIsEqualToSomething() throws Exception {
        User internalUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            dietaRepository.saveAndFlush(dieta);
            internalUser = UserResourceIT.createEntity(em);
        } else {
            internalUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(internalUser);
        em.flush();
        dieta.setInternalUser(internalUser);
        dietaRepository.saveAndFlush(dieta);
        Long internalUserId = internalUser.getId();
        // Get all the dietaList where internalUser equals to internalUserId
        defaultDietaShouldBeFound("internalUserId.equals=" + internalUserId);

        // Get all the dietaList where internalUser equals to (internalUserId + 1)
        defaultDietaShouldNotBeFound("internalUserId.equals=" + (internalUserId + 1));
    }

    private void defaultDietaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDietaShouldBeFound(shouldBeFound);
        defaultDietaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDietaShouldBeFound(String filter) throws Exception {
        restDietaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dieta.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricaoRefeicao").value(hasItem(DEFAULT_DESCRICAO_REFEICAO)))
            .andExpect(jsonPath("$.[*].dataHorarioRefeicao").value(hasItem(DEFAULT_DATA_HORARIO_REFEICAO.toString())))
            .andExpect(jsonPath("$.[*].caloriasConsumidas").value(hasItem(DEFAULT_CALORIAS_CONSUMIDAS)));

        // Check, that the count call also returns 1
        restDietaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDietaShouldNotBeFound(String filter) throws Exception {
        restDietaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDietaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDieta() throws Exception {
        // Get the dieta
        restDietaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDieta() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dieta
        Dieta updatedDieta = dietaRepository.findById(dieta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDieta are not directly saved in db
        em.detach(updatedDieta);
        updatedDieta
            .descricaoRefeicao(UPDATED_DESCRICAO_REFEICAO)
            .dataHorarioRefeicao(UPDATED_DATA_HORARIO_REFEICAO)
            .caloriasConsumidas(UPDATED_CALORIAS_CONSUMIDAS);

        restDietaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDieta.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDieta))
            )
            .andExpect(status().isOk());

        // Validate the Dieta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDietaToMatchAllProperties(updatedDieta);
    }

    @Test
    @Transactional
    void putNonExistingDieta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dieta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDietaMockMvc
            .perform(put(ENTITY_API_URL_ID, dieta.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dieta)))
            .andExpect(status().isBadRequest());

        // Validate the Dieta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDieta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dieta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDietaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dieta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dieta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDieta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dieta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDietaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dieta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dieta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDietaWithPatch() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dieta using partial update
        Dieta partialUpdatedDieta = new Dieta();
        partialUpdatedDieta.setId(dieta.getId());

        partialUpdatedDieta.descricaoRefeicao(UPDATED_DESCRICAO_REFEICAO);

        restDietaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDieta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDieta))
            )
            .andExpect(status().isOk());

        // Validate the Dieta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDietaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDieta, dieta), getPersistedDieta(dieta));
    }

    @Test
    @Transactional
    void fullUpdateDietaWithPatch() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dieta using partial update
        Dieta partialUpdatedDieta = new Dieta();
        partialUpdatedDieta.setId(dieta.getId());

        partialUpdatedDieta
            .descricaoRefeicao(UPDATED_DESCRICAO_REFEICAO)
            .dataHorarioRefeicao(UPDATED_DATA_HORARIO_REFEICAO)
            .caloriasConsumidas(UPDATED_CALORIAS_CONSUMIDAS);

        restDietaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDieta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDieta))
            )
            .andExpect(status().isOk());

        // Validate the Dieta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDietaUpdatableFieldsEquals(partialUpdatedDieta, getPersistedDieta(partialUpdatedDieta));
    }

    @Test
    @Transactional
    void patchNonExistingDieta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dieta.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDietaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dieta.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dieta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dieta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDieta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dieta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDietaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dieta))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dieta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDieta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dieta.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDietaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dieta)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dieta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDieta() throws Exception {
        // Initialize the database
        dietaRepository.saveAndFlush(dieta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dieta
        restDietaMockMvc
            .perform(delete(ENTITY_API_URL_ID, dieta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dietaRepository.count();
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

    protected Dieta getPersistedDieta(Dieta dieta) {
        return dietaRepository.findById(dieta.getId()).orElseThrow();
    }

    protected void assertPersistedDietaToMatchAllProperties(Dieta expectedDieta) {
        assertDietaAllPropertiesEquals(expectedDieta, getPersistedDieta(expectedDieta));
    }

    protected void assertPersistedDietaToMatchUpdatableProperties(Dieta expectedDieta) {
        assertDietaAllUpdatablePropertiesEquals(expectedDieta, getPersistedDieta(expectedDieta));
    }
}
