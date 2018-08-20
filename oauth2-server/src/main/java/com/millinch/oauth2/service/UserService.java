package com.millinch.oauth2.service;


import com.millinch.oauth2.entity.OAuthUser;
import com.millinch.oauth2.exception.ServiceException;

import java.util.List;

/**
 * This guy is busy, nothing left
 *
 * @author John Zhang
 */
public interface UserService {

    /**
     *  创建一个用户，至少授予1个角色，并且在 OpenShift 创建对应用户
     * @param oauthUser OAuth2 用户
     * @return OAuthUser
     */
    OAuthUser createUser(OAuthUser oauthUser) throws ServiceException;

    boolean updateUserRoles(long userId, List<Long> addRoleIdList, List<Long> delRoleIdList) throws ServiceException;
}
