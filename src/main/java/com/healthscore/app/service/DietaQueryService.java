package com.healthscore.app.service;

import com.healthscore.app.domain.*; // for static metamodels
import com.healthscore.app.domain.Dieta;
import com.healthscore.app.repository.DietaRepository;
import com.healthscore.app.service.criteria.DietaCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Dieta} entities in the database.
 * The main input is a {@link DietaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Dieta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DietaQueryService extends QueryService<Dieta> {

    private final Logger log = LoggerFactory.getLogger(DietaQueryService.class);

    private final DietaRepository dietaRepository;

    public DietaQueryService(DietaRepository dietaRepository) {
        this.dietaRepository = dietaRepository;
    }

    /**
     * Return a {@link Page} of {@link Dieta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Dieta> findByCriteria(DietaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Dieta> specification = createSpecification(criteria);
        return dietaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DietaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Dieta> specification = createSpecification(criteria);
        return dietaRepository.count(specification);
    }

    /**
     * Function to convert {@link DietaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Dieta> createSpecification(DietaCriteria criteria) {
        Specification<Dieta> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Dieta_.id));
            }
            if (criteria.getDescricaoRefeicao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricaoRefeicao(), Dieta_.descricaoRefeicao));
            }
            if (criteria.getDataHorarioRefeicao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataHorarioRefeicao(), Dieta_.dataHorarioRefeicao));
            }
            if (criteria.getCaloriasConsumidas() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCaloriasConsumidas(), Dieta_.caloriasConsumidas));
            }
            if (criteria.getInternalUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getInternalUserId(), root -> root.join(Dieta_.internalUser, JoinType.LEFT).get(User_.id))
                );
            }
        }
        return specification;
    }
}
