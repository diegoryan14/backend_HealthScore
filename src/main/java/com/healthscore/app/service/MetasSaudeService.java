package com.healthscore.app.service;

import com.healthscore.app.domain.MetasSaude;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.healthscore.app.domain.MetasSaude}.
 */
public interface MetasSaudeService {
    /**
     * Save a metasSaude.
     *
     * @param metasSaude the entity to save.
     * @return the persisted entity.
     */
    MetasSaude save(MetasSaude metasSaude);

    /**
     * Updates a metasSaude.
     *
     * @param metasSaude the entity to update.
     * @return the persisted entity.
     */
    MetasSaude update(MetasSaude metasSaude);

    /**
     * Partially updates a metasSaude.
     *
     * @param metasSaude the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MetasSaude> partialUpdate(MetasSaude metasSaude);

    /**
     * Get the "id" metasSaude.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MetasSaude> findOne(Long id);

    /**
     * Delete the "id" metasSaude.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
