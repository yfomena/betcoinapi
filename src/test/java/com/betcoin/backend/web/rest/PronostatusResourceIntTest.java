package com.betcoin.backend.web.rest;

import com.betcoin.backend.BetcoinapiApp;

import com.betcoin.backend.domain.Pronostatus;
import com.betcoin.backend.repository.PronostatusRepository;
import com.betcoin.backend.service.PronostatusService;
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
 * Test class for the PronostatusResource REST controller.
 *
 * @see PronostatusResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BetcoinapiApp.class)
public class PronostatusResourceIntTest {

    private static final String DEFAULT_PRONOSTATUS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRONOSTATUS_NAME = "BBBBBBBBBB";

    @Autowired
    private PronostatusRepository pronostatusRepository;

    @Autowired
    private PronostatusService pronostatusService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPronostatusMockMvc;

    private Pronostatus pronostatus;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PronostatusResource pronostatusResource = new PronostatusResource(pronostatusService);
        this.restPronostatusMockMvc = MockMvcBuilders.standaloneSetup(pronostatusResource)
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
    public static Pronostatus createEntity(EntityManager em) {
        Pronostatus pronostatus = new Pronostatus()
            .pronostatusName(DEFAULT_PRONOSTATUS_NAME);
        return pronostatus;
    }

    @Before
    public void initTest() {
        pronostatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createPronostatus() throws Exception {
        int databaseSizeBeforeCreate = pronostatusRepository.findAll().size();

        // Create the Pronostatus
        restPronostatusMockMvc.perform(post("/api/pronostatuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pronostatus)))
            .andExpect(status().isCreated());

        // Validate the Pronostatus in the database
        List<Pronostatus> pronostatusList = pronostatusRepository.findAll();
        assertThat(pronostatusList).hasSize(databaseSizeBeforeCreate + 1);
        Pronostatus testPronostatus = pronostatusList.get(pronostatusList.size() - 1);
        assertThat(testPronostatus.getPronostatusName()).isEqualTo(DEFAULT_PRONOSTATUS_NAME);
    }

    @Test
    @Transactional
    public void createPronostatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pronostatusRepository.findAll().size();

        // Create the Pronostatus with an existing ID
        pronostatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPronostatusMockMvc.perform(post("/api/pronostatuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pronostatus)))
            .andExpect(status().isBadRequest());

        // Validate the Pronostatus in the database
        List<Pronostatus> pronostatusList = pronostatusRepository.findAll();
        assertThat(pronostatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPronostatuses() throws Exception {
        // Initialize the database
        pronostatusRepository.saveAndFlush(pronostatus);

        // Get all the pronostatusList
        restPronostatusMockMvc.perform(get("/api/pronostatuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pronostatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].pronostatusName").value(hasItem(DEFAULT_PRONOSTATUS_NAME.toString())));
    }

    @Test
    @Transactional
    public void getPronostatus() throws Exception {
        // Initialize the database
        pronostatusRepository.saveAndFlush(pronostatus);

        // Get the pronostatus
        restPronostatusMockMvc.perform(get("/api/pronostatuses/{id}", pronostatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pronostatus.getId().intValue()))
            .andExpect(jsonPath("$.pronostatusName").value(DEFAULT_PRONOSTATUS_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPronostatus() throws Exception {
        // Get the pronostatus
        restPronostatusMockMvc.perform(get("/api/pronostatuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePronostatus() throws Exception {
        // Initialize the database
        pronostatusService.save(pronostatus);

        int databaseSizeBeforeUpdate = pronostatusRepository.findAll().size();

        // Update the pronostatus
        Pronostatus updatedPronostatus = pronostatusRepository.findOne(pronostatus.getId());
        // Disconnect from session so that the updates on updatedPronostatus are not directly saved in db
        em.detach(updatedPronostatus);
        updatedPronostatus
            .pronostatusName(UPDATED_PRONOSTATUS_NAME);

        restPronostatusMockMvc.perform(put("/api/pronostatuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPronostatus)))
            .andExpect(status().isOk());

        // Validate the Pronostatus in the database
        List<Pronostatus> pronostatusList = pronostatusRepository.findAll();
        assertThat(pronostatusList).hasSize(databaseSizeBeforeUpdate);
        Pronostatus testPronostatus = pronostatusList.get(pronostatusList.size() - 1);
        assertThat(testPronostatus.getPronostatusName()).isEqualTo(UPDATED_PRONOSTATUS_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingPronostatus() throws Exception {
        int databaseSizeBeforeUpdate = pronostatusRepository.findAll().size();

        // Create the Pronostatus

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPronostatusMockMvc.perform(put("/api/pronostatuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pronostatus)))
            .andExpect(status().isCreated());

        // Validate the Pronostatus in the database
        List<Pronostatus> pronostatusList = pronostatusRepository.findAll();
        assertThat(pronostatusList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePronostatus() throws Exception {
        // Initialize the database
        pronostatusService.save(pronostatus);

        int databaseSizeBeforeDelete = pronostatusRepository.findAll().size();

        // Get the pronostatus
        restPronostatusMockMvc.perform(delete("/api/pronostatuses/{id}", pronostatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Pronostatus> pronostatusList = pronostatusRepository.findAll();
        assertThat(pronostatusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pronostatus.class);
        Pronostatus pronostatus1 = new Pronostatus();
        pronostatus1.setId(1L);
        Pronostatus pronostatus2 = new Pronostatus();
        pronostatus2.setId(pronostatus1.getId());
        assertThat(pronostatus1).isEqualTo(pronostatus2);
        pronostatus2.setId(2L);
        assertThat(pronostatus1).isNotEqualTo(pronostatus2);
        pronostatus1.setId(null);
        assertThat(pronostatus1).isNotEqualTo(pronostatus2);
    }
}
