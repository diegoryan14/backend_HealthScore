package com.healthscore.app.web.rest;

import com.healthscore.app.domain.AtividadeFisica;
import com.healthscore.app.repository.AtividadeFisicaRepository;
import com.healthscore.app.service.AtividadeFisicaQueryService;
import com.healthscore.app.service.AtividadeFisicaService;
import com.healthscore.app.service.criteria.AtividadeFisicaCriteria;
import com.healthscore.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.healthscore.app.domain.AtividadeFisica}.
 */
@RestController
@RequestMapping("/api/atividade-fisicas")
public class AtividadeFisicaResource {

    private final Logger log = LoggerFactory.getLogger(AtividadeFisicaResource.class);

    private static final String ENTITY_NAME = "atividadeFisica";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AtividadeFisicaService atividadeFisicaService;

    private final AtividadeFisicaRepository atividadeFisicaRepository;

    private final AtividadeFisicaQueryService atividadeFisicaQueryService;

    public AtividadeFisicaResource(
        AtividadeFisicaService atividadeFisicaService,
        AtividadeFisicaRepository atividadeFisicaRepository,
        AtividadeFisicaQueryService atividadeFisicaQueryService
    ) {
        this.atividadeFisicaService = atividadeFisicaService;
        this.atividadeFisicaRepository = atividadeFisicaRepository;
        this.atividadeFisicaQueryService = atividadeFisicaQueryService;
    }

    /**
     * {@code POST  /atividade-fisicas} : Create a new atividadeFisica.
     *
     * @param atividadeFisica the atividadeFisica to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new atividadeFisica, or with status {@code 400 (Bad Request)} if the atividadeFisica has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AtividadeFisica> createAtividadeFisica(@RequestBody AtividadeFisica atividadeFisica) throws URISyntaxException {
        log.debug("REST request to save AtividadeFisica : {}", atividadeFisica);
        if (atividadeFisica.getId() != null) {
            throw new BadRequestAlertException("A new atividadeFisica cannot already have an ID", ENTITY_NAME, "idexists");
        }
        atividadeFisica = atividadeFisicaService.save(atividadeFisica);
        return ResponseEntity.created(new URI("/api/atividade-fisicas/" + atividadeFisica.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, atividadeFisica.getId().toString()))
            .body(atividadeFisica);
    }

    /**
     * {@code PUT  /atividade-fisicas/:id} : Updates an existing atividadeFisica.
     *
     * @param id the id of the atividadeFisica to save.
     * @param atividadeFisica the atividadeFisica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated atividadeFisica,
     * or with status {@code 400 (Bad Request)} if the atividadeFisica is not valid,
     * or with status {@code 500 (Internal Server Error)} if the atividadeFisica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AtividadeFisica> updateAtividadeFisica(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AtividadeFisica atividadeFisica
    ) throws URISyntaxException {
        log.debug("REST request to update AtividadeFisica : {}, {}", id, atividadeFisica);
        if (atividadeFisica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, atividadeFisica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!atividadeFisicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        atividadeFisica = atividadeFisicaService.update(atividadeFisica);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, atividadeFisica.getId().toString()))
            .body(atividadeFisica);
    }

    /**
     * {@code PATCH  /atividade-fisicas/:id} : Partial updates given fields of an existing atividadeFisica, field will ignore if it is null
     *
     * @param id the id of the atividadeFisica to save.
     * @param atividadeFisica the atividadeFisica to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated atividadeFisica,
     * or with status {@code 400 (Bad Request)} if the atividadeFisica is not valid,
     * or with status {@code 404 (Not Found)} if the atividadeFisica is not found,
     * or with status {@code 500 (Internal Server Error)} if the atividadeFisica couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AtividadeFisica> partialUpdateAtividadeFisica(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AtividadeFisica atividadeFisica
    ) throws URISyntaxException {
        log.debug("REST request to partial update AtividadeFisica partially : {}, {}", id, atividadeFisica);
        if (atividadeFisica.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, atividadeFisica.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!atividadeFisicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AtividadeFisica> result = atividadeFisicaService.partialUpdate(atividadeFisica);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, atividadeFisica.getId().toString())
        );
    }

    /**
     * {@code GET  /atividade-fisicas} : get all the atividadeFisicas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of atividadeFisicas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AtividadeFisica>> getAllAtividadeFisicas(
        AtividadeFisicaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AtividadeFisicas by criteria: {}", criteria);

        Page<AtividadeFisica> page = atividadeFisicaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /atividade-fisicas/count} : count all the atividadeFisicas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAtividadeFisicas(AtividadeFisicaCriteria criteria) {
        log.debug("REST request to count AtividadeFisicas by criteria: {}", criteria);
        return ResponseEntity.ok().body(atividadeFisicaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /atividade-fisicas/:id} : get the "id" atividadeFisica.
     *
     * @param id the id of the atividadeFisica to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the atividadeFisica, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AtividadeFisica> getAtividadeFisica(@PathVariable("id") Long id) {
        log.debug("REST request to get AtividadeFisica : {}", id);
        Optional<AtividadeFisica> atividadeFisica = atividadeFisicaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(atividadeFisica);
    }

    /**
     * {@code DELETE  /atividade-fisicas/:id} : delete the "id" atividadeFisica.
     *
     * @param id the id of the atividadeFisica to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAtividadeFisica(@PathVariable("id") Long id) {
        log.debug("REST request to delete AtividadeFisica : {}", id);
        atividadeFisicaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
