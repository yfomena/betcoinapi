package com.betcoin.backend.service.impl;

import com.betcoin.backend.service.GamerService;
import com.betcoin.backend.domain.Gamer;
import com.betcoin.backend.repository.GamerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Gamer.
 */
@Service
@Transactional
public class GamerServiceImpl implements GamerService {

    private final Logger log = LoggerFactory.getLogger(GamerServiceImpl.class);

    private final GamerRepository gamerRepository;

    public GamerServiceImpl(GamerRepository gamerRepository) {
        this.gamerRepository = gamerRepository;
    }

    /**
     * Save a gamer.
     *
     * @param gamer the entity to save
     * @return the persisted entity
     */
    @Override
    public Gamer save(Gamer gamer) {
        log.debug("Request to save Gamer : {}", gamer);
        return gamerRepository.save(gamer);
    }

    /**
     * Get all the gamers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Gamer> findAll(Pageable pageable) {
        log.debug("Request to get all Gamers");
        return gamerRepository.findAll(pageable);
    }

    /**
     * Get one gamer by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Gamer findOne(Long id) {
        log.debug("Request to get Gamer : {}", id);
        return gamerRepository.findOne(id);
    }

    /**
     * Delete the gamer by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Gamer : {}", id);
        gamerRepository.delete(id);
    }
}
