package com.healthscore.app.web.rest;

import com.healthscore.app.domain.PontuacaoUsuario;
import com.healthscore.app.repository.PontuacaoUsuarioRepository;
import com.healthscore.app.service.PontuacaoUsuarioQueryService;
import com.healthscore.app.service.PontuacaoUsuarioService;
import com.healthscore.app.service.criteria.PontuacaoUsuarioCriteria;
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
 * REST controller for managing {@link com.healthscore.app.domain.PontuacaoUsuario}.
 */
@RestController
@RequestMapping("/api/pontuacao-usuarios")
public class PontuacaoUsuarioResource {

    private final Logger log = LoggerFactory.getLogger(PontuacaoUsuarioResource.class);

    private static final String ENTITY_NAME = "pontuacaoUsuario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PontuacaoUsuarioService pontuacaoUsuarioService;

    private final PontuacaoUsuarioRepository pontuacaoUsuarioRepository;

    private final PontuacaoUsuarioQueryService pontuacaoUsuarioQueryService;

    public PontuacaoUsuarioResource(
        PontuacaoUsuarioService pontuacaoUsuarioService,
        PontuacaoUsuarioRepository pontuacaoUsuarioRepository,
        PontuacaoUsuarioQueryService pontuacaoUsuarioQueryService
    ) {
        this.pontuacaoUsuarioService = pontuacaoUsuarioService;
        this.pontuacaoUsuarioRepository = pontuacaoUsuarioRepository;
        this.pontuacaoUsuarioQueryService = pontuacaoUsuarioQueryService;
    }

    /**
     * {@code POST  /pontuacao-usuarios} : Create a new pontuacaoUsuario.
     *
     * @param pontuacaoUsuario the pontuacaoUsuario to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pontuacaoUsuario, or with status {@code 400 (Bad Request)} if the pontuacaoUsuario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PontuacaoUsuario> createPontuacaoUsuario(@RequestBody PontuacaoUsuario pontuacaoUsuario)
        throws URISyntaxException {
        log.debug("REST request to save PontuacaoUsuario : {}", pontuacaoUsuario);
        if (pontuacaoUsuario.getId() != null) {
            throw new BadRequestAlertException("A new pontuacaoUsuario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pontuacaoUsuario = pontuacaoUsuarioService.save(pontuacaoUsuario);
        return ResponseEntity.created(new URI("/api/pontuacao-usuarios/" + pontuacaoUsuario.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pontuacaoUsuario.getId().toString()))
            .body(pontuacaoUsuario);
    }

    /**
     * {@code PUT  /pontuacao-usuarios/:id} : Updates an existing pontuacaoUsuario.
     *
     * @param id the id of the pontuacaoUsuario to save.
     * @param pontuacaoUsuario the pontuacaoUsuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pontuacaoUsuario,
     * or with status {@code 400 (Bad Request)} if the pontuacaoUsuario is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pontuacaoUsuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PontuacaoUsuario> updatePontuacaoUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PontuacaoUsuario pontuacaoUsuario
    ) throws URISyntaxException {
        log.debug("REST request to update PontuacaoUsuario : {}, {}", id, pontuacaoUsuario);
        if (pontuacaoUsuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pontuacaoUsuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pontuacaoUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pontuacaoUsuario = pontuacaoUsuarioService.update(pontuacaoUsuario);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pontuacaoUsuario.getId().toString()))
            .body(pontuacaoUsuario);
    }

    /**
     * {@code PATCH  /pontuacao-usuarios/:id} : Partial updates given fields of an existing pontuacaoUsuario, field will ignore if it is null
     *
     * @param id the id of the pontuacaoUsuario to save.
     * @param pontuacaoUsuario the pontuacaoUsuario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pontuacaoUsuario,
     * or with status {@code 400 (Bad Request)} if the pontuacaoUsuario is not valid,
     * or with status {@code 404 (Not Found)} if the pontuacaoUsuario is not found,
     * or with status {@code 500 (Internal Server Error)} if the pontuacaoUsuario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PontuacaoUsuario> partialUpdatePontuacaoUsuario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PontuacaoUsuario pontuacaoUsuario
    ) throws URISyntaxException {
        log.debug("REST request to partial update PontuacaoUsuario partially : {}, {}", id, pontuacaoUsuario);
        if (pontuacaoUsuario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pontuacaoUsuario.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pontuacaoUsuarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PontuacaoUsuario> result = pontuacaoUsuarioService.partialUpdate(pontuacaoUsuario);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pontuacaoUsuario.getId().toString())
        );
    }

    /**
     * {@code GET  /pontuacao-usuarios} : get all the pontuacaoUsuarios.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pontuacaoUsuarios in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PontuacaoUsuario>> getAllPontuacaoUsuarios(
        PontuacaoUsuarioCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PontuacaoUsuarios by criteria: {}", criteria);

        Page<PontuacaoUsuario> page = pontuacaoUsuarioQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pontuacao-usuarios/count} : count all the pontuacaoUsuarios.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPontuacaoUsuarios(PontuacaoUsuarioCriteria criteria) {
        log.debug("REST request to count PontuacaoUsuarios by criteria: {}", criteria);
        return ResponseEntity.ok().body(pontuacaoUsuarioQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pontuacao-usuarios/:id} : get the "id" pontuacaoUsuario.
     *
     * @param id the id of the pontuacaoUsuario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pontuacaoUsuario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PontuacaoUsuario> getPontuacaoUsuario(@PathVariable("id") Long id) {
        log.debug("REST request to get PontuacaoUsuario : {}", id);
        Optional<PontuacaoUsuario> pontuacaoUsuario = pontuacaoUsuarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pontuacaoUsuario);
    }

    /**
     * {@code DELETE  /pontuacao-usuarios/:id} : delete the "id" pontuacaoUsuario.
     *
     * @param id the id of the pontuacaoUsuario to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePontuacaoUsuario(@PathVariable("id") Long id) {
        log.debug("REST request to delete PontuacaoUsuario : {}", id);
        pontuacaoUsuarioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
