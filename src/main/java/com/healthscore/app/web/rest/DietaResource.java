package com.healthscore.app.web.rest;

import com.healthscore.app.domain.Dieta;
import com.healthscore.app.repository.DietaRepository;
import com.healthscore.app.service.DietaQueryService;
import com.healthscore.app.service.DietaService;
import com.healthscore.app.service.criteria.DietaCriteria;
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
 * REST controller for managing {@link com.healthscore.app.domain.Dieta}.
 */
@RestController
@RequestMapping("/api/dietas")
public class DietaResource {

    private final Logger log = LoggerFactory.getLogger(DietaResource.class);

    private static final String ENTITY_NAME = "dieta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DietaService dietaService;

    private final DietaRepository dietaRepository;

    private final DietaQueryService dietaQueryService;

    public DietaResource(DietaService dietaService, DietaRepository dietaRepository, DietaQueryService dietaQueryService) {
        this.dietaService = dietaService;
        this.dietaRepository = dietaRepository;
        this.dietaQueryService = dietaQueryService;
    }

    /**
     * {@code POST  /dietas} : Create a new dieta.
     *
     * @param dieta the dieta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dieta, or with status {@code 400 (Bad Request)} if the dieta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Dieta> createDieta(@RequestBody Dieta dieta) throws URISyntaxException {
        log.debug("REST request to save Dieta : {}", dieta);
        if (dieta.getId() != null) {
            throw new BadRequestAlertException("A new dieta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dieta = dietaService.save(dieta);
        return ResponseEntity.created(new URI("/api/dietas/" + dieta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, dieta.getId().toString()))
            .body(dieta);
    }

    /**
     * {@code PUT  /dietas/:id} : Updates an existing dieta.
     *
     * @param id the id of the dieta to save.
     * @param dieta the dieta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dieta,
     * or with status {@code 400 (Bad Request)} if the dieta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dieta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Dieta> updateDieta(@PathVariable(value = "id", required = false) final Long id, @RequestBody Dieta dieta)
        throws URISyntaxException {
        log.debug("REST request to update Dieta : {}, {}", id, dieta);
        if (dieta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dieta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dietaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dieta = dietaService.update(dieta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dieta.getId().toString()))
            .body(dieta);
    }

    /**
     * {@code PATCH  /dietas/:id} : Partial updates given fields of an existing dieta, field will ignore if it is null
     *
     * @param id the id of the dieta to save.
     * @param dieta the dieta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dieta,
     * or with status {@code 400 (Bad Request)} if the dieta is not valid,
     * or with status {@code 404 (Not Found)} if the dieta is not found,
     * or with status {@code 500 (Internal Server Error)} if the dieta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Dieta> partialUpdateDieta(@PathVariable(value = "id", required = false) final Long id, @RequestBody Dieta dieta)
        throws URISyntaxException {
        log.debug("REST request to partial update Dieta partially : {}, {}", id, dieta);
        if (dieta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dieta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dietaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Dieta> result = dietaService.partialUpdate(dieta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dieta.getId().toString())
        );
    }

    /**
     * {@code GET  /dietas} : get all the dietas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dietas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Dieta>> getAllDietas(
        DietaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Dietas by criteria: {}", criteria);

        Page<Dieta> page = dietaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dietas/count} : count all the dietas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDietas(DietaCriteria criteria) {
        log.debug("REST request to count Dietas by criteria: {}", criteria);
        return ResponseEntity.ok().body(dietaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dietas/:id} : get the "id" dieta.
     *
     * @param id the id of the dieta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dieta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Dieta> getDieta(@PathVariable("id") Long id) {
        log.debug("REST request to get Dieta : {}", id);
        Optional<Dieta> dieta = dietaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dieta);
    }

    /**
     * {@code DELETE  /dietas/:id} : delete the "id" dieta.
     *
     * @param id the id of the dieta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDieta(@PathVariable("id") Long id) {
        log.debug("REST request to delete Dieta : {}", id);
        dietaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
