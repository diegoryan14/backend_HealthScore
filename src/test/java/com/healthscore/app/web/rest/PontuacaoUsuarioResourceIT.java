package com.healthscore.app.web.rest;

import static com.healthscore.app.domain.PontuacaoUsuarioAsserts.*;
import static com.healthscore.app.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthscore.app.IntegrationTest;
import com.healthscore.app.domain.PontuacaoUsuario;
import com.healthscore.app.domain.Usuario;
import com.healthscore.app.repository.PontuacaoUsuarioRepository;
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
 * Integration tests for the {@link PontuacaoUsuarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PontuacaoUsuarioResourceIT {

    private static final Instant DEFAULT_DATA_ALTERACAO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_ALTERACAO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_VALOR_ALTERACAO = 1;
    private static final Integer UPDATED_VALOR_ALTERACAO = 2;
    private static final Integer SMALLER_VALOR_ALTERACAO = 1 - 1;

    private static final String DEFAULT_MOTIVO = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pontuacao-usuarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PontuacaoUsuarioRepository pontuacaoUsuarioRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPontuacaoUsuarioMockMvc;

    private PontuacaoUsuario pontuacaoUsuario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PontuacaoUsuario createEntity(EntityManager em) {
        PontuacaoUsuario pontuacaoUsuario = new PontuacaoUsuario()
            .dataAlteracao(DEFAULT_DATA_ALTERACAO)
            .valorAlteracao(DEFAULT_VALOR_ALTERACAO)
            .motivo(DEFAULT_MOTIVO);
        return pontuacaoUsuario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PontuacaoUsuario createUpdatedEntity(EntityManager em) {
        PontuacaoUsuario pontuacaoUsuario = new PontuacaoUsuario()
            .dataAlteracao(UPDATED_DATA_ALTERACAO)
            .valorAlteracao(UPDATED_VALOR_ALTERACAO)
            .motivo(UPDATED_MOTIVO);
        return pontuacaoUsuario;
    }

    @BeforeEach
    public void initTest() {
        pontuacaoUsuario = createEntity(em);
    }

    @Test
    @Transactional
    void createPontuacaoUsuario() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PontuacaoUsuario
        var returnedPontuacaoUsuario = om.readValue(
            restPontuacaoUsuarioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pontuacaoUsuario)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PontuacaoUsuario.class
        );

        // Validate the PontuacaoUsuario in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPontuacaoUsuarioUpdatableFieldsEquals(returnedPontuacaoUsuario, getPersistedPontuacaoUsuario(returnedPontuacaoUsuario));
    }

    @Test
    @Transactional
    void createPontuacaoUsuarioWithExistingId() throws Exception {
        // Create the PontuacaoUsuario with an existing ID
        pontuacaoUsuario.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPontuacaoUsuarioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pontuacaoUsuario)))
            .andExpect(status().isBadRequest());

        // Validate the PontuacaoUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPontuacaoUsuarios() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        // Get all the pontuacaoUsuarioList
        restPontuacaoUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pontuacaoUsuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataAlteracao").value(hasItem(DEFAULT_DATA_ALTERACAO.toString())))
            .andExpect(jsonPath("$.[*].valorAlteracao").value(hasItem(DEFAULT_VALOR_ALTERACAO)))
            .andExpect(jsonPath("$.[*].motivo").value(hasItem(DEFAULT_MOTIVO)));
    }

    @Test
    @Transactional
    void getPontuacaoUsuario() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        // Get the pontuacaoUsuario
        restPontuacaoUsuarioMockMvc
            .perform(get(ENTITY_API_URL_ID, pontuacaoUsuario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pontuacaoUsuario.getId().intValue()))
            .andExpect(jsonPath("$.dataAlteracao").value(DEFAULT_DATA_ALTERACAO.toString()))
            .andExpect(jsonPath("$.valorAlteracao").value(DEFAULT_VALOR_ALTERACAO))
            .andExpect(jsonPath("$.motivo").value(DEFAULT_MOTIVO));
    }

    @Test
    @Transactional
    void getPontuacaoUsuariosByIdFiltering() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        Long id = pontuacaoUsuario.getId();

        defaultPontuacaoUsuarioFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPontuacaoUsuarioFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPontuacaoUsuarioFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPontuacaoUsuariosByDataAlteracaoIsEqualToSomething() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        // Get all the pontuacaoUsuarioList where dataAlteracao equals to
        defaultPontuacaoUsuarioFiltering(
            "dataAlteracao.equals=" + DEFAULT_DATA_ALTERACAO,
            "dataAlteracao.equals=" + UPDATED_DATA_ALTERACAO
        );
    }

    @Test
    @Transactional
    void getAllPontuacaoUsuariosByDataAlteracaoIsInShouldWork() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        // Get all the pontuacaoUsuarioList where dataAlteracao in
        defaultPontuacaoUsuarioFiltering(
            "dataAlteracao.in=" + DEFAULT_DATA_ALTERACAO + "," + UPDATED_DATA_ALTERACAO,
            "dataAlteracao.in=" + UPDATED_DATA_ALTERACAO
        );
    }

    @Test
    @Transactional
    void getAllPontuacaoUsuariosByDataAlteracaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        // Get all the pontuacaoUsuarioList where dataAlteracao is not null
        defaultPontuacaoUsuarioFiltering("dataAlteracao.specified=true", "dataAlteracao.specified=false");
    }

    @Test
    @Transactional
    void getAllPontuacaoUsuariosByValorAlteracaoIsEqualToSomething() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        // Get all the pontuacaoUsuarioList where valorAlteracao equals to
        defaultPontuacaoUsuarioFiltering(
            "valorAlteracao.equals=" + DEFAULT_VALOR_ALTERACAO,
            "valorAlteracao.equals=" + UPDATED_VALOR_ALTERACAO
        );
    }

    @Test
    @Transactional
    void getAllPontuacaoUsuariosByValorAlteracaoIsInShouldWork() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        // Get all the pontuacaoUsuarioList where valorAlteracao in
        defaultPontuacaoUsuarioFiltering(
            "valorAlteracao.in=" + DEFAULT_VALOR_ALTERACAO + "," + UPDATED_VALOR_ALTERACAO,
            "valorAlteracao.in=" + UPDATED_VALOR_ALTERACAO
        );
    }

    @Test
    @Transactional
    void getAllPontuacaoUsuariosByValorAlteracaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        // Get all the pontuacaoUsuarioList where valorAlteracao is not null
        defaultPontuacaoUsuarioFiltering("valorAlteracao.specified=true", "valorAlteracao.specified=false");
    }

    @Test
    @Transactional
    void getAllPontuacaoUsuariosByValorAlteracaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        // Get all the pontuacaoUsuarioList where valorAlteracao is greater than or equal to
        defaultPontuacaoUsuarioFiltering(
            "valorAlteracao.greaterThanOrEqual=" + DEFAULT_VALOR_ALTERACAO,
            "valorAlteracao.greaterThanOrEqual=" + UPDATED_VALOR_ALTERACAO
        );
    }

    @Test
    @Transactional
    void getAllPontuacaoUsuariosByValorAlteracaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        // Get all the pontuacaoUsuarioList where valorAlteracao is less than or equal to
        defaultPontuacaoUsuarioFiltering(
            "valorAlteracao.lessThanOrEqual=" + DEFAULT_VALOR_ALTERACAO,
            "valorAlteracao.lessThanOrEqual=" + SMALLER_VALOR_ALTERACAO
        );
    }

    @Test
    @Transactional
    void getAllPontuacaoUsuariosByValorAlteracaoIsLessThanSomething() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        // Get all the pontuacaoUsuarioList where valorAlteracao is less than
        defaultPontuacaoUsuarioFiltering(
            "valorAlteracao.lessThan=" + UPDATED_VALOR_ALTERACAO,
            "valorAlteracao.lessThan=" + DEFAULT_VALOR_ALTERACAO
        );
    }

    @Test
    @Transactional
    void getAllPontuacaoUsuariosByValorAlteracaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        // Get all the pontuacaoUsuarioList where valorAlteracao is greater than
        defaultPontuacaoUsuarioFiltering(
            "valorAlteracao.greaterThan=" + SMALLER_VALOR_ALTERACAO,
            "valorAlteracao.greaterThan=" + DEFAULT_VALOR_ALTERACAO
        );
    }

    @Test
    @Transactional
    void getAllPontuacaoUsuariosByMotivoIsEqualToSomething() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        // Get all the pontuacaoUsuarioList where motivo equals to
        defaultPontuacaoUsuarioFiltering("motivo.equals=" + DEFAULT_MOTIVO, "motivo.equals=" + UPDATED_MOTIVO);
    }

    @Test
    @Transactional
    void getAllPontuacaoUsuariosByMotivoIsInShouldWork() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        // Get all the pontuacaoUsuarioList where motivo in
        defaultPontuacaoUsuarioFiltering("motivo.in=" + DEFAULT_MOTIVO + "," + UPDATED_MOTIVO, "motivo.in=" + UPDATED_MOTIVO);
    }

    @Test
    @Transactional
    void getAllPontuacaoUsuariosByMotivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        // Get all the pontuacaoUsuarioList where motivo is not null
        defaultPontuacaoUsuarioFiltering("motivo.specified=true", "motivo.specified=false");
    }

    @Test
    @Transactional
    void getAllPontuacaoUsuariosByMotivoContainsSomething() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        // Get all the pontuacaoUsuarioList where motivo contains
        defaultPontuacaoUsuarioFiltering("motivo.contains=" + DEFAULT_MOTIVO, "motivo.contains=" + UPDATED_MOTIVO);
    }

    @Test
    @Transactional
    void getAllPontuacaoUsuariosByMotivoNotContainsSomething() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        // Get all the pontuacaoUsuarioList where motivo does not contain
        defaultPontuacaoUsuarioFiltering("motivo.doesNotContain=" + UPDATED_MOTIVO, "motivo.doesNotContain=" + DEFAULT_MOTIVO);
    }

    @Test
    @Transactional
    void getAllPontuacaoUsuariosByUsuarioIsEqualToSomething() throws Exception {
        Usuario usuario;
        if (TestUtil.findAll(em, Usuario.class).isEmpty()) {
            pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);
            usuario = UsuarioResourceIT.createEntity(em);
        } else {
            usuario = TestUtil.findAll(em, Usuario.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        pontuacaoUsuario.setUsuario(usuario);
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);
        Long usuarioId = usuario.getId();
        // Get all the pontuacaoUsuarioList where usuario equals to usuarioId
        defaultPontuacaoUsuarioShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the pontuacaoUsuarioList where usuario equals to (usuarioId + 1)
        defaultPontuacaoUsuarioShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    private void defaultPontuacaoUsuarioFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPontuacaoUsuarioShouldBeFound(shouldBeFound);
        defaultPontuacaoUsuarioShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPontuacaoUsuarioShouldBeFound(String filter) throws Exception {
        restPontuacaoUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pontuacaoUsuario.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataAlteracao").value(hasItem(DEFAULT_DATA_ALTERACAO.toString())))
            .andExpect(jsonPath("$.[*].valorAlteracao").value(hasItem(DEFAULT_VALOR_ALTERACAO)))
            .andExpect(jsonPath("$.[*].motivo").value(hasItem(DEFAULT_MOTIVO)));

        // Check, that the count call also returns 1
        restPontuacaoUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPontuacaoUsuarioShouldNotBeFound(String filter) throws Exception {
        restPontuacaoUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPontuacaoUsuarioMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPontuacaoUsuario() throws Exception {
        // Get the pontuacaoUsuario
        restPontuacaoUsuarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPontuacaoUsuario() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pontuacaoUsuario
        PontuacaoUsuario updatedPontuacaoUsuario = pontuacaoUsuarioRepository.findById(pontuacaoUsuario.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPontuacaoUsuario are not directly saved in db
        em.detach(updatedPontuacaoUsuario);
        updatedPontuacaoUsuario.dataAlteracao(UPDATED_DATA_ALTERACAO).valorAlteracao(UPDATED_VALOR_ALTERACAO).motivo(UPDATED_MOTIVO);

        restPontuacaoUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPontuacaoUsuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPontuacaoUsuario))
            )
            .andExpect(status().isOk());

        // Validate the PontuacaoUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPontuacaoUsuarioToMatchAllProperties(updatedPontuacaoUsuario);
    }

    @Test
    @Transactional
    void putNonExistingPontuacaoUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pontuacaoUsuario.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPontuacaoUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pontuacaoUsuario.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pontuacaoUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the PontuacaoUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPontuacaoUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pontuacaoUsuario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPontuacaoUsuarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pontuacaoUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the PontuacaoUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPontuacaoUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pontuacaoUsuario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPontuacaoUsuarioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pontuacaoUsuario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PontuacaoUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePontuacaoUsuarioWithPatch() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pontuacaoUsuario using partial update
        PontuacaoUsuario partialUpdatedPontuacaoUsuario = new PontuacaoUsuario();
        partialUpdatedPontuacaoUsuario.setId(pontuacaoUsuario.getId());

        partialUpdatedPontuacaoUsuario.dataAlteracao(UPDATED_DATA_ALTERACAO).motivo(UPDATED_MOTIVO);

        restPontuacaoUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPontuacaoUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPontuacaoUsuario))
            )
            .andExpect(status().isOk());

        // Validate the PontuacaoUsuario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPontuacaoUsuarioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPontuacaoUsuario, pontuacaoUsuario),
            getPersistedPontuacaoUsuario(pontuacaoUsuario)
        );
    }

    @Test
    @Transactional
    void fullUpdatePontuacaoUsuarioWithPatch() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pontuacaoUsuario using partial update
        PontuacaoUsuario partialUpdatedPontuacaoUsuario = new PontuacaoUsuario();
        partialUpdatedPontuacaoUsuario.setId(pontuacaoUsuario.getId());

        partialUpdatedPontuacaoUsuario.dataAlteracao(UPDATED_DATA_ALTERACAO).valorAlteracao(UPDATED_VALOR_ALTERACAO).motivo(UPDATED_MOTIVO);

        restPontuacaoUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPontuacaoUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPontuacaoUsuario))
            )
            .andExpect(status().isOk());

        // Validate the PontuacaoUsuario in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPontuacaoUsuarioUpdatableFieldsEquals(
            partialUpdatedPontuacaoUsuario,
            getPersistedPontuacaoUsuario(partialUpdatedPontuacaoUsuario)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPontuacaoUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pontuacaoUsuario.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPontuacaoUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pontuacaoUsuario.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pontuacaoUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the PontuacaoUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPontuacaoUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pontuacaoUsuario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPontuacaoUsuarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pontuacaoUsuario))
            )
            .andExpect(status().isBadRequest());

        // Validate the PontuacaoUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPontuacaoUsuario() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pontuacaoUsuario.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPontuacaoUsuarioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pontuacaoUsuario)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PontuacaoUsuario in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePontuacaoUsuario() throws Exception {
        // Initialize the database
        pontuacaoUsuarioRepository.saveAndFlush(pontuacaoUsuario);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pontuacaoUsuario
        restPontuacaoUsuarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, pontuacaoUsuario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pontuacaoUsuarioRepository.count();
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

    protected PontuacaoUsuario getPersistedPontuacaoUsuario(PontuacaoUsuario pontuacaoUsuario) {
        return pontuacaoUsuarioRepository.findById(pontuacaoUsuario.getId()).orElseThrow();
    }

    protected void assertPersistedPontuacaoUsuarioToMatchAllProperties(PontuacaoUsuario expectedPontuacaoUsuario) {
        assertPontuacaoUsuarioAllPropertiesEquals(expectedPontuacaoUsuario, getPersistedPontuacaoUsuario(expectedPontuacaoUsuario));
    }

    protected void assertPersistedPontuacaoUsuarioToMatchUpdatableProperties(PontuacaoUsuario expectedPontuacaoUsuario) {
        assertPontuacaoUsuarioAllUpdatablePropertiesEquals(
            expectedPontuacaoUsuario,
            getPersistedPontuacaoUsuario(expectedPontuacaoUsuario)
        );
    }
}
