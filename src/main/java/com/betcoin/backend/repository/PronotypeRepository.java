package com.betcoin.backend.repository;

import com.betcoin.backend.domain.Pronotype;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Pronotype entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PronotypeRepository extends JpaRepository<Pronotype, Long> {

}
