package com.betcoin.backend.web.rest;

import com.betcoin.backend.BetcoinapiApp;

import com.betcoin.backend.domain.Pronostic;
import com.betcoin.backend.repository.PronosticRepository;
import com.betcoin.backend.service.PronosticService;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.betcoin.backend.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PronosticResource REST controller.
 *
 * @see PronosticResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BetcoinapiApp.class)
public class PronosticResourceIntTest {

    private static final Instant DEFAULT_PRONO_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PRONO_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_MATCH_OR_GROUP_ORCOMPET_ID = 1L;
    private static final Long UPDATED_MATCH_OR_GROUP_ORCOMPET_ID = 2L;

    private static final Integer DEFAULT_SCORE_HOME = 1;
    private static final Integer UPDATED_SCORE_HOME = 2;

    private static final Integer DEFAULT_SCORE_AWAY = 1;
    private static final Integer UPDATED_SCORE_AWAY = 2;

    private static final Long DEFAULT_WINNER = 1L;
    private static final Long UPDATED_WINNER = 2L;

    @Autowired
    private PronosticRepository pronosticRepository;

    @Autowired
    private PronosticService pronosticService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPronosticMockMvc;

    private Pronostic pronostic;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PronosticResource pronosticResource = new PronosticResource(pronosticService);
        this.restPronosticMockMvc = MockMvcBuilders.standaloneSetup(pronosticResource)
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
    public static Pronostic createEntity(EntityManager em) {
        Pronostic pronostic = new Pronostic()
            .pronoDate(DEFAULT_PRONO_DATE)
            .matchOrGroupOrcompetId(DEFAULT_MATCH_OR_GROUP_ORCOMPET_ID)
            .scoreHome(DEFAULT_SCORE_HOME)
            .scoreAway(DEFAULT_SCORE_AWAY)
            .winner(DEFAULT_WINNER);
        return pronostic;
    }

    @Before
    public void initTest() {
        pronostic = createEntity(em);
    }

