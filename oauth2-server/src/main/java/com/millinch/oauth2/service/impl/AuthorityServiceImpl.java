package com.millinch.oauth2.service.impl;

import com.millinch.oauth2.entity.Authority;
import com.millinch.oauth2.exception.ServiceException;
import com.millinch.oauth2.repository.AuthorityRepository;
import com.millinch.oauth2.repository.RoleAuthorityRepository;
import com.millinch.oauth2.service.AuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * This guy is busy, nothing left
 *
 * @author John Zhang
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final Logger LOGGER = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    private final AuthorityRepository authorityRepository;

    private final RoleAuthorityRepository roleAuthorityRepository;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository authorityRepository, RoleAuthorityRepository roleAuthorityRepository) {
        this.authorityRepository = authorityRepository;
        this.roleAuthorityRepository = roleAuthorityRepository;
    }

    @Override
    public Optional<Authority> findOne(long id) {
        return authorityRepository.findById(id);
    }

    @Override
    public Page<Authority> findAllByName(String name, Pageable pageable) {
        if (StringUtils.hasText(name)) {
            return authorityRepository.findAllByNameContains(name, pageable);
        }
        return authorityRepository.findAll(pageable);
    }

    @Override
    public Authority saveAuthority(Authority authority) throws ServiceException {
        boolean uniqueCode = StringUtils.hasText(authority.getCode())
                && this.isUniqueCode(authority.getCode());
        if (!uniqueCode) {
            throw new ServiceException("权限代码已存在");
        }
        boolean uniqueName = StringUtils.hasText(authority.getCode())
                && this.isUniqueName(authority.getName());
        if (!uniqueName) {
            throw new ServiceException("权限名称已存在");
        }
        return authorityRepository.save(authority);
    }

    @Override
    public Authority updateAuthority(Authority authority) throws ServiceException {
        if (authority == null || authority.getId() == null) {
            throw new ServiceException("权限不存在");
        }
        Optional<Authority> optionalAuthority = authorityRepository.findById(authority.getId());
        if (!optionalAuthority.isPresent()) {
            throw new ServiceException("权限不存在");
        }
        boolean dirty = false;
        Authority oneInDb = optionalAuthority.get();
        if (StringUtils.hasText(authority.getCode())
            && !authority.getCode().equals(oneInDb.getCode())) {
            if (this.isUniqueCode(authority.getCode())) {
                dirty = true;
                oneInDb.setCode(authority.getCode());
            } else {
                throw new ServiceException("权限代码已存在");
            }
        }
        if (StringUtils.hasText(authority.getName())
            && !authority.getName().equals(oneInDb.getName())) {
            if (this.isUniqueName(authority.getName())) {
                dirty = true;
                oneInDb.setName(authority.getName());
            } else {
                throw new ServiceException("权限名称已存在");
            }
        }
        if (StringUtils.hasText(authority.getRemark())
            && !authority.getRemark().equals(oneInDb.getRemark())) {
            dirty = true;
            oneInDb.setRemark(authority.getRemark());
        }
        if (dirty) {
            return authorityRepository.save(oneInDb);
        }
        return oneInDb;
    }

    @Override
    public boolean deleteAuthority(long id) throws ServiceException {
        Optional<Authority> optionalAuthority = authorityRepository.findById(id);
        if (!optionalAuthority.isPresent()) {
            throw new ServiceException("权限不存在");
        }
        try {
            authorityRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            LOGGER.error("删除 Authority 失败：id={}", id, e);
            return false;
        }
    }

    @Override
    public boolean deleteAuthority(List<Long> idList) {
        try {
            List<Authority> all = authorityRepository.findAllById(idList);
            authorityRepository.deleteAll(all);
            return true;
        } catch (Exception e) {
            LOGGER.info("批量删除 Authority 失败", e);
            return false;
        }
    }

    @Override
    public boolean isUniqueName(String name) {
        return authorityRepository.countByName(name) <= 0;
    }

    @Override
    public boolean isUniqueCode(String code) {
        return authorityRepository.countByCode(code) <= 0;
    }
}
