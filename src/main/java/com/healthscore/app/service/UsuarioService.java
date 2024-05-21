package com.healthscore.app.service;

import com.healthscore.app.domain.Usuario;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.healthscore.app.domain.Usuario}.
 */
public interface UsuarioService {
    /**
     * Save a usuario.
     *
     * @param usuario the entity to save.
     * @return the persisted entity.
     */
    Usuario save(Usuario usuario);

    /**
     * Updates a usuario.
     *
     * @param usuario the entity to update.
     * @return the persisted entity.
     */
    Usuario update(Usuario usuario);

    /**
     * Partially updates a usuario.
     *
     * @param usuario the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Usuario> partialUpdate(Usuario usuario);

    /**
     * Get the "id" usuario.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Usuario> findOne(Long id);

    /**
     * Delete the "id" usuario.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
