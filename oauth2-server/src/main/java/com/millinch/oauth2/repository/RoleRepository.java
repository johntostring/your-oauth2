package com.millinch.oauth2.repository;

import com.millinch.oauth2.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This guy is lazy, nothing left.
 *
 * @author John Zhang
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByCodeIn(String... code);

    Page<Role> findAllByNameLike(String name, Pageable pageable);

    Page<Role> findAllByNameContains(String name, Pageable pageable);

    long countByName(String name);

    long countByCode(String code);
}
