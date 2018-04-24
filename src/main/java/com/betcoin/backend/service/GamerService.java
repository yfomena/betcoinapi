package com.betcoin.backend.service;

import com.betcoin.backend.domain.Gamer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Gamer.
 */
public interface GamerService {

    /**
     * Save a gamer.
     *
     * @param gamer the entity to save
     * @return the persisted entity
     */
    Gamer save(Gamer gamer);

    /**
     * Get all the gamers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Gamer> findAll(Pageable pageable);

    /**
     * Get the "id" gamer.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Gamer findOne(Long id);

    /**
     * Delete the "id" gamer.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
