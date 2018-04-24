package com.betcoin.backend.service;

import com.betcoin.backend.domain.Competition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Competition.
 */
public interface CompetitionService {

    /**
     * Save a competition.
     *
     * @param competition the entity to save
     * @return the persisted entity
     */
    Competition save(Competition competition);

    /**
     * Get all the competitions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Competition> findAll(Pageable pageable);

    /**
     * Get the "id" competition.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Competition findOne(Long id);

    /**
     * Delete the "id" competition.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
