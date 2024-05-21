package com.healthscore.app.web.rest;

import static com.healthscore.app.domain.ControleMedicamentosAsserts.*;
import static com.healthscore.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthscore.app.IntegrationTest;
import com.healthscore.app.domain.ControleMedicamentos;
import com.healthscore.app.domain.User;
import com.healthscore.app.repository.ControleMedicamentosRepository;
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
 * Integration tests for the {@link ControleMedicamentosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ControleMedicamentosResourceIT {

    private static final String DEFAULT_NOME_MEDICAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_MEDICAMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_DOSAGEM = "AAAAAAAAAA";
    private static final String UPDATED_DOSAGEM = "BBBBBBBBBB";

    private static final Instant DEFAULT_HORARIO_INGESTAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HORARIO_INGESTAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/controle-medicamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ControleMedicamentosRepository controleMedicamentosRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restControleMedicamentosMockMvc;

    private ControleMedicamentos controleMedicamentos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ControleMedicamentos createEntity(EntityManager em) {
        ControleMedicamentos controleMedicamentos = new ControleMedicamentos()
            .nomeMedicamento(DEFAULT_NOME_MEDICAMENTO)
            .dosagem(DEFAULT_DOSAGEM)
            .horarioIngestao(DEFAULT_HORARIO_INGESTAO);
        return controleMedicamentos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ControleMedicamentos createUpdatedEntity(EntityManager em) {
        ControleMedicamentos controleMedicamentos = new ControleMedicamentos()
            .nomeMedicamento(UPDATED_NOME_MEDICAMENTO)
            .dosagem(UPDATED_DOSAGEM)
            .horarioIngestao(UPDATED_HORARIO_INGESTAO);
        return controleMedicamentos;
    }

    @BeforeEach
    public void initTest() {
        controleMedicamentos = createEntity(em);
    }

    @Test
    @Transactional
    void createControleMedicamentos() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ControleMedicamentos
        var returnedControleMedicamentos = om.readValue(
            restControleMedicamentosMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(controleMedicamentos)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ControleMedicamentos.class
        );

        // Validate the ControleMedicamentos in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertControleMedicamentosUpdatableFieldsEquals(
            returnedControleMedicamentos,
            getPersistedControleMedicamentos(returnedControleMedicamentos)
        );
    }

    @Test
    @Transactional
    void createControleMedicamentosWithExistingId() throws Exception {
        // Create the ControleMedicamentos with an existing ID
        controleMedicamentos.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restControleMedicamentosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(controleMedicamentos)))
            .andExpect(status().isBadRequest());

        // Validate the ControleMedicamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllControleMedicamentos() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        // Get all the controleMedicamentosList
        restControleMedicamentosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(controleMedicamentos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeMedicamento").value(hasItem(DEFAULT_NOME_MEDICAMENTO)))
            .andExpect(jsonPath("$.[*].dosagem").value(hasItem(DEFAULT_DOSAGEM)))
            .andExpect(jsonPath("$.[*].horarioIngestao").value(hasItem(DEFAULT_HORARIO_INGESTAO.toString())));
    }

    @Test
    @Transactional
    void getControleMedicamentos() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        // Get the controleMedicamentos
        restControleMedicamentosMockMvc
            .perform(get(ENTITY_API_URL_ID, controleMedicamentos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(controleMedicamentos.getId().intValue()))
            .andExpect(jsonPath("$.nomeMedicamento").value(DEFAULT_NOME_MEDICAMENTO))
            .andExpect(jsonPath("$.dosagem").value(DEFAULT_DOSAGEM))
            .andExpect(jsonPath("$.horarioIngestao").value(DEFAULT_HORARIO_INGESTAO.toString()));
    }

    @Test
    @Transactional
    void getControleMedicamentosByIdFiltering() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        Long id = controleMedicamentos.getId();

        defaultControleMedicamentosFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultControleMedicamentosFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultControleMedicamentosFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllControleMedicamentosByNomeMedicamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        // Get all the controleMedicamentosList where nomeMedicamento equals to
        defaultControleMedicamentosFiltering(
            "nomeMedicamento.equals=" + DEFAULT_NOME_MEDICAMENTO,
            "nomeMedicamento.equals=" + UPDATED_NOME_MEDICAMENTO
        );
    }

    @Test
    @Transactional
    void getAllControleMedicamentosByNomeMedicamentoIsInShouldWork() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        // Get all the controleMedicamentosList where nomeMedicamento in
        defaultControleMedicamentosFiltering(
            "nomeMedicamento.in=" + DEFAULT_NOME_MEDICAMENTO + "," + UPDATED_NOME_MEDICAMENTO,
            "nomeMedicamento.in=" + UPDATED_NOME_MEDICAMENTO
        );
    }

    @Test
    @Transactional
    void getAllControleMedicamentosByNomeMedicamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        // Get all the controleMedicamentosList where nomeMedicamento is not null
        defaultControleMedicamentosFiltering("nomeMedicamento.specified=true", "nomeMedicamento.specified=false");
    }

    @Test
    @Transactional
    void getAllControleMedicamentosByNomeMedicamentoContainsSomething() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        // Get all the controleMedicamentosList where nomeMedicamento contains
        defaultControleMedicamentosFiltering(
            "nomeMedicamento.contains=" + DEFAULT_NOME_MEDICAMENTO,
            "nomeMedicamento.contains=" + UPDATED_NOME_MEDICAMENTO
        );
    }

    @Test
    @Transactional
    void getAllControleMedicamentosByNomeMedicamentoNotContainsSomething() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        // Get all the controleMedicamentosList where nomeMedicamento does not contain
        defaultControleMedicamentosFiltering(
            "nomeMedicamento.doesNotContain=" + UPDATED_NOME_MEDICAMENTO,
            "nomeMedicamento.doesNotContain=" + DEFAULT_NOME_MEDICAMENTO
        );
    }

    @Test
    @Transactional
    void getAllControleMedicamentosByDosagemIsEqualToSomething() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        // Get all the controleMedicamentosList where dosagem equals to
        defaultControleMedicamentosFiltering("dosagem.equals=" + DEFAULT_DOSAGEM, "dosagem.equals=" + UPDATED_DOSAGEM);
    }

    @Test
    @Transactional
    void getAllControleMedicamentosByDosagemIsInShouldWork() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        // Get all the controleMedicamentosList where dosagem in
        defaultControleMedicamentosFiltering("dosagem.in=" + DEFAULT_DOSAGEM + "," + UPDATED_DOSAGEM, "dosagem.in=" + UPDATED_DOSAGEM);
    }

    @Test
    @Transactional
    void getAllControleMedicamentosByDosagemIsNullOrNotNull() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        // Get all the controleMedicamentosList where dosagem is not null
        defaultControleMedicamentosFiltering("dosagem.specified=true", "dosagem.specified=false");
    }

    @Test
    @Transactional
    void getAllControleMedicamentosByDosagemContainsSomething() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        // Get all the controleMedicamentosList where dosagem contains
        defaultControleMedicamentosFiltering("dosagem.contains=" + DEFAULT_DOSAGEM, "dosagem.contains=" + UPDATED_DOSAGEM);
    }

    @Test
    @Transactional
    void getAllControleMedicamentosByDosagemNotContainsSomething() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        // Get all the controleMedicamentosList where dosagem does not contain
        defaultControleMedicamentosFiltering("dosagem.doesNotContain=" + UPDATED_DOSAGEM, "dosagem.doesNotContain=" + DEFAULT_DOSAGEM);
    }

    @Test
    @Transactional
    void getAllControleMedicamentosByHorarioIngestaoIsEqualToSomething() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        // Get all the controleMedicamentosList where horarioIngestao equals to
        defaultControleMedicamentosFiltering(
            "horarioIngestao.equals=" + DEFAULT_HORARIO_INGESTAO,
            "horarioIngestao.equals=" + UPDATED_HORARIO_INGESTAO
        );
    }

    @Test
    @Transactional
    void getAllControleMedicamentosByHorarioIngestaoIsInShouldWork() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        // Get all the controleMedicamentosList where horarioIngestao in
        defaultControleMedicamentosFiltering(
            "horarioIngestao.in=" + DEFAULT_HORARIO_INGESTAO + "," + UPDATED_HORARIO_INGESTAO,
            "horarioIngestao.in=" + UPDATED_HORARIO_INGESTAO
        );
    }

    @Test
    @Transactional
    void getAllControleMedicamentosByHorarioIngestaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        // Get all the controleMedicamentosList where horarioIngestao is not null
        defaultControleMedicamentosFiltering("horarioIngestao.specified=true", "horarioIngestao.specified=false");
    }

    @Test
    @Transactional
    void getAllControleMedicamentosByInternalUserIsEqualToSomething() throws Exception {
        User internalUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            controleMedicamentosRepository.saveAndFlush(controleMedicamentos);
            internalUser = UserResourceIT.createEntity(em);
        } else {
            internalUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(internalUser);
        em.flush();
        controleMedicamentos.setInternalUser(internalUser);
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);
        Long internalUserId = internalUser.getId();
        // Get all the controleMedicamentosList where internalUser equals to internalUserId
        defaultControleMedicamentosShouldBeFound("internalUserId.equals=" + internalUserId);

        // Get all the controleMedicamentosList where internalUser equals to (internalUserId + 1)
        defaultControleMedicamentosShouldNotBeFound("internalUserId.equals=" + (internalUserId + 1));
    }

    private void defaultControleMedicamentosFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultControleMedicamentosShouldBeFound(shouldBeFound);
        defaultControleMedicamentosShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultControleMedicamentosShouldBeFound(String filter) throws Exception {
        restControleMedicamentosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(controleMedicamentos.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeMedicamento").value(hasItem(DEFAULT_NOME_MEDICAMENTO)))
            .andExpect(jsonPath("$.[*].dosagem").value(hasItem(DEFAULT_DOSAGEM)))
            .andExpect(jsonPath("$.[*].horarioIngestao").value(hasItem(DEFAULT_HORARIO_INGESTAO.toString())));

        // Check, that the count call also returns 1
        restControleMedicamentosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultControleMedicamentosShouldNotBeFound(String filter) throws Exception {
        restControleMedicamentosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restControleMedicamentosMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingControleMedicamentos() throws Exception {
        // Get the controleMedicamentos
        restControleMedicamentosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingControleMedicamentos() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the controleMedicamentos
        ControleMedicamentos updatedControleMedicamentos = controleMedicamentosRepository
            .findById(controleMedicamentos.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedControleMedicamentos are not directly saved in db
        em.detach(updatedControleMedicamentos);
        updatedControleMedicamentos
            .nomeMedicamento(UPDATED_NOME_MEDICAMENTO)
            .dosagem(UPDATED_DOSAGEM)
            .horarioIngestao(UPDATED_HORARIO_INGESTAO);

        restControleMedicamentosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedControleMedicamentos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedControleMedicamentos))
            )
            .andExpect(status().isOk());

        // Validate the ControleMedicamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedControleMedicamentosToMatchAllProperties(updatedControleMedicamentos);
    }

    @Test
    @Transactional
    void putNonExistingControleMedicamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        controleMedicamentos.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restControleMedicamentosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, controleMedicamentos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(controleMedicamentos))
            )
            .andExpect(status().isBadRequest());

        // Validate the ControleMedicamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchControleMedicamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        controleMedicamentos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restControleMedicamentosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(controleMedicamentos))
            )
            .andExpect(status().isBadRequest());

        // Validate the ControleMedicamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamControleMedicamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        controleMedicamentos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restControleMedicamentosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(controleMedicamentos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ControleMedicamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateControleMedicamentosWithPatch() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the controleMedicamentos using partial update
        ControleMedicamentos partialUpdatedControleMedicamentos = new ControleMedicamentos();
        partialUpdatedControleMedicamentos.setId(controleMedicamentos.getId());

        restControleMedicamentosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedControleMedicamentos.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedControleMedicamentos))
            )
            .andExpect(status().isOk());

        // Validate the ControleMedicamentos in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertControleMedicamentosUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedControleMedicamentos, controleMedicamentos),
            getPersistedControleMedicamentos(controleMedicamentos)
        );
    }

    @Test
    @Transactional
    void fullUpdateControleMedicamentosWithPatch() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the controleMedicamentos using partial update
        ControleMedicamentos partialUpdatedControleMedicamentos = new ControleMedicamentos();
        partialUpdatedControleMedicamentos.setId(controleMedicamentos.getId());

        partialUpdatedControleMedicamentos
            .nomeMedicamento(UPDATED_NOME_MEDICAMENTO)
            .dosagem(UPDATED_DOSAGEM)
            .horarioIngestao(UPDATED_HORARIO_INGESTAO);

        restControleMedicamentosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedControleMedicamentos.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedControleMedicamentos))
            )
            .andExpect(status().isOk());

        // Validate the ControleMedicamentos in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertControleMedicamentosUpdatableFieldsEquals(
            partialUpdatedControleMedicamentos,
            getPersistedControleMedicamentos(partialUpdatedControleMedicamentos)
        );
    }

    @Test
    @Transactional
    void patchNonExistingControleMedicamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        controleMedicamentos.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restControleMedicamentosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, controleMedicamentos.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(controleMedicamentos))
            )
            .andExpect(status().isBadRequest());

        // Validate the ControleMedicamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchControleMedicamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        controleMedicamentos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restControleMedicamentosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(controleMedicamentos))
            )
            .andExpect(status().isBadRequest());

        // Validate the ControleMedicamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamControleMedicamentos() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        controleMedicamentos.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restControleMedicamentosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(controleMedicamentos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ControleMedicamentos in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteControleMedicamentos() throws Exception {
        // Initialize the database
        controleMedicamentosRepository.saveAndFlush(controleMedicamentos);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the controleMedicamentos
        restControleMedicamentosMockMvc
            .perform(delete(ENTITY_API_URL_ID, controleMedicamentos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return controleMedicamentosRepository.count();
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

    protected ControleMedicamentos getPersistedControleMedicamentos(ControleMedicamentos controleMedicamentos) {
        return controleMedicamentosRepository.findById(controleMedicamentos.getId()).orElseThrow();
    }

    protected void assertPersistedControleMedicamentosToMatchAllProperties(ControleMedicamentos expectedControleMedicamentos) {
        assertControleMedicamentosAllPropertiesEquals(
            expectedControleMedicamentos,
            getPersistedControleMedicamentos(expectedControleMedicamentos)
        );
    }

    protected void assertPersistedControleMedicamentosToMatchUpdatableProperties(ControleMedicamentos expectedControleMedicamentos) {
        assertControleMedicamentosAllUpdatablePropertiesEquals(
            expectedControleMedicamentos,
            getPersistedControleMedicamentos(expectedControleMedicamentos)
        );
    }
}