    @Test
    @Transactional
    public void createPronostic() throws Exception {
        int databaseSizeBeforeCreate = pronosticRepository.findAll().size();

        // Create the Pronostic
        restPronosticMockMvc.perform(post("/api/pronostics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pronostic)))
            .andExpect(status().isCreated());

        // Validate the Pronostic in the database
        List<Pronostic> pronosticList = pronosticRepository.findAll();
        assertThat(pronosticList).hasSize(databaseSizeBeforeCreate + 1);
        Pronostic testPronostic = pronosticList.get(pronosticList.size() - 1);
        assertThat(testPronostic.getPronoDate()).isEqualTo(DEFAULT_PRONO_DATE);
        assertThat(testPronostic.getMatchOrGroupOrcompetId()).isEqualTo(DEFAULT_MATCH_OR_GROUP_ORCOMPET_ID);
        assertThat(testPronostic.getScoreHome()).isEqualTo(DEFAULT_SCORE_HOME);
        assertThat(testPronostic.getScoreAway()).isEqualTo(DEFAULT_SCORE_AWAY);
        assertThat(testPronostic.getWinner()).isEqualTo(DEFAULT_WINNER);
    }

    @Test
    @Transactional
    public void createPronosticWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pronosticRepository.findAll().size();

        // Create the Pronostic with an existing ID
        pronostic.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPronosticMockMvc.perform(post("/api/pronostics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pronostic)))
            .andExpect(status().isBadRequest());

        // Validate the Pronostic in the database
        List<Pronostic> pronosticList = pronosticRepository.findAll();
        assertThat(pronosticList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPronostics() throws Exception {
        // Initialize the database
        pronosticRepository.saveAndFlush(pronostic);

        // Get all the pronosticList
        restPronosticMockMvc.perform(get("/api/pronostics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pronostic.getId().intValue())))
            .andExpect(jsonPath("$.[*].pronoDate").value(hasItem(DEFAULT_PRONO_DATE.toString())))
            .andExpect(jsonPath("$.[*].matchOrGroupOrcompetId").value(hasItem(DEFAULT_MATCH_OR_GROUP_ORCOMPET_ID.intValue())))
            .andExpect(jsonPath("$.[*].scoreHome").value(hasItem(DEFAULT_SCORE_HOME)))
            .andExpect(jsonPath("$.[*].scoreAway").value(hasItem(DEFAULT_SCORE_AWAY)))
            .andExpect(jsonPath("$.[*].winner").value(hasItem(DEFAULT_WINNER.intValue())));
    }

    @Test
    @Transactional
    public void getPronostic() throws Exception {
        // Initialize the database
        pronosticRepository.saveAndFlush(pronostic);

        // Get the pronostic
        restPronosticMockMvc.perform(get("/api/pronostics/{id}", pronostic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pronostic.getId().intValue()))
            .andExpect(jsonPath("$.pronoDate").value(DEFAULT_PRONO_DATE.toString()))
            .andExpect(jsonPath("$.matchOrGroupOrcompetId").value(DEFAULT_MATCH_OR_GROUP_ORCOMPET_ID.intValue()))
            .andExpect(jsonPath("$.scoreHome").value(DEFAULT_SCORE_HOME))
            .andExpect(jsonPath("$.scoreAway").value(DEFAULT_SCORE_AWAY))
            .andExpect(jsonPath("$.winner").value(DEFAULT_WINNER.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPronostic() throws Exception {
        // Get the pronostic
        restPronosticMockMvc.perform(get("/api/pronostics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePronostic() throws Exception {
        // Initialize the database
        pronosticService.save(pronostic);

        int databaseSizeBeforeUpdate = pronosticRepository.findAll().size();

        // Update the pronostic
        Pronostic updatedPronostic = pronosticRepository.findOne(pronostic.getId());
        // Disconnect from session so that the updates on updatedPronostic are not directly saved in db
        em.detach(updatedPronostic);
        updatedPronostic
            .pronoDate(UPDATED_PRONO_DATE)
            .matchOrGroupOrcompetId(UPDATED_MATCH_OR_GROUP_ORCOMPET_ID)
            .scoreHome(UPDATED_SCORE_HOME)
            .scoreAway(UPDATED_SCORE_AWAY)
            .winner(UPDATED_WINNER);

        restPronosticMockMvc.perform(put("/api/pronostics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPronostic)))
            .andExpect(status().isOk());

        // Validate the Pronostic in the database
        List<Pronostic> pronosticList = pronosticRepository.findAll();
        assertThat(pronosticList).hasSize(databaseSizeBeforeUpdate);
        Pronostic testPronostic = pronosticList.get(pronosticList.size() - 1);
        assertThat(testPronostic.getPronoDate()).isEqualTo(UPDATED_PRONO_DATE);
        assertThat(testPronostic.getMatchOrGroupOrcompetId()).isEqualTo(UPDATED_MATCH_OR_GROUP_ORCOMPET_ID);
        assertThat(testPronostic.getScoreHome()).isEqualTo(UPDATED_SCORE_HOME);
        assertThat(testPronostic.getScoreAway()).isEqualTo(UPDATED_SCORE_AWAY);
        assertThat(testPronostic.getWinner()).isEqualTo(UPDATED_WINNER);
    }

    @Test
    @Transactional
    public void updateNonExistingPronostic() throws Exception {
        int databaseSizeBeforeUpdate = pronosticRepository.findAll().size();

        // Create the Pronostic

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPronosticMockMvc.perform(put("/api/pronostics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pronostic)))
            .andExpect(status().isCreated());

        // Validate the Pronostic in the database
        List<Pronostic> pronosticList = pronosticRepository.findAll();
        assertThat(pronosticList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePronostic() throws Exception {
        // Initialize the database
        pronosticService.save(pronostic);

        int databaseSizeBeforeDelete = pronosticRepository.findAll().size();

        // Get the pronostic
        restPronosticMockMvc.perform(delete("/api/pronostics/{id}", pronostic.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Pronostic> pronosticList = pronosticRepository.findAll();
        assertThat(pronosticList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pronostic.class);
        Pronostic pronostic1 = new Pronostic();
        pronostic1.setId(1L);
        Pronostic pronostic2 = new Pronostic();
        pronostic2.setId(pronostic1.getId());
        assertThat(pronostic1).isEqualTo(pronostic2);
        pronostic2.setId(2L);
        assertThat(pronostic1).isNotEqualTo(pronostic2);
        pronostic1.setId(null);
        assertThat(pronostic1).isNotEqualTo(pronostic2);
    }
}
