package com.healthscore.app.service.impl;

import com.healthscore.app.domain.Especialista;
import com.healthscore.app.repository.EspecialistaRepository;
import com.healthscore.app.service.EspecialistaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.healthscore.app.domain.Especialista}.
 */
@Service
@Transactional
public class EspecialistaServiceImpl implements EspecialistaService {

    private final Logger log = LoggerFactory.getLogger(EspecialistaServiceImpl.class);

    private final EspecialistaRepository especialistaRepository;

    public EspecialistaServiceImpl(EspecialistaRepository especialistaRepository) {
        this.especialistaRepository = especialistaRepository;
    }

    @Override
    public Especialista save(Especialista especialista) {
        log.debug("Request to save Especialista : {}", especialista);
        return especialistaRepository.save(especialista);
    }

    @Override
    public Especialista update(Especialista especialista) {
        log.debug("Request to update Especialista : {}", especialista);
        return especialistaRepository.save(especialista);
    }

    @Override
    public Optional<Especialista> partialUpdate(Especialista especialista) {
        log.debug("Request to partially update Especialista : {}", especialista);

        return especialistaRepository
            .findById(especialista.getId())
            .map(existingEspecialista -> {
                if (especialista.getNome() != null) {
                    existingEspecialista.setNome(especialista.getNome());
                }
                if (especialista.getCpf() != null) {
                    existingEspecialista.setCpf(especialista.getCpf());
                }
                if (especialista.getEspecializacao() != null) {
                    existingEspecialista.setEspecializacao(especialista.getEspecializacao());
                }
                if (especialista.getDataFormacao() != null) {
                    existingEspecialista.setDataFormacao(especialista.getDataFormacao());
                }
                if (especialista.getTelefone() != null) {
                    existingEspecialista.setTelefone(especialista.getTelefone());
                }
                if (especialista.getEmail() != null) {
                    existingEspecialista.setEmail(especialista.getEmail());
                }
                if (especialista.getDataNascimento() != null) {
                    existingEspecialista.setDataNascimento(especialista.getDataNascimento());
                }

                return existingEspecialista;
            })
            .map(especialistaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Especialista> findOne(Long id) {
        log.debug("Request to get Especialista : {}", id);
        return especialistaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Especialista : {}", id);
        especialistaRepository.deleteById(id);
    }
}
