package com.millinch.oauth2.repository;

import com.millinch.oauth2.entity.RoleAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * This guy is lazy, nothing left.
 *
 * @author John Zhang
 */
public interface RoleAuthorityRepository extends JpaRepository<RoleAuthority, Long> {

    @Modifying
    @Query("delete from RoleAuthority ra where ra.role.id = ?1 and ra.authority.id in ?2")
    int deleteRoleAuthorities(long roleId, List<Long> authIds);

    long countByAuthorityId(long authorityId);

}
