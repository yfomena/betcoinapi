package com.betcoin.backend.web.rest;

import com.betcoin.backend.BetcoinapiApp;

import com.betcoin.backend.domain.Phase;
import com.betcoin.backend.repository.PhaseRepository;
import com.betcoin.backend.service.PhaseService;
import com.betcoin.backend.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.betcoin.backend.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PhaseResource REST controller.
 *
 * @see PhaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BetcoinapiApp.class)
public class PhaseResourceIntTest {

    private static final String DEFAULT_PHASE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PHASE_NAME = "BBBBBBBBBB";

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private PhaseService phaseService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPhaseMockMvc;

    private Phase phase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PhaseResource phaseResource = new PhaseResource(phaseService);
        this.restPhaseMockMvc = MockMvcBuilders.standaloneSetup(phaseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phase createEntity(EntityManager em) {
        Phase phase = new Phase()
            .phaseName(DEFAULT_PHASE_NAME);
        return phase;
    }

    @Before
    public void initTest() {
        phase = createEntity(em);
    }

    @Test
    @Transactional
    public void createPhase() throws Exception {
        int databaseSizeBeforeCreate = phaseRepository.findAll().size();

        // Create the Phase
        restPhaseMockMvc.perform(post("/api/phases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(phase)))
            .andExpect(status().isCreated());

        // Validate the Phase in the database
        List<Phase> phaseList = phaseRepository.findAll();
        assertThat(phaseList).hasSize(databaseSizeBeforeCreate + 1);
        Phase testPhase = phaseList.get(phaseList.size() - 1);
        assertThat(testPhase.getPhaseName()).isEqualTo(DEFAULT_PHASE_NAME);
    }

    @Test
    @Transactional
    public void createPhaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = phaseRepository.findAll().size();

        // Create the Phase with an existing ID
        phase.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhaseMockMvc.perform(post("/api/phases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(phase)))
            .andExpect(status().isBadRequest());

        // Validate the Phase in the database
        List<Phase> phaseList = phaseRepository.findAll();
        assertThat(phaseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPhases() throws Exception {
        // Initialize the database
        phaseRepository.saveAndFlush(phase);

        // Get all the phaseList
        restPhaseMockMvc.perform(get("/api/phases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phase.getId().intValue())))
            .andExpect(jsonPath("$.[*].phaseName").value(hasItem(DEFAULT_PHASE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getPhase() throws Exception {
        // Initialize the database
        phaseRepository.saveAndFlush(phase);

        // Get the phase
        restPhaseMockMvc.perform(get("/api/phases/{id}", phase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(phase.getId().intValue()))
            .andExpect(jsonPath("$.phaseName").value(DEFAULT_PHASE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPhase() throws Exception {
        // Get the phase
        restPhaseMockMvc.perform(get("/api/phases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePhase() throws Exception {
        // Initialize the database
        phaseService.save(phase);

        int databaseSizeBeforeUpdate = phaseRepository.findAll().size();

        // Update the phase
        Phase updatedPhase = phaseRepository.findOne(phase.getId());
        // Disconnect from session so that the updates on updatedPhase are not directly saved in db
        em.detach(updatedPhase);
        updatedPhase
            .phaseName(UPDATED_PHASE_NAME);

        restPhaseMockMvc.perform(put("/api/phases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPhase)))
            .andExpect(status().isOk());

        // Validate the Phase in the database
        List<Phase> phaseList = phaseRepository.findAll();
        assertThat(phaseList).hasSize(databaseSizeBeforeUpdate);
        Phase testPhase = phaseList.get(phaseList.size() - 1);
        assertThat(testPhase.getPhaseName()).isEqualTo(UPDATED_PHASE_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingPhase() throws Exception {
        int databaseSizeBeforeUpdate = phaseRepository.findAll().size();

        // Create the Phase

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPhaseMockMvc.perform(put("/api/phases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(phase)))
            .andExpect(status().isCreated());

        // Validate the Phase in the database
        List<Phase> phaseList = phaseRepository.findAll();
        assertThat(phaseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePhase() throws Exception {
        // Initialize the database
        phaseService.save(phase);

        int databaseSizeBeforeDelete = phaseRepository.findAll().size();

        // Get the phase
        restPhaseMockMvc.perform(delete("/api/phases/{id}", phase.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Phase> phaseList = phaseRepository.findAll();
        assertThat(phaseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Phase.class);
        Phase phase1 = new Phase();
        phase1.setId(1L);
        Phase phase2 = new Phase();
        phase2.setId(phase1.getId());
        assertThat(phase1).isEqualTo(phase2);
        phase2.setId(2L);
        assertThat(phase1).isNotEqualTo(phase2);
        phase1.setId(null);
        assertThat(phase1).isNotEqualTo(phase2);
    }
}
