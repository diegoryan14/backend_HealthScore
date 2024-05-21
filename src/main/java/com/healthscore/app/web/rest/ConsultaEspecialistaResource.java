package com.healthscore.app.web.rest;

import com.healthscore.app.domain.ConsultaEspecialista;
import com.healthscore.app.repository.ConsultaEspecialistaRepository;
import com.healthscore.app.service.ConsultaEspecialistaQueryService;
import com.healthscore.app.service.ConsultaEspecialistaService;
import com.healthscore.app.service.criteria.ConsultaEspecialistaCriteria;
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
 * REST controller for managing {@link com.healthscore.app.domain.ConsultaEspecialista}.
 */
@RestController
@RequestMapping("/api/consulta-especialistas")
public class ConsultaEspecialistaResource {

    private final Logger log = LoggerFactory.getLogger(ConsultaEspecialistaResource.class);

    private static final String ENTITY_NAME = "consultaEspecialista";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConsultaEspecialistaService consultaEspecialistaService;

    private final ConsultaEspecialistaRepository consultaEspecialistaRepository;

    private final ConsultaEspecialistaQueryService consultaEspecialistaQueryService;

    public ConsultaEspecialistaResource(
        ConsultaEspecialistaService consultaEspecialistaService,
        ConsultaEspecialistaRepository consultaEspecialistaRepository,
        ConsultaEspecialistaQueryService consultaEspecialistaQueryService
    ) {
        this.consultaEspecialistaService = consultaEspecialistaService;
        this.consultaEspecialistaRepository = consultaEspecialistaRepository;
        this.consultaEspecialistaQueryService = consultaEspecialistaQueryService;
    }

    /**
     * {@code POST  /consulta-especialistas} : Create a new consultaEspecialista.
     *
     * @param consultaEspecialista the consultaEspecialista to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consultaEspecialista, or with status {@code 400 (Bad Request)} if the consultaEspecialista has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ConsultaEspecialista> createConsultaEspecialista(@RequestBody ConsultaEspecialista consultaEspecialista)
        throws URISyntaxException {
        log.debug("REST request to save ConsultaEspecialista : {}", consultaEspecialista);
        if (consultaEspecialista.getId() != null) {
            throw new BadRequestAlertException("A new consultaEspecialista cannot already have an ID", ENTITY_NAME, "idexists");
        }
        consultaEspecialista = consultaEspecialistaService.save(consultaEspecialista);
        return ResponseEntity.created(new URI("/api/consulta-especialistas/" + consultaEspecialista.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, consultaEspecialista.getId().toString()))
            .body(consultaEspecialista);
    }

    /**
     * {@code PUT  /consulta-especialistas/:id} : Updates an existing consultaEspecialista.
     *
     * @param id the id of the consultaEspecialista to save.
     * @param consultaEspecialista the consultaEspecialista to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultaEspecialista,
     * or with status {@code 400 (Bad Request)} if the consultaEspecialista is not valid,
     * or with status {@code 500 (Internal Server Error)} if the consultaEspecialista couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConsultaEspecialista> updateConsultaEspecialista(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConsultaEspecialista consultaEspecialista
    ) throws URISyntaxException {
        log.debug("REST request to update ConsultaEspecialista : {}, {}", id, consultaEspecialista);
        if (consultaEspecialista.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultaEspecialista.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultaEspecialistaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        consultaEspecialista = consultaEspecialistaService.update(consultaEspecialista);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consultaEspecialista.getId().toString()))
            .body(consultaEspecialista);
    }

    /**
     * {@code PATCH  /consulta-especialistas/:id} : Partial updates given fields of an existing consultaEspecialista, field will ignore if it is null
     *
     * @param id the id of the consultaEspecialista to save.
     * @param consultaEspecialista the consultaEspecialista to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consultaEspecialista,
     * or with status {@code 400 (Bad Request)} if the consultaEspecialista is not valid,
     * or with status {@code 404 (Not Found)} if the consultaEspecialista is not found,
     * or with status {@code 500 (Internal Server Error)} if the consultaEspecialista couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConsultaEspecialista> partialUpdateConsultaEspecialista(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConsultaEspecialista consultaEspecialista
    ) throws URISyntaxException {
        log.debug("REST request to partial update ConsultaEspecialista partially : {}, {}", id, consultaEspecialista);
        if (consultaEspecialista.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consultaEspecialista.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consultaEspecialistaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConsultaEspecialista> result = consultaEspecialistaService.partialUpdate(consultaEspecialista);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consultaEspecialista.getId().toString())
        );
    }

    /**
     * {@code GET  /consulta-especialistas} : get all the consultaEspecialistas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of consultaEspecialistas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ConsultaEspecialista>> getAllConsultaEspecialistas(
        ConsultaEspecialistaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ConsultaEspecialistas by criteria: {}", criteria);

        Page<ConsultaEspecialista> page = consultaEspecialistaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /consulta-especialistas/count} : count all the consultaEspecialistas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countConsultaEspecialistas(ConsultaEspecialistaCriteria criteria) {
        log.debug("REST request to count ConsultaEspecialistas by criteria: {}", criteria);
        return ResponseEntity.ok().body(consultaEspecialistaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /consulta-especialistas/:id} : get the "id" consultaEspecialista.
     *
     * @param id the id of the consultaEspecialista to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consultaEspecialista, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConsultaEspecialista> getConsultaEspecialista(@PathVariable("id") Long id) {
        log.debug("REST request to get ConsultaEspecialista : {}", id);
        Optional<ConsultaEspecialista> consultaEspecialista = consultaEspecialistaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consultaEspecialista);
    }

    /**
     * {@code DELETE  /consulta-especialistas/:id} : delete the "id" consultaEspecialista.
     *
     * @param id the id of the consultaEspecialista to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultaEspecialista(@PathVariable("id") Long id) {
        log.debug("REST request to delete ConsultaEspecialista : {}", id);
        consultaEspecialistaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
