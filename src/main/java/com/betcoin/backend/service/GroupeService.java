package com.betcoin.backend.service;

import com.betcoin.backend.domain.Groupe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Groupe.
 */
public interface GroupeService {

    /**
     * Save a groupe.
     *
     * @param groupe the entity to save
     * @return the persisted entity
     */
    Groupe save(Groupe groupe);

    /**
     * Get all the groupes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Groupe> findAll(Pageable pageable);

    /**
     * Get the "id" groupe.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Groupe findOne(Long id);

    /**
     * Delete the "id" groupe.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
