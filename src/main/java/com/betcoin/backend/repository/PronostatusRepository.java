package com.betcoin.backend.repository;

import com.betcoin.backend.domain.Pronostatus;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Pronostatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PronostatusRepository extends JpaRepository<Pronostatus, Long> {

}
