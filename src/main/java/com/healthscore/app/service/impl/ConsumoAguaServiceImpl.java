package com.healthscore.app.service.impl;

import com.healthscore.app.domain.ConsumoAgua;
import com.healthscore.app.repository.ConsumoAguaRepository;
import com.healthscore.app.service.ConsumoAguaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.healthscore.app.domain.ConsumoAgua}.
 */
@Service
@Transactional
public class ConsumoAguaServiceImpl implements ConsumoAguaService {

    private final Logger log = LoggerFactory.getLogger(ConsumoAguaServiceImpl.class);

    private final ConsumoAguaRepository consumoAguaRepository;

    public ConsumoAguaServiceImpl(ConsumoAguaRepository consumoAguaRepository) {
        this.consumoAguaRepository = consumoAguaRepository;
    }

    @Override
    public ConsumoAgua save(ConsumoAgua consumoAgua) {
        log.debug("Request to save ConsumoAgua : {}", consumoAgua);
        return consumoAguaRepository.save(consumoAgua);
    }

    @Override
    public ConsumoAgua update(ConsumoAgua consumoAgua) {
        log.debug("Request to update ConsumoAgua : {}", consumoAgua);
        return consumoAguaRepository.save(consumoAgua);
    }

    @Override
    public Optional<ConsumoAgua> partialUpdate(ConsumoAgua consumoAgua) {
        log.debug("Request to partially update ConsumoAgua : {}", consumoAgua);

        return consumoAguaRepository
            .findById(consumoAgua.getId())
            .map(existingConsumoAgua -> {
                if (consumoAgua.getDataConsumo() != null) {
                    existingConsumoAgua.setDataConsumo(consumoAgua.getDataConsumo());
                }
                if (consumoAgua.getQuantidadeMl() != null) {
                    existingConsumoAgua.setQuantidadeMl(consumoAgua.getQuantidadeMl());
                }

                return existingConsumoAgua;
            })
            .map(consumoAguaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConsumoAgua> findOne(Long id) {
        log.debug("Request to get ConsumoAgua : {}", id);
        return consumoAguaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ConsumoAgua : {}", id);
        consumoAguaRepository.deleteById(id);
    }
}
