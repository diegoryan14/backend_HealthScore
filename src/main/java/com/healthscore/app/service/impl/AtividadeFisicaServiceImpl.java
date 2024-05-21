package com.healthscore.app.service.impl;

import com.healthscore.app.domain.AtividadeFisica;
import com.healthscore.app.repository.AtividadeFisicaRepository;
import com.healthscore.app.service.AtividadeFisicaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.healthscore.app.domain.AtividadeFisica}.
 */
@Service
@Transactional
public class AtividadeFisicaServiceImpl implements AtividadeFisicaService {

    private final Logger log = LoggerFactory.getLogger(AtividadeFisicaServiceImpl.class);

    private final AtividadeFisicaRepository atividadeFisicaRepository;

    public AtividadeFisicaServiceImpl(AtividadeFisicaRepository atividadeFisicaRepository) {
        this.atividadeFisicaRepository = atividadeFisicaRepository;
    }

    @Override
    public AtividadeFisica save(AtividadeFisica atividadeFisica) {
        log.debug("Request to save AtividadeFisica : {}", atividadeFisica);
        return atividadeFisicaRepository.save(atividadeFisica);
    }

    @Override
    public AtividadeFisica update(AtividadeFisica atividadeFisica) {
        log.debug("Request to update AtividadeFisica : {}", atividadeFisica);
        return atividadeFisicaRepository.save(atividadeFisica);
    }

    @Override
    public Optional<AtividadeFisica> partialUpdate(AtividadeFisica atividadeFisica) {
        log.debug("Request to partially update AtividadeFisica : {}", atividadeFisica);

        return atividadeFisicaRepository
            .findById(atividadeFisica.getId())
            .map(existingAtividadeFisica -> {
                if (atividadeFisica.getTipoAtividade() != null) {
                    existingAtividadeFisica.setTipoAtividade(atividadeFisica.getTipoAtividade());
                }
                if (atividadeFisica.getDataHorario() != null) {
                    existingAtividadeFisica.setDataHorario(atividadeFisica.getDataHorario());
                }
                if (atividadeFisica.getDuracao() != null) {
                    existingAtividadeFisica.setDuracao(atividadeFisica.getDuracao());
                }
                if (atividadeFisica.getPassosCalorias() != null) {
                    existingAtividadeFisica.setPassosCalorias(atividadeFisica.getPassosCalorias());
                }

                return existingAtividadeFisica;
            })
            .map(atividadeFisicaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AtividadeFisica> findOne(Long id) {
        log.debug("Request to get AtividadeFisica : {}", id);
        return atividadeFisicaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AtividadeFisica : {}", id);
        atividadeFisicaRepository.deleteById(id);
    }
}
