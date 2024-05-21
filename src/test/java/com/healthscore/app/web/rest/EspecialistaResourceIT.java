package com.healthscore.app.web.rest;

import static com.healthscore.app.domain.EspecialistaAsserts.*;
import static com.healthscore.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthscore.app.IntegrationTest;
import com.healthscore.app.domain.Especialista;
import com.healthscore.app.domain.enumeration.Especializacao;
import com.healthscore.app.repository.EspecialistaRepository;
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
 * Integration tests for the {@link EspecialistaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EspecialistaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final Especializacao DEFAULT_ESPECIALIZACAO = Especializacao.NUTRICIONISTA;
    private static final Especializacao UPDATED_ESPECIALIZACAO = Especializacao.PSICOLOGO;

    private static final Instant DEFAULT_DATA_FORMACAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_FORMACAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_TELEFONE = 1;
    private static final Integer UPDATED_TELEFONE = 2;
    private static final Integer SMALLER_TELEFONE = 1 - 1;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATA_NASCIMENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_NASCIMENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/especialistas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EspecialistaRepository especialistaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEspecialistaMockMvc;

    private Especialista especialista;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Especialista createEntity(EntityManager em) {
        Especialista especialista = new Especialista()
            .nome(DEFAULT_NOME)
            .cpf(DEFAULT_CPF)
            .especializacao(DEFAULT_ESPECIALIZACAO)
            .dataFormacao(DEFAULT_DATA_FORMACAO)
            .telefone(DEFAULT_TELEFONE)
            .email(DEFAULT_EMAIL)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO);
        return especialista;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Especialista createUpdatedEntity(EntityManager em) {
        Especialista especialista = new Especialista()
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .especializacao(UPDATED_ESPECIALIZACAO)
            .dataFormacao(UPDATED_DATA_FORMACAO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL)
            .dataNascimento(UPDATED_DATA_NASCIMENTO);
        return especialista;
    }

    @BeforeEach
    public void initTest() {
        especialista = createEntity(em);
    }

    @Test
    @Transactional
    void createEspecialista() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Especialista
        var returnedEspecialista = om.readValue(
            restEspecialistaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(especialista)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Especialista.class
        );

        // Validate the Especialista in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEspecialistaUpdatableFieldsEquals(returnedEspecialista, getPersistedEspecialista(returnedEspecialista));
    }

    @Test
    @Transactional
    void createEspecialistaWithExistingId() throws Exception {
        // Create the Especialista with an existing ID
        especialista.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEspecialistaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(especialista)))
            .andExpect(status().isBadRequest());

        // Validate the Especialista in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEspecialistas() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList
        restEspecialistaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(especialista.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].especializacao").value(hasItem(DEFAULT_ESPECIALIZACAO.toString())))
            .andExpect(jsonPath("$.[*].dataFormacao").value(hasItem(DEFAULT_DATA_FORMACAO.toString())))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())));
    }

    @Test
    @Transactional
    void getEspecialista() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get the especialista
        restEspecialistaMockMvc
            .perform(get(ENTITY_API_URL_ID, especialista.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(especialista.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.especializacao").value(DEFAULT_ESPECIALIZACAO.toString()))
            .andExpect(jsonPath("$.dataFormacao").value(DEFAULT_DATA_FORMACAO.toString()))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()));
    }

    @Test
    @Transactional
    void getEspecialistasByIdFiltering() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        Long id = especialista.getId();

        defaultEspecialistaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEspecialistaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEspecialistaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEspecialistasByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where nome equals to
        defaultEspecialistaFiltering("nome.equals=" + DEFAULT_NOME, "nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEspecialistasByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where nome in
        defaultEspecialistaFiltering("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME, "nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEspecialistasByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where nome is not null
        defaultEspecialistaFiltering("nome.specified=true", "nome.specified=false");
    }

    @Test
    @Transactional
    void getAllEspecialistasByNomeContainsSomething() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where nome contains
        defaultEspecialistaFiltering("nome.contains=" + DEFAULT_NOME, "nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllEspecialistasByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where nome does not contain
        defaultEspecialistaFiltering("nome.doesNotContain=" + UPDATED_NOME, "nome.doesNotContain=" + DEFAULT_NOME);
    }

    @Test
    @Transactional
    void getAllEspecialistasByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where cpf equals to
        defaultEspecialistaFiltering("cpf.equals=" + DEFAULT_CPF, "cpf.equals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllEspecialistasByCpfIsInShouldWork() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where cpf in
        defaultEspecialistaFiltering("cpf.in=" + DEFAULT_CPF + "," + UPDATED_CPF, "cpf.in=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllEspecialistasByCpfIsNullOrNotNull() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where cpf is not null
        defaultEspecialistaFiltering("cpf.specified=true", "cpf.specified=false");
    }

    @Test
    @Transactional
    void getAllEspecialistasByCpfContainsSomething() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where cpf contains
        defaultEspecialistaFiltering("cpf.contains=" + DEFAULT_CPF, "cpf.contains=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllEspecialistasByCpfNotContainsSomething() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where cpf does not contain
        defaultEspecialistaFiltering("cpf.doesNotContain=" + UPDATED_CPF, "cpf.doesNotContain=" + DEFAULT_CPF);
    }

    @Test
    @Transactional
    void getAllEspecialistasByEspecializacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where especializacao equals to
        defaultEspecialistaFiltering("especializacao.equals=" + DEFAULT_ESPECIALIZACAO, "especializacao.equals=" + UPDATED_ESPECIALIZACAO);
    }

    @Test
    @Transactional
    void getAllEspecialistasByEspecializacaoIsInShouldWork() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where especializacao in
        defaultEspecialistaFiltering(
            "especializacao.in=" + DEFAULT_ESPECIALIZACAO + "," + UPDATED_ESPECIALIZACAO,
            "especializacao.in=" + UPDATED_ESPECIALIZACAO
        );
    }

    @Test
    @Transactional
    void getAllEspecialistasByEspecializacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where especializacao is not null
        defaultEspecialistaFiltering("especializacao.specified=true", "especializacao.specified=false");
    }

    @Test
    @Transactional
    void getAllEspecialistasByDataFormacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where dataFormacao equals to
        defaultEspecialistaFiltering("dataFormacao.equals=" + DEFAULT_DATA_FORMACAO, "dataFormacao.equals=" + UPDATED_DATA_FORMACAO);
    }

    @Test
    @Transactional
    void getAllEspecialistasByDataFormacaoIsInShouldWork() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where dataFormacao in
        defaultEspecialistaFiltering(
            "dataFormacao.in=" + DEFAULT_DATA_FORMACAO + "," + UPDATED_DATA_FORMACAO,
            "dataFormacao.in=" + UPDATED_DATA_FORMACAO
        );
    }

    @Test
    @Transactional
    void getAllEspecialistasByDataFormacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where dataFormacao is not null
        defaultEspecialistaFiltering("dataFormacao.specified=true", "dataFormacao.specified=false");
    }

    @Test
    @Transactional
    void getAllEspecialistasByTelefoneIsEqualToSomething() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where telefone equals to
        defaultEspecialistaFiltering("telefone.equals=" + DEFAULT_TELEFONE, "telefone.equals=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllEspecialistasByTelefoneIsInShouldWork() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where telefone in
        defaultEspecialistaFiltering("telefone.in=" + DEFAULT_TELEFONE + "," + UPDATED_TELEFONE, "telefone.in=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllEspecialistasByTelefoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where telefone is not null
        defaultEspecialistaFiltering("telefone.specified=true", "telefone.specified=false");
    }

    @Test
    @Transactional
    void getAllEspecialistasByTelefoneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where telefone is greater than or equal to
        defaultEspecialistaFiltering("telefone.greaterThanOrEqual=" + DEFAULT_TELEFONE, "telefone.greaterThanOrEqual=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllEspecialistasByTelefoneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where telefone is less than or equal to
        defaultEspecialistaFiltering("telefone.lessThanOrEqual=" + DEFAULT_TELEFONE, "telefone.lessThanOrEqual=" + SMALLER_TELEFONE);
    }

    @Test
    @Transactional
    void getAllEspecialistasByTelefoneIsLessThanSomething() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where telefone is less than
        defaultEspecialistaFiltering("telefone.lessThan=" + UPDATED_TELEFONE, "telefone.lessThan=" + DEFAULT_TELEFONE);
    }

    @Test
    @Transactional
    void getAllEspecialistasByTelefoneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where telefone is greater than
        defaultEspecialistaFiltering("telefone.greaterThan=" + SMALLER_TELEFONE, "telefone.greaterThan=" + DEFAULT_TELEFONE);
    }

    @Test
    @Transactional
    void getAllEspecialistasByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where email equals to
        defaultEspecialistaFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEspecialistasByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where email in
        defaultEspecialistaFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEspecialistasByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where email is not null
        defaultEspecialistaFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllEspecialistasByEmailContainsSomething() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where email contains
        defaultEspecialistaFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEspecialistasByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where email does not contain
        defaultEspecialistaFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllEspecialistasByDataNascimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where dataNascimento equals to
        defaultEspecialistaFiltering(
            "dataNascimento.equals=" + DEFAULT_DATA_NASCIMENTO,
            "dataNascimento.equals=" + UPDATED_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllEspecialistasByDataNascimentoIsInShouldWork() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where dataNascimento in
        defaultEspecialistaFiltering(
            "dataNascimento.in=" + DEFAULT_DATA_NASCIMENTO + "," + UPDATED_DATA_NASCIMENTO,
            "dataNascimento.in=" + UPDATED_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllEspecialistasByDataNascimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        // Get all the especialistaList where dataNascimento is not null
        defaultEspecialistaFiltering("dataNascimento.specified=true", "dataNascimento.specified=false");
    }

    private void defaultEspecialistaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEspecialistaShouldBeFound(shouldBeFound);
        defaultEspecialistaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEspecialistaShouldBeFound(String filter) throws Exception {
        restEspecialistaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(especialista.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].especializacao").value(hasItem(DEFAULT_ESPECIALIZACAO.toString())))
            .andExpect(jsonPath("$.[*].dataFormacao").value(hasItem(DEFAULT_DATA_FORMACAO.toString())))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())));

        // Check, that the count call also returns 1
        restEspecialistaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEspecialistaShouldNotBeFound(String filter) throws Exception {
        restEspecialistaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEspecialistaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEspecialista() throws Exception {
        // Get the especialista
        restEspecialistaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEspecialista() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the especialista
        Especialista updatedEspecialista = especialistaRepository.findById(especialista.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEspecialista are not directly saved in db
        em.detach(updatedEspecialista);
        updatedEspecialista
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .especializacao(UPDATED_ESPECIALIZACAO)
            .dataFormacao(UPDATED_DATA_FORMACAO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL)
            .dataNascimento(UPDATED_DATA_NASCIMENTO);

        restEspecialistaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEspecialista.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEspecialista))
            )
            .andExpect(status().isOk());

        // Validate the Especialista in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEspecialistaToMatchAllProperties(updatedEspecialista);
    }

    @Test
    @Transactional
    void putNonExistingEspecialista() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especialista.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEspecialistaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, especialista.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(especialista))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especialista in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEspecialista() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especialista.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecialistaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(especialista))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especialista in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEspecialista() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especialista.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecialistaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(especialista)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Especialista in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEspecialistaWithPatch() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the especialista using partial update
        Especialista partialUpdatedEspecialista = new Especialista();
        partialUpdatedEspecialista.setId(especialista.getId());

        partialUpdatedEspecialista.especializacao(UPDATED_ESPECIALIZACAO);

        restEspecialistaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEspecialista.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEspecialista))
            )
            .andExpect(status().isOk());

        // Validate the Especialista in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEspecialistaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEspecialista, especialista),
            getPersistedEspecialista(especialista)
        );
    }

    @Test
    @Transactional
    void fullUpdateEspecialistaWithPatch() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the especialista using partial update
        Especialista partialUpdatedEspecialista = new Especialista();
        partialUpdatedEspecialista.setId(especialista.getId());

        partialUpdatedEspecialista
            .nome(UPDATED_NOME)
            .cpf(UPDATED_CPF)
            .especializacao(UPDATED_ESPECIALIZACAO)
            .dataFormacao(UPDATED_DATA_FORMACAO)
            .telefone(UPDATED_TELEFONE)
            .email(UPDATED_EMAIL)
            .dataNascimento(UPDATED_DATA_NASCIMENTO);

        restEspecialistaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEspecialista.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEspecialista))
            )
            .andExpect(status().isOk());

        // Validate the Especialista in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEspecialistaUpdatableFieldsEquals(partialUpdatedEspecialista, getPersistedEspecialista(partialUpdatedEspecialista));
    }

    @Test
    @Transactional
    void patchNonExistingEspecialista() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especialista.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEspecialistaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, especialista.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(especialista))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especialista in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEspecialista() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especialista.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecialistaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(especialista))
            )
            .andExpect(status().isBadRequest());

        // Validate the Especialista in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEspecialista() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        especialista.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEspecialistaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(especialista)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Especialista in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEspecialista() throws Exception {
        // Initialize the database
        especialistaRepository.saveAndFlush(especialista);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the especialista
        restEspecialistaMockMvc
            .perform(delete(ENTITY_API_URL_ID, especialista.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return especialistaRepository.count();
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

    protected Especialista getPersistedEspecialista(Especialista especialista) {
        return especialistaRepository.findById(especialista.getId()).orElseThrow();
    }

    protected void assertPersistedEspecialistaToMatchAllProperties(Especialista expectedEspecialista) {
        assertEspecialistaAllPropertiesEquals(expectedEspecialista, getPersistedEspecialista(expectedEspecialista));
    }

    protected void assertPersistedEspecialistaToMatchUpdatableProperties(Especialista expectedEspecialista) {
        assertEspecialistaAllUpdatablePropertiesEquals(expectedEspecialista, getPersistedEspecialista(expectedEspecialista));
    }
}
