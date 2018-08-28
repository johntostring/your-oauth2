package com.millinch.oauth2.service.impl;

import com.millinch.oauth2.config.YourUserDetailsManager;
import com.millinch.oauth2.entity.OAuthUser;
import com.millinch.oauth2.entity.Role;
import com.millinch.oauth2.entity.UserRole;
import com.millinch.oauth2.exception.ServiceException;
import com.millinch.oauth2.repository.UserRepository;
import com.millinch.oauth2.repository.UserRoleRepository;
import com.millinch.oauth2.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * This guy is busy, nothing left
 *
 * @author John Zhang
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final YourUserDetailsManager yourUserDetailsManager;

    private final UserRepository userRepository;

    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           YourUserDetailsManager yourUserDetailsManager, UserRoleRepository userRoleRepository) {
        this.yourUserDetailsManager = yourUserDetailsManager;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @Transactional(rollbackFor = {ServiceException.class})
    public OAuthUser createUser(OAuthUser oauthUser) throws ServiceException {
        try {
            yourUserDetailsManager.createUser(oauthUser);
        } catch (Exception e) {
            LOGGER.error("保存用户到数据库失败", e);
            throw new ServiceException("保存用户到数据库失败");
        }
        String failedMessage = "用户没有保存成功";

        OAuthUser userInDb = userRepository.findOneByUsername(oauthUser.getUsername());
        if (userInDb == null || userInDb.getId() == null) {
            LOGGER.error(failedMessage);
            throw new ServiceException(failedMessage);
        }
        return userInDb;
    }

    @Override
    @Transactional(rollbackFor = {ServiceException.class})
    public boolean updateUserRoles(long userId, List<Long> addRoleIdList, List<Long> delRoleIdList) throws ServiceException {
        try {
            List<UserRole> addUserRoleList = instanceUserRoles(userId, addRoleIdList);
            if (addUserRoleList.size() > 0) {
                this.userRoleRepository.save(addUserRoleList);
                LOGGER.info("用户:id={}增加{}个角色", userId, addUserRoleList.size());
            }
            int delRows;
            if (delRoleIdList != null && delRoleIdList.size() > 0) {
                delRows = this.userRoleRepository.deleteUserRoles(userId, delRoleIdList);
                LOGGER.info("用户:id={}删除{}个角色", userId, delRows);
            }
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException("包含已授予此用户的角色，请勿重复授权");
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private List<UserRole> instanceUserRoles(long userId, List<Long> addRoleIdList) {
        List<UserRole> addUserRoleList = new ArrayList<>();
        for (Long roleId : addRoleIdList) {
            OAuthUser user = new OAuthUser();
            user.setId(userId);
            Role role = new Role();
            role.setId(roleId);
            UserRole userRole = new UserRole(user, role);
            addUserRoleList.add(userRole);
        }
        return addUserRoleList;
    }
}
