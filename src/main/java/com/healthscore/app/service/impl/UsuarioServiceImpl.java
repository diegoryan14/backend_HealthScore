package com.healthscore.app.service.impl;

import com.healthscore.app.domain.Usuario;
import com.healthscore.app.repository.UsuarioRepository;
import com.healthscore.app.service.UsuarioService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.healthscore.app.domain.Usuario}.
 */
@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        log.debug("Request to save Usuario : {}", usuario);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario update(Usuario usuario) {
        log.debug("Request to update Usuario : {}", usuario);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> partialUpdate(Usuario usuario) {
        log.debug("Request to partially update Usuario : {}", usuario);

        return usuarioRepository
            .findById(usuario.getId())
            .map(existingUsuario -> {
                if (usuario.getPlano() != null) {
                    existingUsuario.setPlano(usuario.getPlano());
                }
                if (usuario.getDataRegistro() != null) {
                    existingUsuario.setDataRegistro(usuario.getDataRegistro());
                }
                if (usuario.getTelefone() != null) {
                    existingUsuario.setTelefone(usuario.getTelefone());
                }
                if (usuario.getEmail() != null) {
                    existingUsuario.setEmail(usuario.getEmail());
                }
                if (usuario.getDataNascimento() != null) {
                    existingUsuario.setDataNascimento(usuario.getDataNascimento());
                }
                if (usuario.getMetaConsumoAgua() != null) {
                    existingUsuario.setMetaConsumoAgua(usuario.getMetaConsumoAgua());
                }
                if (usuario.getMetaSono() != null) {
                    existingUsuario.setMetaSono(usuario.getMetaSono());
                }
                if (usuario.getMetaCaloriasConsumidas() != null) {
                    existingUsuario.setMetaCaloriasConsumidas(usuario.getMetaCaloriasConsumidas());
                }
                if (usuario.getMetaCaloriasQueimadas() != null) {
                    existingUsuario.setMetaCaloriasQueimadas(usuario.getMetaCaloriasQueimadas());
                }
                if (usuario.getPontosUser() != null) {
                    existingUsuario.setPontosUser(usuario.getPontosUser());
                }

                return existingUsuario;
            })
            .map(usuarioRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findOne(Long id) {
        log.debug("Request to get Usuario : {}", id);
        return usuarioRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Usuario : {}", id);
        usuarioRepository.deleteById(id);
    }
}
