package com.healthscore.app.service;

import com.healthscore.app.domain.*; // for static metamodels
import com.healthscore.app.domain.ConsumoAgua;
import com.healthscore.app.repository.ConsumoAguaRepository;
import com.healthscore.app.service.criteria.ConsumoAguaCriteria;
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
 * Service for executing complex queries for {@link ConsumoAgua} entities in the database.
 * The main input is a {@link ConsumoAguaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ConsumoAgua} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConsumoAguaQueryService extends QueryService<ConsumoAgua> {

    private final Logger log = LoggerFactory.getLogger(ConsumoAguaQueryService.class);

    private final ConsumoAguaRepository consumoAguaRepository;

    public ConsumoAguaQueryService(ConsumoAguaRepository consumoAguaRepository) {
        this.consumoAguaRepository = consumoAguaRepository;
    }

    /**
     * Return a {@link Page} of {@link ConsumoAgua} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConsumoAgua> findByCriteria(ConsumoAguaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ConsumoAgua> specification = createSpecification(criteria);
        return consumoAguaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConsumoAguaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ConsumoAgua> specification = createSpecification(criteria);
        return consumoAguaRepository.count(specification);
    }

    /**
     * Function to convert {@link ConsumoAguaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ConsumoAgua> createSpecification(ConsumoAguaCriteria criteria) {
        Specification<ConsumoAgua> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ConsumoAgua_.id));
            }
            if (criteria.getDataConsumo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataConsumo(), ConsumoAgua_.dataConsumo));
            }
            if (criteria.getQuantidadeMl() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantidadeMl(), ConsumoAgua_.quantidadeMl));
            }
            if (criteria.getInternalUserId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getInternalUserId(),
                        root -> root.join(ConsumoAgua_.internalUser, JoinType.LEFT).get(User_.id)
                    )
                );
            }
        }
        return specification;
    }
}
