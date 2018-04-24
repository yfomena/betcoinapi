package com.betcoin.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.betcoin.backend.domain.Pronotype;
import com.betcoin.backend.service.PronotypeService;
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
 * REST controller for managing Pronotype.
 */
@RestController
@RequestMapping("/api")
public class PronotypeResource {

    private final Logger log = LoggerFactory.getLogger(PronotypeResource.class);

    private static final String ENTITY_NAME = "pronotype";

    private final PronotypeService pronotypeService;

    public PronotypeResource(PronotypeService pronotypeService) {
        this.pronotypeService = pronotypeService;
    }

    /**
     * POST  /pronotypes : Create a new pronotype.
     *
     * @param pronotype the pronotype to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pronotype, or with status 400 (Bad Request) if the pronotype has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pronotypes")
    @Timed
    public ResponseEntity<Pronotype> createPronotype(@RequestBody Pronotype pronotype) throws URISyntaxException {
        log.debug("REST request to save Pronotype : {}", pronotype);
        if (pronotype.getId() != null) {
            throw new BadRequestAlertException("A new pronotype cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pronotype result = pronotypeService.save(pronotype);
        return ResponseEntity.created(new URI("/api/pronotypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pronotypes : Updates an existing pronotype.
     *
     * @param pronotype the pronotype to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pronotype,
     * or with status 400 (Bad Request) if the pronotype is not valid,
     * or with status 500 (Internal Server Error) if the pronotype couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pronotypes")
    @Timed
    public ResponseEntity<Pronotype> updatePronotype(@RequestBody Pronotype pronotype) throws URISyntaxException {
        log.debug("REST request to update Pronotype : {}", pronotype);
        if (pronotype.getId() == null) {
            return createPronotype(pronotype);
        }
        Pronotype result = pronotypeService.save(pronotype);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pronotype.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pronotypes : get all the pronotypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pronotypes in body
     */
    @GetMapping("/pronotypes")
    @Timed
    public ResponseEntity<List<Pronotype>> getAllPronotypes(Pageable pageable) {
        log.debug("REST request to get a page of Pronotypes");
        Page<Pronotype> page = pronotypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pronotypes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pronotypes/:id : get the "id" pronotype.
     *
     * @param id the id of the pronotype to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pronotype, or with status 404 (Not Found)
     */
    @GetMapping("/pronotypes/{id}")
    @Timed
    public ResponseEntity<Pronotype> getPronotype(@PathVariable Long id) {
        log.debug("REST request to get Pronotype : {}", id);
        Pronotype pronotype = pronotypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pronotype));
    }

    /**
     * DELETE  /pronotypes/:id : delete the "id" pronotype.
     *
     * @param id the id of the pronotype to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pronotypes/{id}")
    @Timed
    public ResponseEntity<Void> deletePronotype(@PathVariable Long id) {
        log.debug("REST request to delete Pronotype : {}", id);
        pronotypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
