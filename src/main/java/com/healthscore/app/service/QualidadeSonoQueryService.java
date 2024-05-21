package com.healthscore.app.service;

import com.healthscore.app.domain.*; // for static metamodels
import com.healthscore.app.domain.QualidadeSono;
import com.healthscore.app.repository.QualidadeSonoRepository;
import com.healthscore.app.service.criteria.QualidadeSonoCriteria;
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
 * Service for executing complex queries for {@link QualidadeSono} entities in the database.
 * The main input is a {@link QualidadeSonoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link QualidadeSono} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QualidadeSonoQueryService extends QueryService<QualidadeSono> {

    private final Logger log = LoggerFactory.getLogger(QualidadeSonoQueryService.class);

    private final QualidadeSonoRepository qualidadeSonoRepository;

    public QualidadeSonoQueryService(QualidadeSonoRepository qualidadeSonoRepository) {
        this.qualidadeSonoRepository = qualidadeSonoRepository;
    }

    /**
     * Return a {@link Page} of {@link QualidadeSono} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QualidadeSono> findByCriteria(QualidadeSonoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QualidadeSono> specification = createSpecification(criteria);
        return qualidadeSonoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QualidadeSonoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<QualidadeSono> specification = createSpecification(criteria);
        return qualidadeSonoRepository.count(specification);
    }

    /**
     * Function to convert {@link QualidadeSonoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QualidadeSono> createSpecification(QualidadeSonoCriteria criteria) {
        Specification<QualidadeSono> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), QualidadeSono_.id));
            }
            if (criteria.getData() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getData(), QualidadeSono_.data));
            }
            if (criteria.getHorasSono() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHorasSono(), QualidadeSono_.horasSono));
            }
            if (criteria.getInternalUserId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getInternalUserId(),
                        root -> root.join(QualidadeSono_.internalUser, JoinType.LEFT).get(User_.id)
                    )
                );
            }
        }
        return specification;
    }
}
