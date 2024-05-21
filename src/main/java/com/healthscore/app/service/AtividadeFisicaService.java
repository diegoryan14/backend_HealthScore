package com.healthscore.app.service;

import com.healthscore.app.domain.AtividadeFisica;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.healthscore.app.domain.AtividadeFisica}.
 */
public interface AtividadeFisicaService {
    /**
     * Save a atividadeFisica.
     *
     * @param atividadeFisica the entity to save.
     * @return the persisted entity.
     */
    AtividadeFisica save(AtividadeFisica atividadeFisica);

    /**
     * Updates a atividadeFisica.
     *
     * @param atividadeFisica the entity to update.
     * @return the persisted entity.
     */
    AtividadeFisica update(AtividadeFisica atividadeFisica);

    /**
     * Partially updates a atividadeFisica.
     *
     * @param atividadeFisica the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AtividadeFisica> partialUpdate(AtividadeFisica atividadeFisica);

    /**
     * Get the "id" atividadeFisica.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AtividadeFisica> findOne(Long id);

    /**
     * Delete the "id" atividadeFisica.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
