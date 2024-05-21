package com.healthscore.app.service.impl;

import com.healthscore.app.domain.ControleMedicamentos;
import com.healthscore.app.repository.ControleMedicamentosRepository;
import com.healthscore.app.service.ControleMedicamentosService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.healthscore.app.domain.ControleMedicamentos}.
 */
@Service
@Transactional
public class ControleMedicamentosServiceImpl implements ControleMedicamentosService {

    private final Logger log = LoggerFactory.getLogger(ControleMedicamentosServiceImpl.class);

    private final ControleMedicamentosRepository controleMedicamentosRepository;

    public ControleMedicamentosServiceImpl(ControleMedicamentosRepository controleMedicamentosRepository) {
        this.controleMedicamentosRepository = controleMedicamentosRepository;
    }

    @Override
    public ControleMedicamentos save(ControleMedicamentos controleMedicamentos) {
        log.debug("Request to save ControleMedicamentos : {}", controleMedicamentos);
        return controleMedicamentosRepository.save(controleMedicamentos);
    }

    @Override
    public ControleMedicamentos update(ControleMedicamentos controleMedicamentos) {
        log.debug("Request to update ControleMedicamentos : {}", controleMedicamentos);
        return controleMedicamentosRepository.save(controleMedicamentos);
    }

    @Override
    public Optional<ControleMedicamentos> partialUpdate(ControleMedicamentos controleMedicamentos) {
        log.debug("Request to partially update ControleMedicamentos : {}", controleMedicamentos);

        return controleMedicamentosRepository
            .findById(controleMedicamentos.getId())
            .map(existingControleMedicamentos -> {
                if (controleMedicamentos.getNomeMedicamento() != null) {
                    existingControleMedicamentos.setNomeMedicamento(controleMedicamentos.getNomeMedicamento());
                }
                if (controleMedicamentos.getDosagem() != null) {
                    existingControleMedicamentos.setDosagem(controleMedicamentos.getDosagem());
                }
                if (controleMedicamentos.getHorarioIngestao() != null) {
                    existingControleMedicamentos.setHorarioIngestao(controleMedicamentos.getHorarioIngestao());
                }

                return existingControleMedicamentos;
            })
            .map(controleMedicamentosRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ControleMedicamentos> findOne(Long id) {
        log.debug("Request to get ControleMedicamentos : {}", id);
        return controleMedicamentosRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ControleMedicamentos : {}", id);
        controleMedicamentosRepository.deleteById(id);
    }
}
