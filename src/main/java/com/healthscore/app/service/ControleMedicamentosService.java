package com.healthscore.app.service;

import com.healthscore.app.domain.ControleMedicamentos;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.healthscore.app.domain.ControleMedicamentos}.
 */
public interface ControleMedicamentosService {
    /**
     * Save a controleMedicamentos.
     *
     * @param controleMedicamentos the entity to save.
     * @return the persisted entity.
     */
    ControleMedicamentos save(ControleMedicamentos controleMedicamentos);

    /**
     * Updates a controleMedicamentos.
     *
     * @param controleMedicamentos the entity to update.
     * @return the persisted entity.
     */
    ControleMedicamentos update(ControleMedicamentos controleMedicamentos);

    /**
     * Partially updates a controleMedicamentos.
     *
     * @param controleMedicamentos the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ControleMedicamentos> partialUpdate(ControleMedicamentos controleMedicamentos);

    /**
     * Get the "id" controleMedicamentos.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ControleMedicamentos> findOne(Long id);

    /**
     * Delete the "id" controleMedicamentos.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
