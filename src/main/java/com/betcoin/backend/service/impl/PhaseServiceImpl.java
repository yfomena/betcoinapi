package com.betcoin.backend.service.impl;

import com.betcoin.backend.service.PhaseService;
import com.betcoin.backend.domain.Phase;
import com.betcoin.backend.repository.PhaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Phase.
 */
@Service
@Transactional
public class PhaseServiceImpl implements PhaseService {

    private final Logger log = LoggerFactory.getLogger(PhaseServiceImpl.class);

    private final PhaseRepository phaseRepository;

    public PhaseServiceImpl(PhaseRepository phaseRepository) {
        this.phaseRepository = phaseRepository;
    }

    /**
     * Save a phase.
     *
     * @param phase the entity to save
     * @return the persisted entity
     */
    @Override
    public Phase save(Phase phase) {
        log.debug("Request to save Phase : {}", phase);
        return phaseRepository.save(phase);
    }

    /**
     * Get all the phases.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Phase> findAll(Pageable pageable) {
        log.debug("Request to get all Phases");
        return phaseRepository.findAll(pageable);
    }

    /**
     * Get one phase by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Phase findOne(Long id) {
        log.debug("Request to get Phase : {}", id);
        return phaseRepository.findOne(id);
    }

    /**
     * Delete the phase by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Phase : {}", id);
        phaseRepository.delete(id);
    }
}
