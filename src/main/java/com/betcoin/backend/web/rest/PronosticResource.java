package com.betcoin.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.betcoin.backend.domain.Pronostic;
import com.betcoin.backend.service.PronosticService;
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
 * REST controller for managing Pronostic.
 */
@RestController
@RequestMapping("/api")
public class PronosticResource {

    private final Logger log = LoggerFactory.getLogger(PronosticResource.class);

    private static final String ENTITY_NAME = "pronostic";

    private final PronosticService pronosticService;

    public PronosticResource(PronosticService pronosticService) {
        this.pronosticService = pronosticService;
    }

    /**
     * POST  /pronostics : Create a new pronostic.
     *
     * @param pronostic the pronostic to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pronostic, or with status 400 (Bad Request) if the pronostic has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pronostics")
    @Timed
    public ResponseEntity<Pronostic> createPronostic(@RequestBody Pronostic pronostic) throws URISyntaxException {
        log.debug("REST request to save Pronostic : {}", pronostic);
        if (pronostic.getId() != null) {
            throw new BadRequestAlertException("A new pronostic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pronostic result = pronosticService.save(pronostic);
        return ResponseEntity.created(new URI("/api/pronostics/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pronostics : Updates an existing pronostic.
     *
     * @param pronostic the pronostic to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pronostic,
     * or with status 400 (Bad Request) if the pronostic is not valid,
     * or with status 500 (Internal Server Error) if the pronostic couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pronostics")
    @Timed
    public ResponseEntity<Pronostic> updatePronostic(@RequestBody Pronostic pronostic) throws URISyntaxException {
        log.debug("REST request to update Pronostic : {}", pronostic);
        if (pronostic.getId() == null) {
            return createPronostic(pronostic);
        }
        Pronostic result = pronosticService.save(pronostic);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pronostic.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pronostics : get all the pronostics.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pronostics in body
     */
    @GetMapping("/pronostics")
    @Timed
    public ResponseEntity<List<Pronostic>> getAllPronostics(Pageable pageable) {
        log.debug("REST request to get a page of Pronostics");
        Page<Pronostic> page = pronosticService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pronostics");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pronostics/:id : get the "id" pronostic.
     *
     * @param id the id of the pronostic to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pronostic, or with status 404 (Not Found)
     */
    @GetMapping("/pronostics/{id}")
    @Timed
    public ResponseEntity<Pronostic> getPronostic(@PathVariable Long id) {
        log.debug("REST request to get Pronostic : {}", id);
        Pronostic pronostic = pronosticService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pronostic));
    }

    /**
     * DELETE  /pronostics/:id : delete the "id" pronostic.
     *
     * @param id the id of the pronostic to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pronostics/{id}")
    @Timed
    public ResponseEntity<Void> deletePronostic(@PathVariable Long id) {
        log.debug("REST request to delete Pronostic : {}", id);
        pronosticService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
