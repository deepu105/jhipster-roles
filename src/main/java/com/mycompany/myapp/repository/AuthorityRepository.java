package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Authority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {

	@Query("select a from Authority a")
	Page<Authority> findAll(Pageable pageable);

	Authority findOneByName(String name);
}
