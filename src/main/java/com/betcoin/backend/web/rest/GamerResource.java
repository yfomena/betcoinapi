package com.betcoin.backend.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.betcoin.backend.domain.Gamer;
import com.betcoin.backend.service.GamerService;
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
 * REST controller for managing Gamer.
 */
@RestController
@RequestMapping("/api")
public class GamerResource {

    private final Logger log = LoggerFactory.getLogger(GamerResource.class);

    private static final String ENTITY_NAME = "gamer";

    private final GamerService gamerService;

    public GamerResource(GamerService gamerService) {
        this.gamerService = gamerService;
    }

    /**
     * POST  /gamers : Create a new gamer.
     *
     * @param gamer the gamer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gamer, or with status 400 (Bad Request) if the gamer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/gamers")
    @Timed
    public ResponseEntity<Gamer> createGamer(@RequestBody Gamer gamer) throws URISyntaxException {
        log.debug("REST request to save Gamer : {}", gamer);
        if (gamer.getId() != null) {
            throw new BadRequestAlertException("A new gamer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Gamer result = gamerService.save(gamer);
        return ResponseEntity.created(new URI("/api/gamers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gamers : Updates an existing gamer.
     *
     * @param gamer the gamer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gamer,
     * or with status 400 (Bad Request) if the gamer is not valid,
     * or with status 500 (Internal Server Error) if the gamer couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/gamers")
    @Timed
    public ResponseEntity<Gamer> updateGamer(@RequestBody Gamer gamer) throws URISyntaxException {
        log.debug("REST request to update Gamer : {}", gamer);
        if (gamer.getId() == null) {
            return createGamer(gamer);
        }
        Gamer result = gamerService.save(gamer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gamer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gamers : get all the gamers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of gamers in body
     */
    @GetMapping("/gamers")
    @Timed
    public ResponseEntity<List<Gamer>> getAllGamers(Pageable pageable) {
        log.debug("REST request to get a page of Gamers");
        Page<Gamer> page = gamerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/gamers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /gamers/:id : get the "id" gamer.
     *
     * @param id the id of the gamer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gamer, or with status 404 (Not Found)
     */
    @GetMapping("/gamers/{id}")
    @Timed
    public ResponseEntity<Gamer> getGamer(@PathVariable Long id) {
        log.debug("REST request to get Gamer : {}", id);
        Gamer gamer = gamerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gamer));
    }

    /**
     * DELETE  /gamers/:id : delete the "id" gamer.
     *
     * @param id the id of the gamer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/gamers/{id}")
    @Timed
    public ResponseEntity<Void> deleteGamer(@PathVariable Long id) {
        log.debug("REST request to delete Gamer : {}", id);
        gamerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
