package com.betcoin.backend.service.impl;

import com.betcoin.backend.service.PronotypeService;
import com.betcoin.backend.domain.Pronotype;
import com.betcoin.backend.repository.PronotypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Pronotype.
 */
@Service
@Transactional
public class PronotypeServiceImpl implements PronotypeService {

    private final Logger log = LoggerFactory.getLogger(PronotypeServiceImpl.class);

    private final PronotypeRepository pronotypeRepository;

    public PronotypeServiceImpl(PronotypeRepository pronotypeRepository) {
        this.pronotypeRepository = pronotypeRepository;
    }

    /**
     * Save a pronotype.
     *
     * @param pronotype the entity to save
     * @return the persisted entity
     */
    @Override
    public Pronotype save(Pronotype pronotype) {
        log.debug("Request to save Pronotype : {}", pronotype);
        return pronotypeRepository.save(pronotype);
    }

    /**
     * Get all the pronotypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Pronotype> findAll(Pageable pageable) {
        log.debug("Request to get all Pronotypes");
        return pronotypeRepository.findAll(pageable);
    }

    /**
     * Get one pronotype by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Pronotype findOne(Long id) {
        log.debug("Request to get Pronotype : {}", id);
        return pronotypeRepository.findOne(id);
    }

    /**
     * Delete the pronotype by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pronotype : {}", id);
        pronotypeRepository.delete(id);
    }
}
