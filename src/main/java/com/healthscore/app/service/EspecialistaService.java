package com.healthscore.app.service;

import com.healthscore.app.domain.Especialista;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.healthscore.app.domain.Especialista}.
 */
public interface EspecialistaService {
    /**
     * Save a especialista.
     *
     * @param especialista the entity to save.
     * @return the persisted entity.
     */
    Especialista save(Especialista especialista);

    /**
     * Updates a especialista.
     *
     * @param especialista the entity to update.
     * @return the persisted entity.
     */
    Especialista update(Especialista especialista);

    /**
     * Partially updates a especialista.
     *
     * @param especialista the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Especialista> partialUpdate(Especialista especialista);

    /**
     * Get the "id" especialista.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Especialista> findOne(Long id);

    /**
     * Delete the "id" especialista.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
