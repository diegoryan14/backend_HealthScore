package com.healthscore.app.web.rest;

import static com.healthscore.app.domain.ConsultaEspecialistaAsserts.*;
import static com.healthscore.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthscore.app.IntegrationTest;
import com.healthscore.app.domain.ConsultaEspecialista;
import com.healthscore.app.domain.Especialista;
import com.healthscore.app.domain.User;
import com.healthscore.app.domain.enumeration.StatusConsulta;
import com.healthscore.app.domain.enumeration.TipoEspecialista;
import com.healthscore.app.repository.ConsultaEspecialistaRepository;
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
 * Integration tests for the {@link ConsultaEspecialistaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConsultaEspecialistaResourceIT {

    private static final TipoEspecialista DEFAULT_TIPO_ESPECIALISTA = TipoEspecialista.MEDICO;
    private static final TipoEspecialista UPDATED_TIPO_ESPECIALISTA = TipoEspecialista.PSICOLOGO;

    private static final Instant DEFAULT_DATA_HORARIO_CONSULTA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_HORARIO_CONSULTA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final StatusConsulta DEFAULT_STATUS_CONSULTA = StatusConsulta.PENDENTE;
    private static final StatusConsulta UPDATED_STATUS_CONSULTA = StatusConsulta.CONFIRMADA;

    private static final String DEFAULT_LINK_CONSULTA = "AAAAAAAAAA";
    private static final String UPDATED_LINK_CONSULTA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/consulta-especialistas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConsultaEspecialistaRepository consultaEspecialistaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsultaEspecialistaMockMvc;

    private ConsultaEspecialista consultaEspecialista;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConsultaEspecialista createEntity(EntityManager em) {
        ConsultaEspecialista consultaEspecialista = new ConsultaEspecialista()
            .tipoEspecialista(DEFAULT_TIPO_ESPECIALISTA)
            .dataHorarioConsulta(DEFAULT_DATA_HORARIO_CONSULTA)
            .statusConsulta(DEFAULT_STATUS_CONSULTA)
            .linkConsulta(DEFAULT_LINK_CONSULTA);
        return consultaEspecialista;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConsultaEspecialista createUpdatedEntity(EntityManager em) {
        ConsultaEspecialista consultaEspecialista = new ConsultaEspecialista()
            .tipoEspecialista(UPDATED_TIPO_ESPECIALISTA)
            .dataHorarioConsulta(UPDATED_DATA_HORARIO_CONSULTA)
            .statusConsulta(UPDATED_STATUS_CONSULTA)
            .linkConsulta(UPDATED_LINK_CONSULTA);
        return consultaEspecialista;
    }

    @BeforeEach
    public void initTest() {
        consultaEspecialista = createEntity(em);
    }

    @Test
    @Transactional
    void createConsultaEspecialista() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ConsultaEspecialista
        var returnedConsultaEspecialista = om.readValue(
            restConsultaEspecialistaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consultaEspecialista)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConsultaEspecialista.class
        );

        // Validate the ConsultaEspecialista in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertConsultaEspecialistaUpdatableFieldsEquals(
            returnedConsultaEspecialista,
            getPersistedConsultaEspecialista(returnedConsultaEspecialista)
        );
    }

    @Test
    @Transactional
    void createConsultaEspecialistaWithExistingId() throws Exception {
        // Create the ConsultaEspecialista with an existing ID
        consultaEspecialista.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsultaEspecialistaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consultaEspecialista)))
            .andExpect(status().isBadRequest());

        // Validate the ConsultaEspecialista in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConsultaEspecialistas() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        // Get all the consultaEspecialistaList
        restConsultaEspecialistaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consultaEspecialista.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoEspecialista").value(hasItem(DEFAULT_TIPO_ESPECIALISTA.toString())))
            .andExpect(jsonPath("$.[*].dataHorarioConsulta").value(hasItem(DEFAULT_DATA_HORARIO_CONSULTA.toString())))
            .andExpect(jsonPath("$.[*].statusConsulta").value(hasItem(DEFAULT_STATUS_CONSULTA.toString())))
            .andExpect(jsonPath("$.[*].linkConsulta").value(hasItem(DEFAULT_LINK_CONSULTA)));
    }

    @Test
    @Transactional
    void getConsultaEspecialista() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        // Get the consultaEspecialista
        restConsultaEspecialistaMockMvc
            .perform(get(ENTITY_API_URL_ID, consultaEspecialista.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consultaEspecialista.getId().intValue()))
            .andExpect(jsonPath("$.tipoEspecialista").value(DEFAULT_TIPO_ESPECIALISTA.toString()))
            .andExpect(jsonPath("$.dataHorarioConsulta").value(DEFAULT_DATA_HORARIO_CONSULTA.toString()))
            .andExpect(jsonPath("$.statusConsulta").value(DEFAULT_STATUS_CONSULTA.toString()))
            .andExpect(jsonPath("$.linkConsulta").value(DEFAULT_LINK_CONSULTA));
    }

    @Test
    @Transactional
    void getConsultaEspecialistasByIdFiltering() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        Long id = consultaEspecialista.getId();

        defaultConsultaEspecialistaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultConsultaEspecialistaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultConsultaEspecialistaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConsultaEspecialistasByTipoEspecialistaIsEqualToSomething() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        // Get all the consultaEspecialistaList where tipoEspecialista equals to
        defaultConsultaEspecialistaFiltering(
            "tipoEspecialista.equals=" + DEFAULT_TIPO_ESPECIALISTA,
            "tipoEspecialista.equals=" + UPDATED_TIPO_ESPECIALISTA
        );
    }

    @Test
    @Transactional
    void getAllConsultaEspecialistasByTipoEspecialistaIsInShouldWork() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        // Get all the consultaEspecialistaList where tipoEspecialista in
        defaultConsultaEspecialistaFiltering(
            "tipoEspecialista.in=" + DEFAULT_TIPO_ESPECIALISTA + "," + UPDATED_TIPO_ESPECIALISTA,
            "tipoEspecialista.in=" + UPDATED_TIPO_ESPECIALISTA
        );
    }

    @Test
    @Transactional
    void getAllConsultaEspecialistasByTipoEspecialistaIsNullOrNotNull() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        // Get all the consultaEspecialistaList where tipoEspecialista is not null
        defaultConsultaEspecialistaFiltering("tipoEspecialista.specified=true", "tipoEspecialista.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultaEspecialistasByDataHorarioConsultaIsEqualToSomething() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        // Get all the consultaEspecialistaList where dataHorarioConsulta equals to
        defaultConsultaEspecialistaFiltering(
            "dataHorarioConsulta.equals=" + DEFAULT_DATA_HORARIO_CONSULTA,
            "dataHorarioConsulta.equals=" + UPDATED_DATA_HORARIO_CONSULTA
        );
    }

    @Test
    @Transactional
    void getAllConsultaEspecialistasByDataHorarioConsultaIsInShouldWork() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        // Get all the consultaEspecialistaList where dataHorarioConsulta in
        defaultConsultaEspecialistaFiltering(
            "dataHorarioConsulta.in=" + DEFAULT_DATA_HORARIO_CONSULTA + "," + UPDATED_DATA_HORARIO_CONSULTA,
            "dataHorarioConsulta.in=" + UPDATED_DATA_HORARIO_CONSULTA
        );
    }

    @Test
    @Transactional
    void getAllConsultaEspecialistasByDataHorarioConsultaIsNullOrNotNull() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        // Get all the consultaEspecialistaList where dataHorarioConsulta is not null
        defaultConsultaEspecialistaFiltering("dataHorarioConsulta.specified=true", "dataHorarioConsulta.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultaEspecialistasByStatusConsultaIsEqualToSomething() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        // Get all the consultaEspecialistaList where statusConsulta equals to
        defaultConsultaEspecialistaFiltering(
            "statusConsulta.equals=" + DEFAULT_STATUS_CONSULTA,
            "statusConsulta.equals=" + UPDATED_STATUS_CONSULTA
        );
    }

    @Test
    @Transactional
    void getAllConsultaEspecialistasByStatusConsultaIsInShouldWork() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        // Get all the consultaEspecialistaList where statusConsulta in
        defaultConsultaEspecialistaFiltering(
            "statusConsulta.in=" + DEFAULT_STATUS_CONSULTA + "," + UPDATED_STATUS_CONSULTA,
            "statusConsulta.in=" + UPDATED_STATUS_CONSULTA
        );
    }

    @Test
    @Transactional
    void getAllConsultaEspecialistasByStatusConsultaIsNullOrNotNull() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        // Get all the consultaEspecialistaList where statusConsulta is not null
        defaultConsultaEspecialistaFiltering("statusConsulta.specified=true", "statusConsulta.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultaEspecialistasByLinkConsultaIsEqualToSomething() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        // Get all the consultaEspecialistaList where linkConsulta equals to
        defaultConsultaEspecialistaFiltering(
            "linkConsulta.equals=" + DEFAULT_LINK_CONSULTA,
            "linkConsulta.equals=" + UPDATED_LINK_CONSULTA
        );
    }

    @Test
    @Transactional
    void getAllConsultaEspecialistasByLinkConsultaIsInShouldWork() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        // Get all the consultaEspecialistaList where linkConsulta in
        defaultConsultaEspecialistaFiltering(
            "linkConsulta.in=" + DEFAULT_LINK_CONSULTA + "," + UPDATED_LINK_CONSULTA,
            "linkConsulta.in=" + UPDATED_LINK_CONSULTA
        );
    }

    @Test
    @Transactional
    void getAllConsultaEspecialistasByLinkConsultaIsNullOrNotNull() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        // Get all the consultaEspecialistaList where linkConsulta is not null
        defaultConsultaEspecialistaFiltering("linkConsulta.specified=true", "linkConsulta.specified=false");
    }

    @Test
    @Transactional
    void getAllConsultaEspecialistasByLinkConsultaContainsSomething() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        // Get all the consultaEspecialistaList where linkConsulta contains
        defaultConsultaEspecialistaFiltering(
            "linkConsulta.contains=" + DEFAULT_LINK_CONSULTA,
            "linkConsulta.contains=" + UPDATED_LINK_CONSULTA
        );
    }

    @Test
    @Transactional
    void getAllConsultaEspecialistasByLinkConsultaNotContainsSomething() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        // Get all the consultaEspecialistaList where linkConsulta does not contain
        defaultConsultaEspecialistaFiltering(
            "linkConsulta.doesNotContain=" + UPDATED_LINK_CONSULTA,
            "linkConsulta.doesNotContain=" + DEFAULT_LINK_CONSULTA
        );
    }

    @Test
    @Transactional
    void getAllConsultaEspecialistasByInternalUserIsEqualToSomething() throws Exception {
        User internalUser;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);
            internalUser = UserResourceIT.createEntity(em);
        } else {
            internalUser = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(internalUser);
        em.flush();
        consultaEspecialista.setInternalUser(internalUser);
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);
        Long internalUserId = internalUser.getId();
        // Get all the consultaEspecialistaList where internalUser equals to internalUserId
        defaultConsultaEspecialistaShouldBeFound("internalUserId.equals=" + internalUserId);

        // Get all the consultaEspecialistaList where internalUser equals to (internalUserId + 1)
        defaultConsultaEspecialistaShouldNotBeFound("internalUserId.equals=" + (internalUserId + 1));
    }

    @Test
    @Transactional
    void getAllConsultaEspecialistasByEspecialistaIsEqualToSomething() throws Exception {
        Especialista especialista;
        if (TestUtil.findAll(em, Especialista.class).isEmpty()) {
            consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);
            especialista = EspecialistaResourceIT.createEntity(em);
        } else {
            especialista = TestUtil.findAll(em, Especialista.class).get(0);
        }
        em.persist(especialista);
        em.flush();
        consultaEspecialista.setEspecialista(especialista);
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);
        Long especialistaId = especialista.getId();
        // Get all the consultaEspecialistaList where especialista equals to especialistaId
        defaultConsultaEspecialistaShouldBeFound("especialistaId.equals=" + especialistaId);

        // Get all the consultaEspecialistaList where especialista equals to (especialistaId + 1)
        defaultConsultaEspecialistaShouldNotBeFound("especialistaId.equals=" + (especialistaId + 1));
    }

    private void defaultConsultaEspecialistaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultConsultaEspecialistaShouldBeFound(shouldBeFound);
        defaultConsultaEspecialistaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConsultaEspecialistaShouldBeFound(String filter) throws Exception {
        restConsultaEspecialistaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consultaEspecialista.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoEspecialista").value(hasItem(DEFAULT_TIPO_ESPECIALISTA.toString())))
            .andExpect(jsonPath("$.[*].dataHorarioConsulta").value(hasItem(DEFAULT_DATA_HORARIO_CONSULTA.toString())))
            .andExpect(jsonPath("$.[*].statusConsulta").value(hasItem(DEFAULT_STATUS_CONSULTA.toString())))
            .andExpect(jsonPath("$.[*].linkConsulta").value(hasItem(DEFAULT_LINK_CONSULTA)));

        // Check, that the count call also returns 1
        restConsultaEspecialistaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConsultaEspecialistaShouldNotBeFound(String filter) throws Exception {
        restConsultaEspecialistaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConsultaEspecialistaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConsultaEspecialista() throws Exception {
        // Get the consultaEspecialista
        restConsultaEspecialistaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConsultaEspecialista() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consultaEspecialista
        ConsultaEspecialista updatedConsultaEspecialista = consultaEspecialistaRepository
            .findById(consultaEspecialista.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedConsultaEspecialista are not directly saved in db
        em.detach(updatedConsultaEspecialista);
        updatedConsultaEspecialista
            .tipoEspecialista(UPDATED_TIPO_ESPECIALISTA)
            .dataHorarioConsulta(UPDATED_DATA_HORARIO_CONSULTA)
            .statusConsulta(UPDATED_STATUS_CONSULTA)
            .linkConsulta(UPDATED_LINK_CONSULTA);

        restConsultaEspecialistaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConsultaEspecialista.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedConsultaEspecialista))
            )
            .andExpect(status().isOk());

        // Validate the ConsultaEspecialista in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConsultaEspecialistaToMatchAllProperties(updatedConsultaEspecialista);
    }

    @Test
    @Transactional
    void putNonExistingConsultaEspecialista() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consultaEspecialista.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultaEspecialistaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consultaEspecialista.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consultaEspecialista))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsultaEspecialista in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsultaEspecialista() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consultaEspecialista.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultaEspecialistaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(consultaEspecialista))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsultaEspecialista in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsultaEspecialista() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consultaEspecialista.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultaEspecialistaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(consultaEspecialista)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConsultaEspecialista in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsultaEspecialistaWithPatch() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consultaEspecialista using partial update
        ConsultaEspecialista partialUpdatedConsultaEspecialista = new ConsultaEspecialista();
        partialUpdatedConsultaEspecialista.setId(consultaEspecialista.getId());

        partialUpdatedConsultaEspecialista.statusConsulta(UPDATED_STATUS_CONSULTA);

        restConsultaEspecialistaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultaEspecialista.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConsultaEspecialista))
            )
            .andExpect(status().isOk());

        // Validate the ConsultaEspecialista in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConsultaEspecialistaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedConsultaEspecialista, consultaEspecialista),
            getPersistedConsultaEspecialista(consultaEspecialista)
        );
    }

    @Test
    @Transactional
    void fullUpdateConsultaEspecialistaWithPatch() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the consultaEspecialista using partial update
        ConsultaEspecialista partialUpdatedConsultaEspecialista = new ConsultaEspecialista();
        partialUpdatedConsultaEspecialista.setId(consultaEspecialista.getId());

        partialUpdatedConsultaEspecialista
            .tipoEspecialista(UPDATED_TIPO_ESPECIALISTA)
            .dataHorarioConsulta(UPDATED_DATA_HORARIO_CONSULTA)
            .statusConsulta(UPDATED_STATUS_CONSULTA)
            .linkConsulta(UPDATED_LINK_CONSULTA);

        restConsultaEspecialistaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsultaEspecialista.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConsultaEspecialista))
            )
            .andExpect(status().isOk());

        // Validate the ConsultaEspecialista in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConsultaEspecialistaUpdatableFieldsEquals(
            partialUpdatedConsultaEspecialista,
            getPersistedConsultaEspecialista(partialUpdatedConsultaEspecialista)
        );
    }

    @Test
    @Transactional
    void patchNonExistingConsultaEspecialista() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consultaEspecialista.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsultaEspecialistaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consultaEspecialista.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(consultaEspecialista))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsultaEspecialista in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsultaEspecialista() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consultaEspecialista.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultaEspecialistaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(consultaEspecialista))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConsultaEspecialista in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsultaEspecialista() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        consultaEspecialista.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsultaEspecialistaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(consultaEspecialista)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConsultaEspecialista in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsultaEspecialista() throws Exception {
        // Initialize the database
        consultaEspecialistaRepository.saveAndFlush(consultaEspecialista);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the consultaEspecialista
        restConsultaEspecialistaMockMvc
            .perform(delete(ENTITY_API_URL_ID, consultaEspecialista.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return consultaEspecialistaRepository.count();
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

    protected ConsultaEspecialista getPersistedConsultaEspecialista(ConsultaEspecialista consultaEspecialista) {
        return consultaEspecialistaRepository.findById(consultaEspecialista.getId()).orElseThrow();
    }

    protected void assertPersistedConsultaEspecialistaToMatchAllProperties(ConsultaEspecialista expectedConsultaEspecialista) {
        assertConsultaEspecialistaAllPropertiesEquals(
            expectedConsultaEspecialista,
            getPersistedConsultaEspecialista(expectedConsultaEspecialista)
        );
    }

    protected void assertPersistedConsultaEspecialistaToMatchUpdatableProperties(ConsultaEspecialista expectedConsultaEspecialista) {
        assertConsultaEspecialistaAllUpdatablePropertiesEquals(
            expectedConsultaEspecialista,
            getPersistedConsultaEspecialista(expectedConsultaEspecialista)
        );
    }
}
