package com.healthscore.app.service;

import com.healthscore.app.domain.Dieta;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.healthscore.app.domain.Dieta}.
 */
public interface DietaService {
    /**
     * Save a dieta.
     *
     * @param dieta the entity to save.
     * @return the persisted entity.
     */
    Dieta save(Dieta dieta);

    /**
     * Updates a dieta.
     *
     * @param dieta the entity to update.
     * @return the persisted entity.
     */
    Dieta update(Dieta dieta);

    /**
     * Partially updates a dieta.
     *
     * @param dieta the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Dieta> partialUpdate(Dieta dieta);

    /**
     * Get the "id" dieta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Dieta> findOne(Long id);

    /**
     * Delete the "id" dieta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
