package com.healthscore.app.web.rest;

import com.healthscore.app.domain.ControleMedicamentos;
import com.healthscore.app.repository.ControleMedicamentosRepository;
import com.healthscore.app.service.ControleMedicamentosQueryService;
import com.healthscore.app.service.ControleMedicamentosService;
import com.healthscore.app.service.criteria.ControleMedicamentosCriteria;
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
 * REST controller for managing {@link com.healthscore.app.domain.ControleMedicamentos}.
 */
@RestController
@RequestMapping("/api/controle-medicamentos")
public class ControleMedicamentosResource {

    private final Logger log = LoggerFactory.getLogger(ControleMedicamentosResource.class);

    private static final String ENTITY_NAME = "controleMedicamentos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ControleMedicamentosService controleMedicamentosService;

    private final ControleMedicamentosRepository controleMedicamentosRepository;

    private final ControleMedicamentosQueryService controleMedicamentosQueryService;

    public ControleMedicamentosResource(
        ControleMedicamentosService controleMedicamentosService,
        ControleMedicamentosRepository controleMedicamentosRepository,
        ControleMedicamentosQueryService controleMedicamentosQueryService
    ) {
        this.controleMedicamentosService = controleMedicamentosService;
        this.controleMedicamentosRepository = controleMedicamentosRepository;
        this.controleMedicamentosQueryService = controleMedicamentosQueryService;
    }

    /**
     * {@code POST  /controle-medicamentos} : Create a new controleMedicamentos.
     *
     * @param controleMedicamentos the controleMedicamentos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new controleMedicamentos, or with status {@code 400 (Bad Request)} if the controleMedicamentos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ControleMedicamentos> createControleMedicamentos(@RequestBody ControleMedicamentos controleMedicamentos)
        throws URISyntaxException {
        log.debug("REST request to save ControleMedicamentos : {}", controleMedicamentos);
        if (controleMedicamentos.getId() != null) {
            throw new BadRequestAlertException("A new controleMedicamentos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        controleMedicamentos = controleMedicamentosService.save(controleMedicamentos);
        return ResponseEntity.created(new URI("/api/controle-medicamentos/" + controleMedicamentos.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, controleMedicamentos.getId().toString()))
            .body(controleMedicamentos);
    }

    /**
     * {@code PUT  /controle-medicamentos/:id} : Updates an existing controleMedicamentos.
     *
     * @param id the id of the controleMedicamentos to save.
     * @param controleMedicamentos the controleMedicamentos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated controleMedicamentos,
     * or with status {@code 400 (Bad Request)} if the controleMedicamentos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the controleMedicamentos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ControleMedicamentos> updateControleMedicamentos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ControleMedicamentos controleMedicamentos
    ) throws URISyntaxException {
        log.debug("REST request to update ControleMedicamentos : {}, {}", id, controleMedicamentos);
        if (controleMedicamentos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, controleMedicamentos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!controleMedicamentosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        controleMedicamentos = controleMedicamentosService.update(controleMedicamentos);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, controleMedicamentos.getId().toString()))
            .body(controleMedicamentos);
    }

    /**
     * {@code PATCH  /controle-medicamentos/:id} : Partial updates given fields of an existing controleMedicamentos, field will ignore if it is null
     *
     * @param id the id of the controleMedicamentos to save.
     * @param controleMedicamentos the controleMedicamentos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated controleMedicamentos,
     * or with status {@code 400 (Bad Request)} if the controleMedicamentos is not valid,
     * or with status {@code 404 (Not Found)} if the controleMedicamentos is not found,
     * or with status {@code 500 (Internal Server Error)} if the controleMedicamentos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ControleMedicamentos> partialUpdateControleMedicamentos(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ControleMedicamentos controleMedicamentos
    ) throws URISyntaxException {
        log.debug("REST request to partial update ControleMedicamentos partially : {}, {}", id, controleMedicamentos);
        if (controleMedicamentos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, controleMedicamentos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!controleMedicamentosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ControleMedicamentos> result = controleMedicamentosService.partialUpdate(controleMedicamentos);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, controleMedicamentos.getId().toString())
        );
    }

    /**
     * {@code GET  /controle-medicamentos} : get all the controleMedicamentos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of controleMedicamentos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ControleMedicamentos>> getAllControleMedicamentos(
        ControleMedicamentosCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ControleMedicamentos by criteria: {}", criteria);

        Page<ControleMedicamentos> page = controleMedicamentosQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /controle-medicamentos/count} : count all the controleMedicamentos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countControleMedicamentos(ControleMedicamentosCriteria criteria) {
        log.debug("REST request to count ControleMedicamentos by criteria: {}", criteria);
        return ResponseEntity.ok().body(controleMedicamentosQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /controle-medicamentos/:id} : get the "id" controleMedicamentos.
     *
     * @param id the id of the controleMedicamentos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the controleMedicamentos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ControleMedicamentos> getControleMedicamentos(@PathVariable("id") Long id) {
        log.debug("REST request to get ControleMedicamentos : {}", id);
        Optional<ControleMedicamentos> controleMedicamentos = controleMedicamentosService.findOne(id);
        return ResponseUtil.wrapOrNotFound(controleMedicamentos);
    }

    /**
     * {@code DELETE  /controle-medicamentos/:id} : delete the "id" controleMedicamentos.
     *
     * @param id the id of the controleMedicamentos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteControleMedicamentos(@PathVariable("id") Long id) {
        log.debug("REST request to delete ControleMedicamentos : {}", id);
        controleMedicamentosService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
