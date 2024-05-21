package com.healthscore.app.service;

import com.healthscore.app.domain.*; // for static metamodels
import com.healthscore.app.domain.ControleMedicamentos;
import com.healthscore.app.repository.ControleMedicamentosRepository;
import com.healthscore.app.service.criteria.ControleMedicamentosCriteria;
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
 * Service for executing complex queries for {@link ControleMedicamentos} entities in the database.
 * The main input is a {@link ControleMedicamentosCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ControleMedicamentos} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ControleMedicamentosQueryService extends QueryService<ControleMedicamentos> {

    private final Logger log = LoggerFactory.getLogger(ControleMedicamentosQueryService.class);

    private final ControleMedicamentosRepository controleMedicamentosRepository;

    public ControleMedicamentosQueryService(ControleMedicamentosRepository controleMedicamentosRepository) {
        this.controleMedicamentosRepository = controleMedicamentosRepository;
    }

    /**
     * Return a {@link Page} of {@link ControleMedicamentos} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ControleMedicamentos> findByCriteria(ControleMedicamentosCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ControleMedicamentos> specification = createSpecification(criteria);
        return controleMedicamentosRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ControleMedicamentosCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ControleMedicamentos> specification = createSpecification(criteria);
        return controleMedicamentosRepository.count(specification);
    }

    /**
     * Function to convert {@link ControleMedicamentosCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ControleMedicamentos> createSpecification(ControleMedicamentosCriteria criteria) {
        Specification<ControleMedicamentos> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ControleMedicamentos_.id));
            }
            if (criteria.getNomeMedicamento() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getNomeMedicamento(), ControleMedicamentos_.nomeMedicamento)
                );
            }
            if (criteria.getDosagem() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDosagem(), ControleMedicamentos_.dosagem));
            }
            if (criteria.getHorarioIngestao() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getHorarioIngestao(), ControleMedicamentos_.horarioIngestao)
                );
            }
            if (criteria.getInternalUserId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getInternalUserId(),
                        root -> root.join(ControleMedicamentos_.internalUser, JoinType.LEFT).get(User_.id)
                    )
                );
            }
        }
        return specification;
    }
}
