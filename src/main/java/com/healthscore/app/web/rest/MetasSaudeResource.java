package com.healthscore.app.web.rest;

import com.healthscore.app.domain.MetasSaude;
import com.healthscore.app.repository.MetasSaudeRepository;
import com.healthscore.app.service.MetasSaudeQueryService;
import com.healthscore.app.service.MetasSaudeService;
import com.healthscore.app.service.criteria.MetasSaudeCriteria;
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
 * REST controller for managing {@link com.healthscore.app.domain.MetasSaude}.
 */
@RestController
@RequestMapping("/api/metas-saudes")
public class MetasSaudeResource {

    private final Logger log = LoggerFactory.getLogger(MetasSaudeResource.class);

    private static final String ENTITY_NAME = "metasSaude";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetasSaudeService metasSaudeService;

    private final MetasSaudeRepository metasSaudeRepository;

    private final MetasSaudeQueryService metasSaudeQueryService;

    public MetasSaudeResource(
        MetasSaudeService metasSaudeService,
        MetasSaudeRepository metasSaudeRepository,
        MetasSaudeQueryService metasSaudeQueryService
    ) {
        this.metasSaudeService = metasSaudeService;
        this.metasSaudeRepository = metasSaudeRepository;
        this.metasSaudeQueryService = metasSaudeQueryService;
    }

    /**
     * {@code POST  /metas-saudes} : Create a new metasSaude.
     *
     * @param metasSaude the metasSaude to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metasSaude, or with status {@code 400 (Bad Request)} if the metasSaude has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MetasSaude> createMetasSaude(@RequestBody MetasSaude metasSaude) throws URISyntaxException {
        log.debug("REST request to save MetasSaude : {}", metasSaude);
        if (metasSaude.getId() != null) {
            throw new BadRequestAlertException("A new metasSaude cannot already have an ID", ENTITY_NAME, "idexists");
        }
        metasSaude = metasSaudeService.save(metasSaude);
        return ResponseEntity.created(new URI("/api/metas-saudes/" + metasSaude.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, metasSaude.getId().toString()))
            .body(metasSaude);
    }

    /**
     * {@code PUT  /metas-saudes/:id} : Updates an existing metasSaude.
     *
     * @param id the id of the metasSaude to save.
     * @param metasSaude the metasSaude to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metasSaude,
     * or with status {@code 400 (Bad Request)} if the metasSaude is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metasSaude couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MetasSaude> updateMetasSaude(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MetasSaude metasSaude
    ) throws URISyntaxException {
        log.debug("REST request to update MetasSaude : {}, {}", id, metasSaude);
        if (metasSaude.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metasSaude.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metasSaudeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        metasSaude = metasSaudeService.update(metasSaude);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metasSaude.getId().toString()))
            .body(metasSaude);
    }

    /**
     * {@code PATCH  /metas-saudes/:id} : Partial updates given fields of an existing metasSaude, field will ignore if it is null
     *
     * @param id the id of the metasSaude to save.
     * @param metasSaude the metasSaude to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metasSaude,
     * or with status {@code 400 (Bad Request)} if the metasSaude is not valid,
     * or with status {@code 404 (Not Found)} if the metasSaude is not found,
     * or with status {@code 500 (Internal Server Error)} if the metasSaude couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MetasSaude> partialUpdateMetasSaude(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MetasSaude metasSaude
    ) throws URISyntaxException {
        log.debug("REST request to partial update MetasSaude partially : {}, {}", id, metasSaude);
        if (metasSaude.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metasSaude.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metasSaudeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MetasSaude> result = metasSaudeService.partialUpdate(metasSaude);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metasSaude.getId().toString())
        );
    }

    /**
     * {@code GET  /metas-saudes} : get all the metasSaudes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metasSaudes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MetasSaude>> getAllMetasSaudes(
        MetasSaudeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get MetasSaudes by criteria: {}", criteria);

        Page<MetasSaude> page = metasSaudeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /metas-saudes/count} : count all the metasSaudes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMetasSaudes(MetasSaudeCriteria criteria) {
        log.debug("REST request to count MetasSaudes by criteria: {}", criteria);
        return ResponseEntity.ok().body(metasSaudeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /metas-saudes/:id} : get the "id" metasSaude.
     *
     * @param id the id of the metasSaude to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metasSaude, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MetasSaude> getMetasSaude(@PathVariable("id") Long id) {
        log.debug("REST request to get MetasSaude : {}", id);
        Optional<MetasSaude> metasSaude = metasSaudeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(metasSaude);
    }

    /**
     * {@code DELETE  /metas-saudes/:id} : delete the "id" metasSaude.
     *
     * @param id the id of the metasSaude to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetasSaude(@PathVariable("id") Long id) {
        log.debug("REST request to delete MetasSaude : {}", id);
        metasSaudeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
