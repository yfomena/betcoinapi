package com.betcoin.backend.service;

import com.betcoin.backend.domain.Pronostic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Pronostic.
 */
public interface PronosticService {

    /**
     * Save a pronostic.
     *
     * @param pronostic the entity to save
     * @return the persisted entity
     */
    Pronostic save(Pronostic pronostic);

    /**
     * Get all the pronostics.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Pronostic> findAll(Pageable pageable);

    /**
     * Get the "id" pronostic.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Pronostic findOne(Long id);

    /**
     * Delete the "id" pronostic.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
