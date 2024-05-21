package com.healthscore.app.web.rest;

import com.healthscore.app.domain.ConsumoAgua;
import com.healthscore.app.repository.ConsumoAguaRepository;
import com.healthscore.app.service.ConsumoAguaQueryService;
import com.healthscore.app.service.ConsumoAguaService;
import com.healthscore.app.service.criteria.ConsumoAguaCriteria;
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
 * REST controller for managing {@link com.healthscore.app.domain.ConsumoAgua}.
 */
@RestController
@RequestMapping("/api/consumo-aguas")
public class ConsumoAguaResource {

    private final Logger log = LoggerFactory.getLogger(ConsumoAguaResource.class);

    private static final String ENTITY_NAME = "consumoAgua";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConsumoAguaService consumoAguaService;

    private final ConsumoAguaRepository consumoAguaRepository;

    private final ConsumoAguaQueryService consumoAguaQueryService;

    public ConsumoAguaResource(
        ConsumoAguaService consumoAguaService,
        ConsumoAguaRepository consumoAguaRepository,
        ConsumoAguaQueryService consumoAguaQueryService
    ) {
        this.consumoAguaService = consumoAguaService;
        this.consumoAguaRepository = consumoAguaRepository;
        this.consumoAguaQueryService = consumoAguaQueryService;
    }

    /**
     * {@code POST  /consumo-aguas} : Create a new consumoAgua.
     *
     * @param consumoAgua the consumoAgua to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new consumoAgua, or with status {@code 400 (Bad Request)} if the consumoAgua has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ConsumoAgua> createConsumoAgua(@RequestBody ConsumoAgua consumoAgua) throws URISyntaxException {
        log.debug("REST request to save ConsumoAgua : {}", consumoAgua);
        if (consumoAgua.getId() != null) {
            throw new BadRequestAlertException("A new consumoAgua cannot already have an ID", ENTITY_NAME, "idexists");
        }
        consumoAgua = consumoAguaService.save(consumoAgua);
        return ResponseEntity.created(new URI("/api/consumo-aguas/" + consumoAgua.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, consumoAgua.getId().toString()))
            .body(consumoAgua);
    }

    /**
     * {@code PUT  /consumo-aguas/:id} : Updates an existing consumoAgua.
     *
     * @param id the id of the consumoAgua to save.
     * @param consumoAgua the consumoAgua to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consumoAgua,
     * or with status {@code 400 (Bad Request)} if the consumoAgua is not valid,
     * or with status {@code 500 (Internal Server Error)} if the consumoAgua couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConsumoAgua> updateConsumoAgua(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConsumoAgua consumoAgua
    ) throws URISyntaxException {
        log.debug("REST request to update ConsumoAgua : {}, {}", id, consumoAgua);
        if (consumoAgua.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consumoAgua.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consumoAguaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        consumoAgua = consumoAguaService.update(consumoAgua);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consumoAgua.getId().toString()))
            .body(consumoAgua);
    }

    /**
     * {@code PATCH  /consumo-aguas/:id} : Partial updates given fields of an existing consumoAgua, field will ignore if it is null
     *
     * @param id the id of the consumoAgua to save.
     * @param consumoAgua the consumoAgua to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated consumoAgua,
     * or with status {@code 400 (Bad Request)} if the consumoAgua is not valid,
     * or with status {@code 404 (Not Found)} if the consumoAgua is not found,
     * or with status {@code 500 (Internal Server Error)} if the consumoAgua couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConsumoAgua> partialUpdateConsumoAgua(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ConsumoAgua consumoAgua
    ) throws URISyntaxException {
        log.debug("REST request to partial update ConsumoAgua partially : {}, {}", id, consumoAgua);
        if (consumoAgua.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, consumoAgua.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!consumoAguaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConsumoAgua> result = consumoAguaService.partialUpdate(consumoAgua);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, consumoAgua.getId().toString())
        );
    }

    /**
     * {@code GET  /consumo-aguas} : get all the consumoAguas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of consumoAguas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ConsumoAgua>> getAllConsumoAguas(
        ConsumoAguaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ConsumoAguas by criteria: {}", criteria);

        Page<ConsumoAgua> page = consumoAguaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /consumo-aguas/count} : count all the consumoAguas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countConsumoAguas(ConsumoAguaCriteria criteria) {
        log.debug("REST request to count ConsumoAguas by criteria: {}", criteria);
        return ResponseEntity.ok().body(consumoAguaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /consumo-aguas/:id} : get the "id" consumoAgua.
     *
     * @param id the id of the consumoAgua to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consumoAgua, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConsumoAgua> getConsumoAgua(@PathVariable("id") Long id) {
        log.debug("REST request to get ConsumoAgua : {}", id);
        Optional<ConsumoAgua> consumoAgua = consumoAguaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(consumoAgua);
    }

    /**
     * {@code DELETE  /consumo-aguas/:id} : delete the "id" consumoAgua.
     *
     * @param id the id of the consumoAgua to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsumoAgua(@PathVariable("id") Long id) {
        log.debug("REST request to delete ConsumoAgua : {}", id);
        consumoAguaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
