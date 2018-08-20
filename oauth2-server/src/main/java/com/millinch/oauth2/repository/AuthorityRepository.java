package com.millinch.oauth2.repository;

import com.millinch.oauth2.entity.Authority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This guy is lazy, nothing left.
 *
 * @author John Zhang
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Page<Authority> findAllByNameLike(String name, Pageable pageable);

    Page<Authority> findAllByNameContains(String name, Pageable pageable);

    long countByName(String name);

    long countByCode(String code);
}
