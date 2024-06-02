package com.healthscore.app.service.impl;

import com.healthscore.app.domain.PontuacaoUsuario;
import com.healthscore.app.repository.PontuacaoUsuarioRepository;
import com.healthscore.app.service.PontuacaoUsuarioService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.healthscore.app.domain.PontuacaoUsuario}.
 */
@Service
@Transactional
public class PontuacaoUsuarioServiceImpl implements PontuacaoUsuarioService {

    private final Logger log = LoggerFactory.getLogger(PontuacaoUsuarioServiceImpl.class);

    private final PontuacaoUsuarioRepository pontuacaoUsuarioRepository;

    public PontuacaoUsuarioServiceImpl(PontuacaoUsuarioRepository pontuacaoUsuarioRepository) {
        this.pontuacaoUsuarioRepository = pontuacaoUsuarioRepository;
    }

    @Override
    public PontuacaoUsuario save(PontuacaoUsuario pontuacaoUsuario) {
        log.debug("Request to save PontuacaoUsuario : {}", pontuacaoUsuario);
        return pontuacaoUsuarioRepository.save(pontuacaoUsuario);
    }

    @Override
    public PontuacaoUsuario update(PontuacaoUsuario pontuacaoUsuario) {
        log.debug("Request to update PontuacaoUsuario : {}", pontuacaoUsuario);
        return pontuacaoUsuarioRepository.save(pontuacaoUsuario);
    }

    @Override
    public Optional<PontuacaoUsuario> partialUpdate(PontuacaoUsuario pontuacaoUsuario) {
        log.debug("Request to partially update PontuacaoUsuario : {}", pontuacaoUsuario);

        return pontuacaoUsuarioRepository
            .findById(pontuacaoUsuario.getId())
            .map(existingPontuacaoUsuario -> {
                if (pontuacaoUsuario.getDataAlteracao() != null) {
                    existingPontuacaoUsuario.setDataAlteracao(pontuacaoUsuario.getDataAlteracao());
                }
                if (pontuacaoUsuario.getValorAlteracao() != null) {
                    existingPontuacaoUsuario.setValorAlteracao(pontuacaoUsuario.getValorAlteracao());
                }
                if (pontuacaoUsuario.getMotivo() != null) {
                    existingPontuacaoUsuario.setMotivo(pontuacaoUsuario.getMotivo());
                }

                return existingPontuacaoUsuario;
            })
            .map(pontuacaoUsuarioRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PontuacaoUsuario> findOne(Long id) {
        log.debug("Request to get PontuacaoUsuario : {}", id);
        return pontuacaoUsuarioRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PontuacaoUsuario : {}", id);
        pontuacaoUsuarioRepository.deleteById(id);
    }
}
