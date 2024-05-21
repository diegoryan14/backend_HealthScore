package com.healthscore.app.service;

import com.healthscore.app.domain.*; // for static metamodels
import com.healthscore.app.domain.Especialista;
import com.healthscore.app.repository.EspecialistaRepository;
import com.healthscore.app.service.criteria.EspecialistaCriteria;
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
 * Service for executing complex queries for {@link Especialista} entities in the database.
 * The main input is a {@link EspecialistaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Especialista} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EspecialistaQueryService extends QueryService<Especialista> {

    private final Logger log = LoggerFactory.getLogger(EspecialistaQueryService.class);

    private final EspecialistaRepository especialistaRepository;

    public EspecialistaQueryService(EspecialistaRepository especialistaRepository) {
        this.especialistaRepository = especialistaRepository;
    }

    /**
     * Return a {@link Page} of {@link Especialista} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Especialista> findByCriteria(EspecialistaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Especialista> specification = createSpecification(criteria);
        return especialistaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EspecialistaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Especialista> specification = createSpecification(criteria);
        return especialistaRepository.count(specification);
    }

    /**
     * Function to convert {@link EspecialistaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Especialista> createSpecification(EspecialistaCriteria criteria) {
        Specification<Especialista> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Especialista_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), Especialista_.nome));
            }
            if (criteria.getCpf() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCpf(), Especialista_.cpf));
            }
            if (criteria.getEspecializacao() != null) {
                specification = specification.and(buildSpecification(criteria.getEspecializacao(), Especialista_.especializacao));
            }
            if (criteria.getDataFormacao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataFormacao(), Especialista_.dataFormacao));
            }
            if (criteria.getTelefone() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTelefone(), Especialista_.telefone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Especialista_.email));
            }
            if (criteria.getDataNascimento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataNascimento(), Especialista_.dataNascimento));
            }
            if (criteria.getConsultasId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getConsultasId(),
                        root -> root.join(Especialista_.consultas, JoinType.LEFT).get(ConsultaEspecialista_.id)
                    )
                );
            }
        }
        return specification;
    }
}
