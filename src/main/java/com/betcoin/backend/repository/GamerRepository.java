package com.betcoin.backend.repository;

import com.betcoin.backend.domain.Gamer;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Gamer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GamerRepository extends JpaRepository<Gamer, Long> {

}
