package com.betcoin.backend.service;

import com.betcoin.backend.domain.Pronostatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Pronostatus.
 */
public interface PronostatusService {

    /**
     * Save a pronostatus.
     *
     * @param pronostatus the entity to save
     * @return the persisted entity
     */
    Pronostatus save(Pronostatus pronostatus);

    /**
     * Get all the pronostatuses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Pronostatus> findAll(Pageable pageable);

    /**
     * Get the "id" pronostatus.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Pronostatus findOne(Long id);

    /**
     * Delete the "id" pronostatus.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
