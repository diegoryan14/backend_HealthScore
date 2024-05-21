package com.healthscore.app.service;

import com.healthscore.app.domain.ConsultaEspecialista;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.healthscore.app.domain.ConsultaEspecialista}.
 */
public interface ConsultaEspecialistaService {
    /**
     * Save a consultaEspecialista.
     *
     * @param consultaEspecialista the entity to save.
     * @return the persisted entity.
     */
    ConsultaEspecialista save(ConsultaEspecialista consultaEspecialista);

    /**
     * Updates a consultaEspecialista.
     *
     * @param consultaEspecialista the entity to update.
     * @return the persisted entity.
     */
    ConsultaEspecialista update(ConsultaEspecialista consultaEspecialista);

    /**
     * Partially updates a consultaEspecialista.
     *
     * @param consultaEspecialista the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ConsultaEspecialista> partialUpdate(ConsultaEspecialista consultaEspecialista);

    /**
     * Get the "id" consultaEspecialista.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConsultaEspecialista> findOne(Long id);

    /**
     * Delete the "id" consultaEspecialista.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
