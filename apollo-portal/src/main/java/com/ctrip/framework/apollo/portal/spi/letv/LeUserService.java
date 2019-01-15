package com.ctrip.framework.apollo.portal.spi.letv;

import com.ctrip.framework.apollo.core.utils.StringUtils;
import com.ctrip.framework.apollo.portal.entity.bo.UserInfo;
import com.ctrip.framework.apollo.portal.entity.po.UserPO;
import com.ctrip.framework.apollo.portal.repository.UserRepository;
import com.ctrip.framework.apollo.portal.spi.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

/**
 * 获取用户信息操作
 *
 * @author evilhex.
 * @date 2018/9/6 下午6:47.
 */
public class LeUserService implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserInfo> searchUsers(String keyword, int offset, int limit) {
        List<UserInfo> userInfos = new LinkedList<>();
        List<UserPO> userPOS;
        if (StringUtils.isEmpty(keyword)) {
            userPOS = userRepository.findFirst20ByEnabled(1);
        } else {
            userPOS = userRepository.findByUsernameLikeAndEnabled("%" + keyword + "%", 1);
        }
        for (UserPO userPO : userPOS) {
            userInfos.add(userPO.toUserInfo());
        }
        return userInfos;
    }

    @Override
    public UserInfo findByUserId(String userId) {
        return userRepository.findByUsername(userId).toUserInfo();
    }

    @Override
    public List<UserInfo> findByUserIds(List<String> userIds) {
        List<UserInfo> userInfos = new LinkedList<>();
        List<UserPO> userPOS = userRepository.findByUsernameIn(userIds);
        for (UserPO userPO : userPOS) {
            userInfos.add(userPO.toUserInfo());
        }
        return userInfos;
    }
}