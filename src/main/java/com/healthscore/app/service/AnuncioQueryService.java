package com.healthscore.app.service;

import com.healthscore.app.domain.*; // for static metamodels
import com.healthscore.app.domain.Anuncio;
import com.healthscore.app.repository.AnuncioRepository;
import com.healthscore.app.service.criteria.AnuncioCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Anuncio} entities in the database.
 * The main input is a {@link AnuncioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Anuncio} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnuncioQueryService extends QueryService<Anuncio> {

    private final Logger log = LoggerFactory.getLogger(AnuncioQueryService.class);

    private final AnuncioRepository anuncioRepository;

    public AnuncioQueryService(AnuncioRepository anuncioRepository) {
        this.anuncioRepository = anuncioRepository;
    }

    /**
     * Return a {@link Page} of {@link Anuncio} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Anuncio> findByCriteria(AnuncioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Anuncio> specification = createSpecification(criteria);
        return anuncioRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnuncioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Anuncio> specification = createSpecification(criteria);
        return anuncioRepository.count(specification);
    }

    /**
     * Function to convert {@link AnuncioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Anuncio> createSpecification(AnuncioCriteria criteria) {
        Specification<Anuncio> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Anuncio_.id));
            }
            if (criteria.getTitulo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitulo(), Anuncio_.titulo));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Anuncio_.descricao));
            }
            if (criteria.getDataPublicacao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataPublicacao(), Anuncio_.dataPublicacao));
            }
            if (criteria.getDataInicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataInicio(), Anuncio_.dataInicio));
            }
            if (criteria.getDataFim() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataFim(), Anuncio_.dataFim));
            }
            if (criteria.getPreco() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPreco(), Anuncio_.preco));
            }
        }
        return specification;
    }
}
