package com.healthscore.app.service;

import com.healthscore.app.domain.*; // for static metamodels
import com.healthscore.app.domain.AtividadeFisica;
import com.healthscore.app.repository.AtividadeFisicaRepository;
import com.healthscore.app.service.criteria.AtividadeFisicaCriteria;
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
 * Service for executing complex queries for {@link AtividadeFisica} entities in the database.
 * The main input is a {@link AtividadeFisicaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AtividadeFisica} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AtividadeFisicaQueryService extends QueryService<AtividadeFisica> {

    private final Logger log = LoggerFactory.getLogger(AtividadeFisicaQueryService.class);

    private final AtividadeFisicaRepository atividadeFisicaRepository;

    public AtividadeFisicaQueryService(AtividadeFisicaRepository atividadeFisicaRepository) {
        this.atividadeFisicaRepository = atividadeFisicaRepository;
    }

    /**
     * Return a {@link Page} of {@link AtividadeFisica} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AtividadeFisica> findByCriteria(AtividadeFisicaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AtividadeFisica> specification = createSpecification(criteria);
        return atividadeFisicaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AtividadeFisicaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AtividadeFisica> specification = createSpecification(criteria);
        return atividadeFisicaRepository.count(specification);
    }

    /**
     * Function to convert {@link AtividadeFisicaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AtividadeFisica> createSpecification(AtividadeFisicaCriteria criteria) {
        Specification<AtividadeFisica> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AtividadeFisica_.id));
            }
            if (criteria.getTipoAtividade() != null) {
                specification = specification.and(buildSpecification(criteria.getTipoAtividade(), AtividadeFisica_.tipoAtividade));
            }
            if (criteria.getDataHorario() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataHorario(), AtividadeFisica_.dataHorario));
            }
            if (criteria.getDuracao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDuracao(), AtividadeFisica_.duracao));
            }
            if (criteria.getPassosCalorias() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPassosCalorias(), AtividadeFisica_.passosCalorias));
            }
            if (criteria.getInternalUserId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getInternalUserId(),
                        root -> root.join(AtividadeFisica_.internalUser, JoinType.LEFT).get(User_.id)
                    )
                );
            }
        }
        return specification;
    }
}
