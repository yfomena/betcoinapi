package com.betcoin.backend.web.rest;

import com.betcoin.backend.BetcoinapiApp;

import com.betcoin.backend.domain.Pronotype;
import com.betcoin.backend.repository.PronotypeRepository;
import com.betcoin.backend.service.PronotypeService;
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
 * Test class for the PronotypeResource REST controller.
 *
 * @see PronotypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BetcoinapiApp.class)
public class PronotypeResourceIntTest {

    private static final String DEFAULT_PRONOTYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRONOTYPE_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXPIRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    @Autowired
    private PronotypeRepository pronotypeRepository;

    @Autowired
    private PronotypeService pronotypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPronotypeMockMvc;

    private Pronotype pronotype;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PronotypeResource pronotypeResource = new PronotypeResource(pronotypeService);
        this.restPronotypeMockMvc = MockMvcBuilders.standaloneSetup(pronotypeResource)
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
    public static Pronotype createEntity(EntityManager em) {
        Pronotype pronotype = new Pronotype()
            .pronotypeName(DEFAULT_PRONOTYPE_NAME)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .points(DEFAULT_POINTS);
        return pronotype;
    }

    @Before
    public void initTest() {
        pronotype = createEntity(em);
    }

    @Test
    @Transactional
    public void createPronotype() throws Exception {
        int databaseSizeBeforeCreate = pronotypeRepository.findAll().size();

        // Create the Pronotype
        restPronotypeMockMvc.perform(post("/api/pronotypes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pronotype)))
            .andExpect(status().isCreated());

        // Validate the Pronotype in the database
        List<Pronotype> pronotypeList = pronotypeRepository.findAll();
        assertThat(pronotypeList).hasSize(databaseSizeBeforeCreate + 1);
        Pronotype testPronotype = pronotypeList.get(pronotypeList.size() - 1);
        assertThat(testPronotype.getPronotypeName()).isEqualTo(DEFAULT_PRONOTYPE_NAME);
        assertThat(testPronotype.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testPronotype.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    public void createPronotypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pronotypeRepository.findAll().size();

        // Create the Pronotype with an existing ID
        pronotype.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPronotypeMockMvc.perform(post("/api/pronotypes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pronotype)))
            .andExpect(status().isBadRequest());

        // Validate the Pronotype in the database
        List<Pronotype> pronotypeList = pronotypeRepository.findAll();
        assertThat(pronotypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPronotypes() throws Exception {
        // Initialize the database
        pronotypeRepository.saveAndFlush(pronotype);

        // Get all the pronotypeList
        restPronotypeMockMvc.perform(get("/api/pronotypes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pronotype.getId().intValue())))
            .andExpect(jsonPath("$.[*].pronotypeName").value(hasItem(DEFAULT_PRONOTYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)));
    }

    @Test
    @Transactional
    public void getPronotype() throws Exception {
        // Initialize the database
        pronotypeRepository.saveAndFlush(pronotype);

        // Get the pronotype
        restPronotypeMockMvc.perform(get("/api/pronotypes/{id}", pronotype.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pronotype.getId().intValue()))
            .andExpect(jsonPath("$.pronotypeName").value(DEFAULT_PRONOTYPE_NAME.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS));
    }

    @Test
    @Transactional
    public void getNonExistingPronotype() throws Exception {
        // Get the pronotype
        restPronotypeMockMvc.perform(get("/api/pronotypes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePronotype() throws Exception {
        // Initialize the database
        pronotypeService.save(pronotype);

        int databaseSizeBeforeUpdate = pronotypeRepository.findAll().size();

        // Update the pronotype
        Pronotype updatedPronotype = pronotypeRepository.findOne(pronotype.getId());
        // Disconnect from session so that the updates on updatedPronotype are not directly saved in db
        em.detach(updatedPronotype);
        updatedPronotype
            .pronotypeName(UPDATED_PRONOTYPE_NAME)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .points(UPDATED_POINTS);

        restPronotypeMockMvc.perform(put("/api/pronotypes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPronotype)))
            .andExpect(status().isOk());

        // Validate the Pronotype in the database
        List<Pronotype> pronotypeList = pronotypeRepository.findAll();
        assertThat(pronotypeList).hasSize(databaseSizeBeforeUpdate);
        Pronotype testPronotype = pronotypeList.get(pronotypeList.size() - 1);
        assertThat(testPronotype.getPronotypeName()).isEqualTo(UPDATED_PRONOTYPE_NAME);
        assertThat(testPronotype.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testPronotype.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void updateNonExistingPronotype() throws Exception {
        int databaseSizeBeforeUpdate = pronotypeRepository.findAll().size();

        // Create the Pronotype

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPronotypeMockMvc.perform(put("/api/pronotypes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pronotype)))
            .andExpect(status().isCreated());

        // Validate the Pronotype in the database
        List<Pronotype> pronotypeList = pronotypeRepository.findAll();
        assertThat(pronotypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePronotype() throws Exception {
        // Initialize the database
        pronotypeService.save(pronotype);

        int databaseSizeBeforeDelete = pronotypeRepository.findAll().size();

        // Get the pronotype
        restPronotypeMockMvc.perform(delete("/api/pronotypes/{id}", pronotype.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Pronotype> pronotypeList = pronotypeRepository.findAll();
        assertThat(pronotypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pronotype.class);
        Pronotype pronotype1 = new Pronotype();
        pronotype1.setId(1L);
        Pronotype pronotype2 = new Pronotype();
        pronotype2.setId(pronotype1.getId());
        assertThat(pronotype1).isEqualTo(pronotype2);
        pronotype2.setId(2L);
        assertThat(pronotype1).isNotEqualTo(pronotype2);
        pronotype1.setId(null);
        assertThat(pronotype1).isNotEqualTo(pronotype2);
    }
}
