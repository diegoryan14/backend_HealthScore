package com.healthscore.app.service;

import com.healthscore.app.domain.*; // for static metamodels
import com.healthscore.app.domain.ConsultaEspecialista;
import com.healthscore.app.repository.ConsultaEspecialistaRepository;
import com.healthscore.app.service.criteria.ConsultaEspecialistaCriteria;
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
 * Service for executing complex queries for {@link ConsultaEspecialista} entities in the database.
 * The main input is a {@link ConsultaEspecialistaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ConsultaEspecialista} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConsultaEspecialistaQueryService extends QueryService<ConsultaEspecialista> {

    private final Logger log = LoggerFactory.getLogger(ConsultaEspecialistaQueryService.class);

    private final ConsultaEspecialistaRepository consultaEspecialistaRepository;

    public ConsultaEspecialistaQueryService(ConsultaEspecialistaRepository consultaEspecialistaRepository) {
        this.consultaEspecialistaRepository = consultaEspecialistaRepository;
    }

    /**
     * Return a {@link Page} of {@link ConsultaEspecialista} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConsultaEspecialista> findByCriteria(ConsultaEspecialistaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ConsultaEspecialista> specification = createSpecification(criteria);
        return consultaEspecialistaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConsultaEspecialistaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ConsultaEspecialista> specification = createSpecification(criteria);
        return consultaEspecialistaRepository.count(specification);
    }

    /**
     * Function to convert {@link ConsultaEspecialistaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ConsultaEspecialista> createSpecification(ConsultaEspecialistaCriteria criteria) {
        Specification<ConsultaEspecialista> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ConsultaEspecialista_.id));
            }
            if (criteria.getTipoEspecialista() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTipoEspecialista(), ConsultaEspecialista_.tipoEspecialista)
                );
            }
            if (criteria.getDataHorarioConsulta() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getDataHorarioConsulta(), ConsultaEspecialista_.dataHorarioConsulta)
                );
            }
            if (criteria.getStatusConsulta() != null) {
                specification = specification.and(buildSpecification(criteria.getStatusConsulta(), ConsultaEspecialista_.statusConsulta));
            }
            if (criteria.getLinkConsulta() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLinkConsulta(), ConsultaEspecialista_.linkConsulta));
            }
            if (criteria.getInternalUserId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getInternalUserId(),
                        root -> root.join(ConsultaEspecialista_.internalUser, JoinType.LEFT).get(User_.id)
                    )
                );
            }
            if (criteria.getEspecialistaId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getEspecialistaId(),
                        root -> root.join(ConsultaEspecialista_.especialista, JoinType.LEFT).get(Especialista_.id)
                    )
                );
            }
        }
        return specification;
    }
}
