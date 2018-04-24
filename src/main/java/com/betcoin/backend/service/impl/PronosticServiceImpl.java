package com.betcoin.backend.service.impl;

import com.betcoin.backend.service.PronosticService;
import com.betcoin.backend.domain.Pronostic;
import com.betcoin.backend.repository.PronosticRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Pronostic.
 */
@Service
@Transactional
public class PronosticServiceImpl implements PronosticService {

    private final Logger log = LoggerFactory.getLogger(PronosticServiceImpl.class);

    private final PronosticRepository pronosticRepository;

    public PronosticServiceImpl(PronosticRepository pronosticRepository) {
        this.pronosticRepository = pronosticRepository;
    }

    /**
     * Save a pronostic.
     *
     * @param pronostic the entity to save
     * @return the persisted entity
     */
    @Override
    public Pronostic save(Pronostic pronostic) {
        log.debug("Request to save Pronostic : {}", pronostic);
        return pronosticRepository.save(pronostic);
    }

    /**
     * Get all the pronostics.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Pronostic> findAll(Pageable pageable) {
        log.debug("Request to get all Pronostics");
        return pronosticRepository.findAll(pageable);
    }

    /**
     * Get one pronostic by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Pronostic findOne(Long id) {
        log.debug("Request to get Pronostic : {}", id);
        return pronosticRepository.findOne(id);
    }

    /**
     * Delete the pronostic by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pronostic : {}", id);
        pronosticRepository.delete(id);
    }
}
