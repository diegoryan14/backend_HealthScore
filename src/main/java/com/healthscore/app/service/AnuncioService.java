package com.healthscore.app.service;

import com.healthscore.app.domain.Anuncio;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.healthscore.app.domain.Anuncio}.
 */
public interface AnuncioService {
    /**
     * Save a anuncio.
     *
     * @param anuncio the entity to save.
     * @return the persisted entity.
     */
    Anuncio save(Anuncio anuncio);

    /**
     * Updates a anuncio.
     *
     * @param anuncio the entity to update.
     * @return the persisted entity.
     */
    Anuncio update(Anuncio anuncio);

    /**
     * Partially updates a anuncio.
     *
     * @param anuncio the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Anuncio> partialUpdate(Anuncio anuncio);

    /**
     * Get the "id" anuncio.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Anuncio> findOne(Long id);

    /**
     * Delete the "id" anuncio.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
