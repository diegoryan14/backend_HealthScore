package com.healthscore.app.service;

import com.healthscore.app.domain.*; // for static metamodels
import com.healthscore.app.domain.Usuario;
import com.healthscore.app.repository.UsuarioRepository;
import com.healthscore.app.service.criteria.UsuarioCriteria;
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
 * Service for executing complex queries for {@link Usuario} entities in the database.
 * The main input is a {@link UsuarioCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Usuario} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UsuarioQueryService extends QueryService<Usuario> {

    private final Logger log = LoggerFactory.getLogger(UsuarioQueryService.class);

    private final UsuarioRepository usuarioRepository;

    public UsuarioQueryService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Return a {@link Page} of {@link Usuario} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Usuario> findByCriteria(UsuarioCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Usuario> specification = createSpecification(criteria);
        return usuarioRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UsuarioCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Usuario> specification = createSpecification(criteria);
        return usuarioRepository.count(specification);
    }

    /**
     * Function to convert {@link UsuarioCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Usuario> createSpecification(UsuarioCriteria criteria) {
        Specification<Usuario> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Usuario_.id));
            }
            if (criteria.getPlano() != null) {
                specification = specification.and(buildSpecification(criteria.getPlano(), Usuario_.plano));
            }
            if (criteria.getDataRegistro() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataRegistro(), Usuario_.dataRegistro));
            }
            if (criteria.getTelefone() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTelefone(), Usuario_.telefone));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Usuario_.email));
            }
            if (criteria.getDataNascimento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataNascimento(), Usuario_.dataNascimento));
            }
            if (criteria.getMetaConsumoAgua() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMetaConsumoAgua(), Usuario_.metaConsumoAgua));
            }
            if (criteria.getMetaSono() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMetaSono(), Usuario_.metaSono));
            }
            if (criteria.getMetaCaloriasConsumidas() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getMetaCaloriasConsumidas(), Usuario_.metaCaloriasConsumidas)
                );
            }
            if (criteria.getMetaCaloriasQueimadas() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getMetaCaloriasQueimadas(), Usuario_.metaCaloriasQueimadas)
                );
            }
            if (criteria.getPontosUser() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPontosUser(), Usuario_.pontosUser));
            }
            if (criteria.getInternalUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getInternalUserId(), root -> root.join(Usuario_.internalUser, JoinType.LEFT).get(User_.id))
                );
            }
        }
        return specification;
    }
}
