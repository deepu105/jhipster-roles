package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.repository.AuthorityRepository;
import com.mycompany.myapp.security.ResourcesConstant;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import com.mycompany.myapp.web.rest.dto.AuthorityDTO;
import com.mycompany.myapp.web.rest.mapper.AuthorityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Role.
 */
@RestController
@RequestMapping("/api")
public class RoleResource {

    private final Logger log = LoggerFactory.getLogger(RoleResource.class);

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private AuthorityMapper authorityMapper;

    /**
     * POST  /roles : Create a new role.
     *
     * @param authorityDTO the authorityDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new authorityDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/roles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('ROLE_RESOURCE', 'CREATE')")
    public ResponseEntity<AuthorityDTO> createRole(@Valid @RequestBody AuthorityDTO authorityDTO) throws URISyntaxException {
        log.debug("REST request to save Role : {}", authorityDTO);
        Authority role = authorityMapper.authorityDTOToAuthority(authorityDTO);
        role = authorityRepository.save(role);
        AuthorityDTO result = authorityMapper.authorityToAuthorityDTO(role);
        return ResponseEntity.created(new URI("/api/roles/" + result.getName()))
            .headers(HeaderUtil.createEntityCreationAlert("role", result.getName()))
            .body(result);
    }

    /**
     * PUT  /roles : Updates an existing role.
     *
     * @param authorityDTO the authorityDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated authorityDTO,
     * or with status 400 (Bad Request) if the roleDTO is not valid,
     * or with status 500 (Internal Server Error) if the authorityDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/roles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('ROLE_RESOURCE', 'EDIT')")
    public ResponseEntity<AuthorityDTO> updateRole(@Valid @RequestBody AuthorityDTO authorityDTO) throws URISyntaxException {
        log.debug("REST request to update Role : {}", authorityDTO);
        Authority role = authorityMapper.authorityDTOToAuthority(authorityDTO);
        role = authorityRepository.save(role);
        AuthorityDTO result = authorityMapper.authorityToAuthorityDTO(role);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("role", authorityDTO.getName()))
            .body(result);
    }

    /**
     * GET  /roles : get all the roles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of roles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/roles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    @PreAuthorize("@jhiAuth.hasPermission('ROLE_RESOURCE', 'VIEW')")
    public ResponseEntity<List<AuthorityDTO>> getAllRoles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Roles");
        Page<Authority> page = authorityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/roles");
        return new ResponseEntity<>(authorityMapper.authoritiesToAuthorityDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /roles/:id : get the "name" role.
     *
     * @param name the name of the authorityDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the authorityDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/roles/{name}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('ROLE_RESOURCE', 'VIEW')")
    public ResponseEntity<AuthorityDTO> getRole(@PathVariable String name) {
        log.debug("REST request to get Role : {}", name);
        Authority role = authorityRepository.findOne(name);
        AuthorityDTO authorityDTO = authorityMapper.authorityToAuthorityDTO(role);
        return Optional.ofNullable(authorityDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /roles/:id : delete the "name" role.
     *
     * @param name the id of the authorityDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/roles/{name}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @PreAuthorize("@jhiAuth.hasPermission('ROLE_RESOURCE', 'DELETE')")
    public ResponseEntity<Void> deleteRole(@PathVariable String name) {
        log.debug("REST request to delete Role : {}", name);
        authorityRepository.delete(name);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("role", name)).build();
    }

    /**
     * GET  /resources : get all the resources.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of resources in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/resources",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    @PreAuthorize("@jhiAuth.hasPermission('ROLE_RESOURCE', 'VIEW')")
    public ResponseEntity<List<ResourcesConstant.Resource>> getAllResources()
        throws URISyntaxException {
        log.debug("REST request to get all static resources");
        List<ResourcesConstant.Resource> resources = ResourcesConstant.getAllResources();
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

}
