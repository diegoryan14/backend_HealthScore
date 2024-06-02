package com.healthscore.app.service;

import com.healthscore.app.domain.PontuacaoUsuario;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.healthscore.app.domain.PontuacaoUsuario}.
 */
public interface PontuacaoUsuarioService {
    /**
     * Save a pontuacaoUsuario.
     *
     * @param pontuacaoUsuario the entity to save.
     * @return the persisted entity.
     */
    PontuacaoUsuario save(PontuacaoUsuario pontuacaoUsuario);

    /**
     * Updates a pontuacaoUsuario.
     *
     * @param pontuacaoUsuario the entity to update.
     * @return the persisted entity.
     */
    PontuacaoUsuario update(PontuacaoUsuario pontuacaoUsuario);

    /**
     * Partially updates a pontuacaoUsuario.
     *
     * @param pontuacaoUsuario the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PontuacaoUsuario> partialUpdate(PontuacaoUsuario pontuacaoUsuario);

    /**
     * Get the "id" pontuacaoUsuario.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PontuacaoUsuario> findOne(Long id);

    /**
     * Delete the "id" pontuacaoUsuario.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
