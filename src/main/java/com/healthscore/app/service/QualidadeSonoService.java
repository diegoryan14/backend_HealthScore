package com.healthscore.app.service;

import com.healthscore.app.domain.QualidadeSono;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.healthscore.app.domain.QualidadeSono}.
 */
public interface QualidadeSonoService {
    /**
     * Save a qualidadeSono.
     *
     * @param qualidadeSono the entity to save.
     * @return the persisted entity.
     */
    QualidadeSono save(QualidadeSono qualidadeSono);

    /**
     * Updates a qualidadeSono.
     *
     * @param qualidadeSono the entity to update.
     * @return the persisted entity.
     */
    QualidadeSono update(QualidadeSono qualidadeSono);

    /**
     * Partially updates a qualidadeSono.
     *
     * @param qualidadeSono the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QualidadeSono> partialUpdate(QualidadeSono qualidadeSono);

    /**
     * Get the "id" qualidadeSono.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QualidadeSono> findOne(Long id);

    /**
     * Delete the "id" qualidadeSono.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
