package com.betcoin.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.betcoin.backend.domain.Groupe;
import com.betcoin.backend.service.GroupeService;
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
 * REST controller for managing Groupe.
 */
@RestController
@RequestMapping("/api")
public class GroupeResource {

    private final Logger log = LoggerFactory.getLogger(GroupeResource.class);

    private static final String ENTITY_NAME = "groupe";

    private final GroupeService groupeService;

    public GroupeResource(GroupeService groupeService) {
        this.groupeService = groupeService;
    }

    /**
     * POST  /groupes : Create a new groupe.
     *
     * @param groupe the groupe to create
     * @return the ResponseEntity with status 201 (Created) and with body the new groupe, or with status 400 (Bad Request) if the groupe has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/groupes")
    @Timed
    public ResponseEntity<Groupe> createGroupe(@RequestBody Groupe groupe) throws URISyntaxException {
        log.debug("REST request to save Groupe : {}", groupe);
        if (groupe.getId() != null) {
            throw new BadRequestAlertException("A new groupe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Groupe result = groupeService.save(groupe);
        return ResponseEntity.created(new URI("/api/groupes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /groupes : Updates an existing groupe.
     *
     * @param groupe the groupe to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated groupe,
     * or with status 400 (Bad Request) if the groupe is not valid,
     * or with status 500 (Internal Server Error) if the groupe couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/groupes")
    @Timed
    public ResponseEntity<Groupe> updateGroupe(@RequestBody Groupe groupe) throws URISyntaxException {
        log.debug("REST request to update Groupe : {}", groupe);
        if (groupe.getId() == null) {
            return createGroupe(groupe);
        }
        Groupe result = groupeService.save(groupe);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, groupe.getId().toString()))
            .body(result);
    }

    /**
     * GET  /groupes : get all the groupes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of groupes in body
     */
    @GetMapping("/groupes")
    @Timed
    public ResponseEntity<List<Groupe>> getAllGroupes(Pageable pageable) {
        log.debug("REST request to get a page of Groupes");
        Page<Groupe> page = groupeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/groupes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /groupes/:id : get the "id" groupe.
     *
     * @param id the id of the groupe to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the groupe, or with status 404 (Not Found)
     */
    @GetMapping("/groupes/{id}")
    @Timed
    public ResponseEntity<Groupe> getGroupe(@PathVariable Long id) {
        log.debug("REST request to get Groupe : {}", id);
        Groupe groupe = groupeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(groupe));
    }

    /**
     * DELETE  /groupes/:id : delete the "id" groupe.
     *
     * @param id the id of the groupe to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/groupes/{id}")
    @Timed
    public ResponseEntity<Void> deleteGroupe(@PathVariable Long id) {
        log.debug("REST request to delete Groupe : {}", id);
        groupeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
