package com.ctrip.framework.apollo.portal.spi.letv;

import com.ctrip.framework.apollo.portal.entity.bo.UserInfo;
import com.ctrip.framework.apollo.portal.spi.UserInfoHolder;

/**
 * 获取当前登录的用户信息
 *
 * @author evilhex.
 * @date 2018/9/6 下午6:49.
 */
public class LeUserinfoHolder implements UserInfoHolder {

    @Override
    public UserInfo getUser() {
        return LeHostHolder.getUser();
    }

}
