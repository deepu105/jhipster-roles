package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.web.rest.dto.AuthorityDTO;
import com.mycompany.myapp.web.rest.mapper.AuthorityMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the RoleResource REST controller.
 *
 * @see RoleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JhipsterApp.class)
@WebAppConfiguration
@IntegrationTest
public class RoleResourceIntTest {

    private static final String DEFAULT_NAME = "";
    private static final String UPDATED_NAME = "";

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private AuthorityMapper authorityMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRoleMockMvc;

    private Authority role;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RoleResource roleResource = new RoleResource();
        ReflectionTestUtils.setField(roleResource, "authorityRepository", authorityRepository);
        ReflectionTestUtils.setField(roleResource, "authorityMapper", authorityMapper);
        this.restRoleMockMvc = MockMvcBuilders.standaloneSetup(roleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        role = new Authority();
        role.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createRole() throws Exception {
        int databaseSizeBeforeCreate = authorityRepository.findAll().size();

        // Create the Role
        AuthorityDTO authorityDTO = authorityMapper.authorityToAuthorityDTO(role);

        restRoleMockMvc.perform(post("/api/roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(authorityDTO)))
                .andExpect(status().isCreated());

        // Validate the Role in the database
        List<Authority> roles = authorityRepository.findAll();
        assertThat(roles).hasSize(databaseSizeBeforeCreate + 1);
        Authority testRole = roles.get(roles.size() - 1);
        assertThat(testRole.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = authorityRepository.findAll().size();
        // set the field null
        role.setName(null);

        // Create the Role, which fails.
        AuthorityDTO authorityDTO = authorityMapper.authorityToAuthorityDTO(role);

        restRoleMockMvc.perform(post("/api/roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(authorityDTO)))
                .andExpect(status().isBadRequest());

        List<Authority> roles = authorityRepository.findAll();
        assertThat(roles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRoles() throws Exception {
        // Initialize the database
        authorityRepository.saveAndFlush(role);

        // Get all the roles
        restRoleMockMvc.perform(get("/api/roles?sort=name,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getRole() throws Exception {
        // Initialize the database
        authorityRepository.saveAndFlush(role);

        // Get the role
        restRoleMockMvc.perform(get("/api/roles/{name}", role.getName()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRole() throws Exception {
        // Get the role
        restRoleMockMvc.perform(get("/api/roles/{name}", "SOME_NE_ROLE"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRole() throws Exception {
        // Initialize the database
        authorityRepository.saveAndFlush(role);
        int databaseSizeBeforeUpdate = authorityRepository.findAll().size();

        // Update the role
        Authority updatedRole = new Authority();
        updatedRole.setName(UPDATED_NAME);
        AuthorityDTO authorityDTO = authorityMapper.authorityToAuthorityDTO(updatedRole);

        restRoleMockMvc.perform(put("/api/roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(authorityDTO)))
                .andExpect(status().isOk());

        // Validate the Role in the database
        List<Authority> roles = authorityRepository.findAll();
        assertThat(roles).hasSize(databaseSizeBeforeUpdate);
        Authority testRole = roles.get(roles.size() - 1);
        assertThat(testRole.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteRole() throws Exception {
        // Initialize the database
        authorityRepository.saveAndFlush(role);
        int databaseSizeBeforeDelete = authorityRepository.findAll().size();

        // Get the role
        restRoleMockMvc.perform(delete("/api/roles/{name}", role.getName())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Authority> roles = authorityRepository.findAll();
        assertThat(roles).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRole() throws Exception {
        // Initialize the database
        authorityRepository.saveAndFlush(role);

        // Search the role
        restRoleMockMvc.perform(get("/api/_search/roles?query=id:" + role.getName()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
}
