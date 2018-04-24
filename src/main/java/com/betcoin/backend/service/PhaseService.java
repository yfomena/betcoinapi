package com.betcoin.backend.service;

import com.betcoin.backend.domain.Phase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Phase.
 */
public interface PhaseService {

    /**
     * Save a phase.
     *
     * @param phase the entity to save
     * @return the persisted entity
     */
    Phase save(Phase phase);

    /**
     * Get all the phases.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Phase> findAll(Pageable pageable);

    /**
     * Get the "id" phase.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Phase findOne(Long id);

    /**
     * Delete the "id" phase.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
