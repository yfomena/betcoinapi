package com.betcoin.backend.web.rest;

import com.betcoin.backend.BetcoinapiApp;

import com.betcoin.backend.domain.Gamer;
import com.betcoin.backend.repository.GamerRepository;
import com.betcoin.backend.service.GamerService;
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
 * Test class for the GamerResource REST controller.
 *
 * @see GamerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BetcoinapiApp.class)
public class GamerResourceIntTest {

    private static final String DEFAULT_PSEUDO = "AAAAAAAAAA";
    private static final String UPDATED_PSEUDO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final Instant DEFAULT_LAST_CONNEXION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_CONNEXION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_WINNING_RUN = 1;
    private static final Integer UPDATED_WINNING_RUN = 2;

    private static final Boolean DEFAULT_IS_ADMIN = false;
    private static final Boolean UPDATED_IS_ADMIN = true;

    @Autowired
    private GamerRepository gamerRepository;

    @Autowired
    private GamerService gamerService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGamerMockMvc;

    private Gamer gamer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GamerResource gamerResource = new GamerResource(gamerService);
        this.restGamerMockMvc = MockMvcBuilders.standaloneSetup(gamerResource)
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
    public static Gamer createEntity(EntityManager em) {
        Gamer gamer = new Gamer()
            .pseudo(DEFAULT_PSEUDO)
            .email(DEFAULT_EMAIL)
            .points(DEFAULT_POINTS)
            .lastConnexion(DEFAULT_LAST_CONNEXION)
            .winningRun(DEFAULT_WINNING_RUN)
            .isAdmin(DEFAULT_IS_ADMIN);
        return gamer;
    }

    @Before
    public void initTest() {
        gamer = createEntity(em);
    }

    @Test
    @Transactional
    public void createGamer() throws Exception {
        int databaseSizeBeforeCreate = gamerRepository.findAll().size();

        // Create the Gamer
        restGamerMockMvc.perform(post("/api/gamers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gamer)))
            .andExpect(status().isCreated());

        // Validate the Gamer in the database
        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeCreate + 1);
        Gamer testGamer = gamerList.get(gamerList.size() - 1);
        assertThat(testGamer.getPseudo()).isEqualTo(DEFAULT_PSEUDO);
        assertThat(testGamer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testGamer.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testGamer.getLastConnexion()).isEqualTo(DEFAULT_LAST_CONNEXION);
        assertThat(testGamer.getWinningRun()).isEqualTo(DEFAULT_WINNING_RUN);
        assertThat(testGamer.isIsAdmin()).isEqualTo(DEFAULT_IS_ADMIN);
    }

    @Test
    @Transactional
    public void createGamerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gamerRepository.findAll().size();

        // Create the Gamer with an existing ID
        gamer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGamerMockMvc.perform(post("/api/gamers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gamer)))
            .andExpect(status().isBadRequest());

        // Validate the Gamer in the database
        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllGamers() throws Exception {
        // Initialize the database
        gamerRepository.saveAndFlush(gamer);

        // Get all the gamerList
        restGamerMockMvc.perform(get("/api/gamers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gamer.getId().intValue())))
            .andExpect(jsonPath("$.[*].pseudo").value(hasItem(DEFAULT_PSEUDO.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].lastConnexion").value(hasItem(DEFAULT_LAST_CONNEXION.toString())))
            .andExpect(jsonPath("$.[*].winningRun").value(hasItem(DEFAULT_WINNING_RUN)))
            .andExpect(jsonPath("$.[*].isAdmin").value(hasItem(DEFAULT_IS_ADMIN.booleanValue())));
    }

    @Test
    @Transactional
    public void getGamer() throws Exception {
        // Initialize the database
        gamerRepository.saveAndFlush(gamer);

        // Get the gamer
        restGamerMockMvc.perform(get("/api/gamers/{id}", gamer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gamer.getId().intValue()))
            .andExpect(jsonPath("$.pseudo").value(DEFAULT_PSEUDO.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.lastConnexion").value(DEFAULT_LAST_CONNEXION.toString()))
            .andExpect(jsonPath("$.winningRun").value(DEFAULT_WINNING_RUN))
            .andExpect(jsonPath("$.isAdmin").value(DEFAULT_IS_ADMIN.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGamer() throws Exception {
        // Get the gamer
        restGamerMockMvc.perform(get("/api/gamers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGamer() throws Exception {
        // Initialize the database
        gamerService.save(gamer);

        int databaseSizeBeforeUpdate = gamerRepository.findAll().size();

        // Update the gamer
        Gamer updatedGamer = gamerRepository.findOne(gamer.getId());
        // Disconnect from session so that the updates on updatedGamer are not directly saved in db
        em.detach(updatedGamer);
        updatedGamer
            .pseudo(UPDATED_PSEUDO)
            .email(UPDATED_EMAIL)
            .points(UPDATED_POINTS)
            .lastConnexion(UPDATED_LAST_CONNEXION)
            .winningRun(UPDATED_WINNING_RUN)
            .isAdmin(UPDATED_IS_ADMIN);

        restGamerMockMvc.perform(put("/api/gamers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGamer)))
            .andExpect(status().isOk());

        // Validate the Gamer in the database
        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeUpdate);
        Gamer testGamer = gamerList.get(gamerList.size() - 1);
        assertThat(testGamer.getPseudo()).isEqualTo(UPDATED_PSEUDO);
        assertThat(testGamer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testGamer.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testGamer.getLastConnexion()).isEqualTo(UPDATED_LAST_CONNEXION);
        assertThat(testGamer.getWinningRun()).isEqualTo(UPDATED_WINNING_RUN);
        assertThat(testGamer.isIsAdmin()).isEqualTo(UPDATED_IS_ADMIN);
    }

    @Test
    @Transactional
    public void updateNonExistingGamer() throws Exception {
        int databaseSizeBeforeUpdate = gamerRepository.findAll().size();

        // Create the Gamer

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGamerMockMvc.perform(put("/api/gamers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gamer)))
            .andExpect(status().isCreated());

        // Validate the Gamer in the database
        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGamer() throws Exception {
        // Initialize the database
        gamerService.save(gamer);

        int databaseSizeBeforeDelete = gamerRepository.findAll().size();

        // Get the gamer
        restGamerMockMvc.perform(delete("/api/gamers/{id}", gamer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Gamer> gamerList = gamerRepository.findAll();
        assertThat(gamerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gamer.class);
        Gamer gamer1 = new Gamer();
        gamer1.setId(1L);
        Gamer gamer2 = new Gamer();
        gamer2.setId(gamer1.getId());
        assertThat(gamer1).isEqualTo(gamer2);
        gamer2.setId(2L);
        assertThat(gamer1).isNotEqualTo(gamer2);
        gamer1.setId(null);
        assertThat(gamer1).isNotEqualTo(gamer2);
    }
}
