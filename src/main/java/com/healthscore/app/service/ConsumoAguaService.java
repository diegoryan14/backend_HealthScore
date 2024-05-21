package com.healthscore.app.service;

import com.healthscore.app.domain.ConsumoAgua;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.healthscore.app.domain.ConsumoAgua}.
 */
public interface ConsumoAguaService {
    /**
     * Save a consumoAgua.
     *
     * @param consumoAgua the entity to save.
     * @return the persisted entity.
     */
    ConsumoAgua save(ConsumoAgua consumoAgua);

    /**
     * Updates a consumoAgua.
     *
     * @param consumoAgua the entity to update.
     * @return the persisted entity.
     */
    ConsumoAgua update(ConsumoAgua consumoAgua);

    /**
     * Partially updates a consumoAgua.
     *
     * @param consumoAgua the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ConsumoAgua> partialUpdate(ConsumoAgua consumoAgua);

    /**
     * Get the "id" consumoAgua.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConsumoAgua> findOne(Long id);

    /**
     * Delete the "id" consumoAgua.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
