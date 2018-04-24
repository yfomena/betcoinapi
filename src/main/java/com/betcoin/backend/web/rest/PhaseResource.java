package com.betcoin.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.betcoin.backend.domain.Phase;
import com.betcoin.backend.service.PhaseService;
import com.betcoin.backend.web.rest.errors.BadRequestAlertException;
import com.betcoin.backend.web.rest.util.HeaderUtil;
import com.betcoin.backend.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Phase.
 */
@RestController
@RequestMapping("/api")
public class PhaseResource {

    private final Logger log = LoggerFactory.getLogger(PhaseResource.class);

    private static final String ENTITY_NAME = "phase";

    private final PhaseService phaseService;

    public PhaseResource(PhaseService phaseService) {
        this.phaseService = phaseService;
    }

    /**
     * POST  /phases : Create a new phase.
     *
     * @param phase the phase to create
     * @return the ResponseEntity with status 201 (Created) and with body the new phase, or with status 400 (Bad Request) if the phase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/phases")
    @Timed
    public ResponseEntity<Phase> createPhase(@RequestBody Phase phase) throws URISyntaxException {
        log.debug("REST request to save Phase : {}", phase);
        if (phase.getId() != null) {
            throw new BadRequestAlertException("A new phase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Phase result = phaseService.save(phase);
        return ResponseEntity.created(new URI("/api/phases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /phases : Updates an existing phase.
     *
     * @param phase the phase to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated phase,
     * or with status 400 (Bad Request) if the phase is not valid,
     * or with status 500 (Internal Server Error) if the phase couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/phases")
    @Timed
    public ResponseEntity<Phase> updatePhase(@RequestBody Phase phase) throws URISyntaxException {
        log.debug("REST request to update Phase : {}", phase);
        if (phase.getId() == null) {
            return createPhase(phase);
        }
        Phase result = phaseService.save(phase);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, phase.getId().toString()))
            .body(result);
    }

    /**
     * GET  /phases : get all the phases.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of phases in body
     */
    @GetMapping("/phases")
    @Timed
    public ResponseEntity<List<Phase>> getAllPhases(Pageable pageable) {
        log.debug("REST request to get a page of Phases");
        Page<Phase> page = phaseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/phases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /phases/:id : get the "id" phase.
     *
     * @param id the id of the phase to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the phase, or with status 404 (Not Found)
     */
    @GetMapping("/phases/{id}")
    @Timed
    public ResponseEntity<Phase> getPhase(@PathVariable Long id) {
        log.debug("REST request to get Phase : {}", id);
        Phase phase = phaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(phase));
    }

    /**
     * DELETE  /phases/:id : delete the "id" phase.
     *
     * @param id the id of the phase to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/phases/{id}")
    @Timed
    public ResponseEntity<Void> deletePhase(@PathVariable Long id) {
        log.debug("REST request to delete Phase : {}", id);
        phaseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
