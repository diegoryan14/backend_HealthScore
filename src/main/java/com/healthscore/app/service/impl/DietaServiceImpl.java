package com.healthscore.app.service.impl;

import com.healthscore.app.domain.Dieta;
import com.healthscore.app.repository.DietaRepository;
import com.healthscore.app.service.DietaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.healthscore.app.domain.Dieta}.
 */
@Service
@Transactional
public class DietaServiceImpl implements DietaService {

    private final Logger log = LoggerFactory.getLogger(DietaServiceImpl.class);

    private final DietaRepository dietaRepository;

    public DietaServiceImpl(DietaRepository dietaRepository) {
        this.dietaRepository = dietaRepository;
    }

    @Override
    public Dieta save(Dieta dieta) {
        log.debug("Request to save Dieta : {}", dieta);
        return dietaRepository.save(dieta);
    }

    @Override
    public Dieta update(Dieta dieta) {
        log.debug("Request to update Dieta : {}", dieta);
        return dietaRepository.save(dieta);
    }

    @Override
    public Optional<Dieta> partialUpdate(Dieta dieta) {
        log.debug("Request to partially update Dieta : {}", dieta);

        return dietaRepository
            .findById(dieta.getId())
            .map(existingDieta -> {
                if (dieta.getDescricaoRefeicao() != null) {
                    existingDieta.setDescricaoRefeicao(dieta.getDescricaoRefeicao());
                }
                if (dieta.getDataHorarioRefeicao() != null) {
                    existingDieta.setDataHorarioRefeicao(dieta.getDataHorarioRefeicao());
                }
                if (dieta.getCaloriasConsumidas() != null) {
                    existingDieta.setCaloriasConsumidas(dieta.getCaloriasConsumidas());
                }

                return existingDieta;
            })
            .map(dietaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Dieta> findOne(Long id) {
        log.debug("Request to get Dieta : {}", id);
        return dietaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dieta : {}", id);
        dietaRepository.deleteById(id);
    }
}
