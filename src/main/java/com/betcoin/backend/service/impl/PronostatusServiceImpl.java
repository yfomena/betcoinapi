package com.betcoin.backend.service.impl;

import com.betcoin.backend.service.PronostatusService;
import com.betcoin.backend.domain.Pronostatus;
import com.betcoin.backend.repository.PronostatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Pronostatus.
 */
@Service
@Transactional
public class PronostatusServiceImpl implements PronostatusService {

    private final Logger log = LoggerFactory.getLogger(PronostatusServiceImpl.class);

    private final PronostatusRepository pronostatusRepository;

    public PronostatusServiceImpl(PronostatusRepository pronostatusRepository) {
        this.pronostatusRepository = pronostatusRepository;
    }

    /**
     * Save a pronostatus.
     *
     * @param pronostatus the entity to save
     * @return the persisted entity
     */
    @Override
    public Pronostatus save(Pronostatus pronostatus) {
        log.debug("Request to save Pronostatus : {}", pronostatus);
        return pronostatusRepository.save(pronostatus);
    }

    /**
     * Get all the pronostatuses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Pronostatus> findAll(Pageable pageable) {
        log.debug("Request to get all Pronostatuses");
        return pronostatusRepository.findAll(pageable);
    }

    /**
     * Get one pronostatus by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Pronostatus findOne(Long id) {
        log.debug("Request to get Pronostatus : {}", id);
        return pronostatusRepository.findOne(id);
    }

    /**
     * Delete the pronostatus by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pronostatus : {}", id);
        pronostatusRepository.delete(id);
    }
}
