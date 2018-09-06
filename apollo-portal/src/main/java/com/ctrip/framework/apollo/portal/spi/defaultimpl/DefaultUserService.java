package com.ctrip.framework.apollo.portal.spi.defaultimpl;

import com.ctrip.framework.apollo.portal.entity.po.UserPO;
import com.ctrip.framework.apollo.portal.repository.UserRepository;
import com.google.common.collect.Lists;

import com.ctrip.framework.apollo.portal.entity.bo.UserInfo;
import com.ctrip.framework.apollo.portal.spi.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class DefaultUserService implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserInfo> searchUsers(String keyword, int offset, int limit) {
        List<UserInfo> userInfos = new LinkedList<>();
        List<UserPO> userPOS = userRepository.findByUsernameLikeAndEnabled(keyword, 1);
        for (UserPO userPO : userPOS) {
            userInfos.add(userPO.toUserInfo());
        }
        return userInfos;
    }

    @Override
    public UserInfo findByUserId(String userId) {
        if (userId.equals("apollo")) {
            return assembleDefaultUser();
        } else {
            return userRepository.findByUsername(userId).toUserInfo();
        }
    }

    @Override
    public List<UserInfo> findByUserIds(List<String> userIds) {
        if (userIds.contains("apollo")) {
            return Lists.newArrayList(assembleDefaultUser());
        } else {
            List<UserInfo> userInfos = new LinkedList<>();
            List<UserPO> userPOS = userRepository.findByUsernameIn(userIds);
            for (UserPO userPO : userPOS) {
                userInfos.add(userPO.toUserInfo());
            }
            return userInfos;
        }
    }

    private UserInfo assembleDefaultUser() {
        UserInfo defaultUser = new UserInfo();
        defaultUser.setUserId("apollo");
        defaultUser.setName("apollo");
        defaultUser.setEmail("apollo@acme.com");

        return defaultUser;
    }
}
