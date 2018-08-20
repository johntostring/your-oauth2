package com.millinch.oauth2.repository;

import com.millinch.oauth2.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * This guy is lazy, nothing left.
 *
 * @author John Zhang
 */
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Modifying
    @Query("delete from UserRole ur where ur.user.id = ?1 and ur.role.id in ?2")
    int deleteUserRoles(long userId, List<Long> roleIdList);

    long countByRoleId(Long roleId);
}
