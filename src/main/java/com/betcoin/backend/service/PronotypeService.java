package com.betcoin.backend.service;

import com.betcoin.backend.domain.Pronotype;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Pronotype.
 */
public interface PronotypeService {

    /**
     * Save a pronotype.
     *
     * @param pronotype the entity to save
     * @return the persisted entity
     */
    Pronotype save(Pronotype pronotype);

    /**
     * Get all the pronotypes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Pronotype> findAll(Pageable pageable);

    /**
     * Get the "id" pronotype.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Pronotype findOne(Long id);

    /**
     * Delete the "id" pronotype.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
