package com.healthscore.app.web.rest;

import com.healthscore.app.domain.Anuncio;
import com.healthscore.app.repository.AnuncioRepository;
import com.healthscore.app.service.AnuncioQueryService;
import com.healthscore.app.service.AnuncioService;
import com.healthscore.app.service.criteria.AnuncioCriteria;
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
 * REST controller for managing {@link com.healthscore.app.domain.Anuncio}.
 */
@RestController
@RequestMapping("/api/anuncios")
public class AnuncioResource {

    private final Logger log = LoggerFactory.getLogger(AnuncioResource.class);

    private static final String ENTITY_NAME = "anuncio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnuncioService anuncioService;

    private final AnuncioRepository anuncioRepository;

    private final AnuncioQueryService anuncioQueryService;

    public AnuncioResource(AnuncioService anuncioService, AnuncioRepository anuncioRepository, AnuncioQueryService anuncioQueryService) {
        this.anuncioService = anuncioService;
        this.anuncioRepository = anuncioRepository;
        this.anuncioQueryService = anuncioQueryService;
    }

    /**
     * {@code POST  /anuncios} : Create a new anuncio.
     *
     * @param anuncio the anuncio to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new anuncio, or with status {@code 400 (Bad Request)} if the anuncio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Anuncio> createAnuncio(@RequestBody Anuncio anuncio) throws URISyntaxException {
        log.debug("REST request to save Anuncio : {}", anuncio);
        if (anuncio.getId() != null) {
            throw new BadRequestAlertException("A new anuncio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        anuncio = anuncioService.save(anuncio);
        return ResponseEntity.created(new URI("/api/anuncios/" + anuncio.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, anuncio.getId().toString()))
            .body(anuncio);
    }

    /**
     * {@code PUT  /anuncios/:id} : Updates an existing anuncio.
     *
     * @param id the id of the anuncio to save.
     * @param anuncio the anuncio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated anuncio,
     * or with status {@code 400 (Bad Request)} if the anuncio is not valid,
     * or with status {@code 500 (Internal Server Error)} if the anuncio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Anuncio> updateAnuncio(@PathVariable(value = "id", required = false) final Long id, @RequestBody Anuncio anuncio)
        throws URISyntaxException {
        log.debug("REST request to update Anuncio : {}, {}", id, anuncio);
        if (anuncio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, anuncio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!anuncioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        anuncio = anuncioService.update(anuncio);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, anuncio.getId().toString()))
            .body(anuncio);
    }

    /**
     * {@code PATCH  /anuncios/:id} : Partial updates given fields of an existing anuncio, field will ignore if it is null
     *
     * @param id the id of the anuncio to save.
     * @param anuncio the anuncio to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated anuncio,
     * or with status {@code 400 (Bad Request)} if the anuncio is not valid,
     * or with status {@code 404 (Not Found)} if the anuncio is not found,
     * or with status {@code 500 (Internal Server Error)} if the anuncio couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Anuncio> partialUpdateAnuncio(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Anuncio anuncio
    ) throws URISyntaxException {
        log.debug("REST request to partial update Anuncio partially : {}, {}", id, anuncio);
        if (anuncio.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, anuncio.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!anuncioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Anuncio> result = anuncioService.partialUpdate(anuncio);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, anuncio.getId().toString())
        );
    }

    /**
     * {@code GET  /anuncios} : get all the anuncios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of anuncios in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Anuncio>> getAllAnuncios(
        AnuncioCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Anuncios by criteria: {}", criteria);

        Page<Anuncio> page = anuncioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /anuncios/count} : count all the anuncios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAnuncios(AnuncioCriteria criteria) {
        log.debug("REST request to count Anuncios by criteria: {}", criteria);
        return ResponseEntity.ok().body(anuncioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /anuncios/:id} : get the "id" anuncio.
     *
     * @param id the id of the anuncio to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the anuncio, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Anuncio> getAnuncio(@PathVariable("id") Long id) {
        log.debug("REST request to get Anuncio : {}", id);
        Optional<Anuncio> anuncio = anuncioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(anuncio);
    }

    /**
     * {@code DELETE  /anuncios/:id} : delete the "id" anuncio.
     *
     * @param id the id of the anuncio to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnuncio(@PathVariable("id") Long id) {
        log.debug("REST request to delete Anuncio : {}", id);
        anuncioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
