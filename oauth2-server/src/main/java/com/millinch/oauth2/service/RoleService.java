package com.millinch.oauth2.service;

import com.millinch.oauth2.entity.Role;
import com.millinch.oauth2.exception.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * This guy is busy, nothing left
 *
 * @author John Zhang
 */
public interface RoleService {

    Optional<Role> findOne(long id);

    Page<Role> findAllByName(String name, Pageable pageable);

    Role saveRole(Role role) throws ServiceException;

    Role saveRole(Role role, List<Long> addAuthIdList) throws ServiceException;

    Role updateRole(Role role) throws ServiceException;

    Role updateRoleAndAuthorities(Role role, List<Long> addAuthIdList, List<Long> delAuthIdList) throws ServiceException;

    boolean deleteRole(long id) throws ServiceException;

    boolean deleteRole(List<Long> idList) throws ServiceException;

    boolean isUniqueName(String name);

    boolean isUniqueCode(String code);
}
