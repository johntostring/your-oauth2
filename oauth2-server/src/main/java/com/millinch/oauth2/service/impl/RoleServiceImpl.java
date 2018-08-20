package com.millinch.oauth2.service.impl;

import com.millinch.oauth2.entity.Authority;
import com.millinch.oauth2.entity.Role;
import com.millinch.oauth2.entity.RoleAuthority;
import com.millinch.oauth2.exception.ServiceException;
import com.millinch.oauth2.repository.RoleAuthorityRepository;
import com.millinch.oauth2.repository.RoleRepository;
import com.millinch.oauth2.repository.UserRoleRepository;
import com.millinch.oauth2.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This guy is busy, nothing left
 *
 * @author John Zhang
 */
@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    private final RoleAuthorityRepository roleAuthorityRepository;

    private final UserRoleRepository userRoleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository,
                           RoleAuthorityRepository roleAuthorityRepository,
                           UserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.roleAuthorityRepository = roleAuthorityRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Optional<Role> findOne(long id) {
        return this.roleRepository.findById(id);
    }

    @Override
    public Page<Role> findAllByName(String name, Pageable pageable) {
        if (StringUtils.hasText(name)) {
            return this.roleRepository.findAllByNameContains(name, pageable);
        }
        return this.roleRepository.findAll(pageable);
    }

    @Override
    public Role saveRole(Role role) throws ServiceException {
        boolean uniqueCode = StringUtils.hasText(role.getCode())
                && this.isUniqueCode(role.getCode());
        if (!uniqueCode) {
            throw new ServiceException("角色代码已存在");
        }
        boolean uniqueName = StringUtils.hasText(role.getCode())
                && this.isUniqueName(role.getName());
        if (!uniqueName) {
            throw new ServiceException("角色名称已存在");
        }
        return this.roleRepository.save(role);
    }

    @Override
    public Role saveRole(Role role, List<Long> addAuthIdList) throws ServiceException {
        String errorMsg = null;
        if (!StringUtils.hasText(role.getCode())) {
            errorMsg = "角色代码不能为空";
        } else if (!role.getCode().startsWith("ROLE_")) {
            errorMsg = "角色代码必须以 ROLE_ 开始";
        } else if (!isUniqueCode(role.getCode())) {
            errorMsg = "角色代码已存在";
        } else if (!StringUtils.hasText(role.getName())) {
            errorMsg = "角色名称不能为空";
        } else if (!isUniqueName(role.getName())) {
            errorMsg = "角色名称已存在";
        }
        if (errorMsg != null) {
            throw new ServiceException(errorMsg);
        }
        List<RoleAuthority> addAuthorityList = instanceRoleAuthorities(role, addAuthIdList);
        Role saveRole = null;
        if (addAuthorityList.size() > 0) {
            saveRole = this.saveRole(role);
            this.roleAuthorityRepository.saveAll(addAuthorityList);
            LOGGER.info("角色{}增加{}个权限", role.getName(), addAuthorityList.size());
        } else {
            saveRole = this.saveRole(role);
        }
        return saveRole;
    }

    private List<RoleAuthority> instanceRoleAuthorities(Role role, List<Long> addAuthIdList) {
        List<RoleAuthority> addAuthorityList = new ArrayList<>();
        if (addAuthIdList != null && addAuthIdList.size() > 0) {
            for (Long authId : addAuthIdList) {
                RoleAuthority roleAuthority = new RoleAuthority();
                Authority authority = new Authority();
                authority.setId(authId);
                roleAuthority.setRole(role);
                roleAuthority.setAuthority(authority);
                addAuthorityList.add(roleAuthority);
            }
        }
        return addAuthorityList;
    }

    @Override
    public Role updateRole(Role role) throws ServiceException {
        if (role == null || role.getId() == null) {
            throw new ServiceException("角色不存在");
        }
        Optional<Role> optionalRole = this.roleRepository.findById(role.getId());
        if (!optionalRole.isPresent()) {
            throw new ServiceException("角色不存在");
        }
        boolean dirty = false;
        Role roleInDb = optionalRole.get();
        if (StringUtils.hasText(role.getCode())
            && !role.getCode().equals(roleInDb.getCode())) {
            if (this.isUniqueCode(role.getCode())) {
                dirty = true;
                roleInDb.setCode(role.getCode());
            } else {
                throw new ServiceException("角色代码已存在");
            }
        }
        if (StringUtils.hasText(role.getName())
            && !role.getName().equals(roleInDb.getName())) {
            if (this.isUniqueName(role.getName())) {
                dirty = true;
                roleInDb.setName(role.getName());
            } else {
                throw new ServiceException("角色名称已存在");
            }
        }
        if (StringUtils.hasText(role.getRemark())
            && !role.getRemark().equals(roleInDb.getRemark())) {
            dirty = true;
            roleInDb.setRemark(role.getRemark());
        }
        if (dirty) {
            return roleRepository.save(roleInDb);
        }
        return roleInDb;
    }

    @Override
    @Transactional(rollbackFor = {ServiceException.class})
    public Role updateRoleAndAuthorities(Role role, List<Long> addAuthIdList, List<Long> delAuthIdList) throws ServiceException {
        try {
            List<RoleAuthority> addAuthorityList = instanceRoleAuthorities(role, addAuthIdList);
            if (addAuthorityList.size() > 0) {
                LOGGER.info("角色:id={}增加{}个权限", role.getId(), addAuthorityList.size());
                this.roleAuthorityRepository.saveAll(addAuthorityList);
            }
            int delRows = 0;
            if (delAuthIdList != null && delAuthIdList.size() > 0) {
                delRows = this.roleAuthorityRepository.deleteRoleAuthorities(role.getId(), delAuthIdList);
                LOGGER.info("角色:id={}删除{}个权限", role.getId(), delRows);
            }
            return this.updateRole(role);
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException("包含已授予此角色的权限，请勿重复授权");
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteRole(long id) throws ServiceException {
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (!optionalRole.isPresent()) {
            throw new ServiceException("角色不存在");
        }
        try {
            roleRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            LOGGER.info("删除角色失败：id={}", id, e);
            return false;
        }
    }

    @Override
    public boolean deleteRole(List<Long> idList) throws ServiceException {
        try {
            List<Role> all = roleRepository.findAllById(idList);
            roleRepository.deleteAll(all);
            return true;
        } catch (Exception e) {
            throw new ServiceException("批量删除角色失败");
        }
    }

    @Override
    public boolean isUniqueName(String name) {
        return this.roleRepository.countByName(name) <= 0;
    }

    @Override
    public boolean isUniqueCode(String code) {
        return this.roleRepository.countByCode(code) <= 0;
    }
}
