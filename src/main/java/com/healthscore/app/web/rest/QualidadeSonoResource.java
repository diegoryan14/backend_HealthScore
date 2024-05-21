package com.healthscore.app.web.rest;

import com.healthscore.app.domain.QualidadeSono;
import com.healthscore.app.repository.QualidadeSonoRepository;
import com.healthscore.app.service.QualidadeSonoQueryService;
import com.healthscore.app.service.QualidadeSonoService;
import com.healthscore.app.service.criteria.QualidadeSonoCriteria;
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
 * REST controller for managing {@link com.healthscore.app.domain.QualidadeSono}.
 */
@RestController
@RequestMapping("/api/qualidade-sonos")
public class QualidadeSonoResource {

    private final Logger log = LoggerFactory.getLogger(QualidadeSonoResource.class);

    private static final String ENTITY_NAME = "qualidadeSono";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QualidadeSonoService qualidadeSonoService;

    private final QualidadeSonoRepository qualidadeSonoRepository;

    private final QualidadeSonoQueryService qualidadeSonoQueryService;

    public QualidadeSonoResource(
        QualidadeSonoService qualidadeSonoService,
        QualidadeSonoRepository qualidadeSonoRepository,
        QualidadeSonoQueryService qualidadeSonoQueryService
    ) {
        this.qualidadeSonoService = qualidadeSonoService;
        this.qualidadeSonoRepository = qualidadeSonoRepository;
        this.qualidadeSonoQueryService = qualidadeSonoQueryService;
    }

    /**
     * {@code POST  /qualidade-sonos} : Create a new qualidadeSono.
     *
     * @param qualidadeSono the qualidadeSono to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new qualidadeSono, or with status {@code 400 (Bad Request)} if the qualidadeSono has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<QualidadeSono> createQualidadeSono(@RequestBody QualidadeSono qualidadeSono) throws URISyntaxException {
        log.debug("REST request to save QualidadeSono : {}", qualidadeSono);
        if (qualidadeSono.getId() != null) {
            throw new BadRequestAlertException("A new qualidadeSono cannot already have an ID", ENTITY_NAME, "idexists");
        }
        qualidadeSono = qualidadeSonoService.save(qualidadeSono);
        return ResponseEntity.created(new URI("/api/qualidade-sonos/" + qualidadeSono.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, qualidadeSono.getId().toString()))
            .body(qualidadeSono);
    }

    /**
     * {@code PUT  /qualidade-sonos/:id} : Updates an existing qualidadeSono.
     *
     * @param id the id of the qualidadeSono to save.
     * @param qualidadeSono the qualidadeSono to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qualidadeSono,
     * or with status {@code 400 (Bad Request)} if the qualidadeSono is not valid,
     * or with status {@code 500 (Internal Server Error)} if the qualidadeSono couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QualidadeSono> updateQualidadeSono(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QualidadeSono qualidadeSono
    ) throws URISyntaxException {
        log.debug("REST request to update QualidadeSono : {}, {}", id, qualidadeSono);
        if (qualidadeSono.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, qualidadeSono.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!qualidadeSonoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        qualidadeSono = qualidadeSonoService.update(qualidadeSono);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, qualidadeSono.getId().toString()))
            .body(qualidadeSono);
    }

    /**
     * {@code PATCH  /qualidade-sonos/:id} : Partial updates given fields of an existing qualidadeSono, field will ignore if it is null
     *
     * @param id the id of the qualidadeSono to save.
     * @param qualidadeSono the qualidadeSono to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated qualidadeSono,
     * or with status {@code 400 (Bad Request)} if the qualidadeSono is not valid,
     * or with status {@code 404 (Not Found)} if the qualidadeSono is not found,
     * or with status {@code 500 (Internal Server Error)} if the qualidadeSono couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QualidadeSono> partialUpdateQualidadeSono(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody QualidadeSono qualidadeSono
    ) throws URISyntaxException {
        log.debug("REST request to partial update QualidadeSono partially : {}, {}", id, qualidadeSono);
        if (qualidadeSono.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, qualidadeSono.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!qualidadeSonoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QualidadeSono> result = qualidadeSonoService.partialUpdate(qualidadeSono);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, qualidadeSono.getId().toString())
        );
    }

    /**
     * {@code GET  /qualidade-sonos} : get all the qualidadeSonos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of qualidadeSonos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<QualidadeSono>> getAllQualidadeSonos(
        QualidadeSonoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get QualidadeSonos by criteria: {}", criteria);

        Page<QualidadeSono> page = qualidadeSonoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /qualidade-sonos/count} : count all the qualidadeSonos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countQualidadeSonos(QualidadeSonoCriteria criteria) {
        log.debug("REST request to count QualidadeSonos by criteria: {}", criteria);
        return ResponseEntity.ok().body(qualidadeSonoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /qualidade-sonos/:id} : get the "id" qualidadeSono.
     *
     * @param id the id of the qualidadeSono to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the qualidadeSono, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QualidadeSono> getQualidadeSono(@PathVariable("id") Long id) {
        log.debug("REST request to get QualidadeSono : {}", id);
        Optional<QualidadeSono> qualidadeSono = qualidadeSonoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(qualidadeSono);
    }

    /**
     * {@code DELETE  /qualidade-sonos/:id} : delete the "id" qualidadeSono.
     *
     * @param id the id of the qualidadeSono to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQualidadeSono(@PathVariable("id") Long id) {
        log.debug("REST request to delete QualidadeSono : {}", id);
        qualidadeSonoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
