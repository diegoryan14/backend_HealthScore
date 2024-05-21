package com.healthscore.app.service.impl;

import com.healthscore.app.domain.QualidadeSono;
import com.healthscore.app.repository.QualidadeSonoRepository;
import com.healthscore.app.service.QualidadeSonoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.healthscore.app.domain.QualidadeSono}.
 */
@Service
@Transactional
public class QualidadeSonoServiceImpl implements QualidadeSonoService {

    private final Logger log = LoggerFactory.getLogger(QualidadeSonoServiceImpl.class);

    private final QualidadeSonoRepository qualidadeSonoRepository;

    public QualidadeSonoServiceImpl(QualidadeSonoRepository qualidadeSonoRepository) {
        this.qualidadeSonoRepository = qualidadeSonoRepository;
    }

    @Override
    public QualidadeSono save(QualidadeSono qualidadeSono) {
        log.debug("Request to save QualidadeSono : {}", qualidadeSono);
        return qualidadeSonoRepository.save(qualidadeSono);
    }

    @Override
    public QualidadeSono update(QualidadeSono qualidadeSono) {
        log.debug("Request to update QualidadeSono : {}", qualidadeSono);
        return qualidadeSonoRepository.save(qualidadeSono);
    }

    @Override
    public Optional<QualidadeSono> partialUpdate(QualidadeSono qualidadeSono) {
        log.debug("Request to partially update QualidadeSono : {}", qualidadeSono);

        return qualidadeSonoRepository
            .findById(qualidadeSono.getId())
            .map(existingQualidadeSono -> {
                if (qualidadeSono.getData() != null) {
                    existingQualidadeSono.setData(qualidadeSono.getData());
                }
                if (qualidadeSono.getHorasSono() != null) {
                    existingQualidadeSono.setHorasSono(qualidadeSono.getHorasSono());
                }

                return existingQualidadeSono;
            })
            .map(qualidadeSonoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QualidadeSono> findOne(Long id) {
        log.debug("Request to get QualidadeSono : {}", id);
        return qualidadeSonoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete QualidadeSono : {}", id);
        qualidadeSonoRepository.deleteById(id);
    }
}
