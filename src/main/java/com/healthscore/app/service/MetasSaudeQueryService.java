package com.healthscore.app.service;

import com.healthscore.app.domain.*; // for static metamodels
import com.healthscore.app.domain.MetasSaude;
import com.healthscore.app.repository.MetasSaudeRepository;
import com.healthscore.app.service.criteria.MetasSaudeCriteria;
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
 * Service for executing complex queries for {@link MetasSaude} entities in the database.
 * The main input is a {@link MetasSaudeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MetasSaude} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MetasSaudeQueryService extends QueryService<MetasSaude> {

    private final Logger log = LoggerFactory.getLogger(MetasSaudeQueryService.class);

    private final MetasSaudeRepository metasSaudeRepository;

    public MetasSaudeQueryService(MetasSaudeRepository metasSaudeRepository) {
        this.metasSaudeRepository = metasSaudeRepository;
    }

    /**
     * Return a {@link Page} of {@link MetasSaude} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MetasSaude> findByCriteria(MetasSaudeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MetasSaude> specification = createSpecification(criteria);
        return metasSaudeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MetasSaudeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MetasSaude> specification = createSpecification(criteria);
        return metasSaudeRepository.count(specification);
    }

    /**
     * Function to convert {@link MetasSaudeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MetasSaude> createSpecification(MetasSaudeCriteria criteria) {
        Specification<MetasSaude> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MetasSaude_.id));
            }
            if (criteria.getTipoMeta() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoMeta(), MetasSaude_.tipoMeta));
            }
            if (criteria.getValorMeta() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorMeta(), MetasSaude_.valorMeta));
            }
            if (criteria.getDataInicio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataInicio(), MetasSaude_.dataInicio));
            }
            if (criteria.getDataFim() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataFim(), MetasSaude_.dataFim));
            }
            if (criteria.getInternalUserId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getInternalUserId(),
                        root -> root.join(MetasSaude_.internalUser, JoinType.LEFT).get(User_.id)
                    )
                );
            }
        }
        return specification;
    }
}
