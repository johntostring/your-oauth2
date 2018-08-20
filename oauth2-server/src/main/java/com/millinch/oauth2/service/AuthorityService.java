package com.millinch.oauth2.service;

import com.millinch.oauth2.entity.Authority;
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
public interface AuthorityService {

    Optional<Authority> findOne(long id);

    Page<Authority> findAllByName(String name, Pageable pageable);

    Authority saveAuthority(Authority authority) throws ServiceException;

    Authority updateAuthority(Authority authority) throws ServiceException;

    boolean deleteAuthority(long id) throws ServiceException;

    boolean deleteAuthority(List<Long> idList);

    boolean isUniqueName(String name);

    boolean isUniqueCode(String code);
}
