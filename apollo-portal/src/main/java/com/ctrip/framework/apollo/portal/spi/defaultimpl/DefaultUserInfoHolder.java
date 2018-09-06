package com.ctrip.framework.apollo.portal.spi.defaultimpl;

import com.ctrip.framework.apollo.portal.spi.UserInfoHolder;
import com.ctrip.framework.apollo.portal.entity.bo.UserInfo;
import com.ctrip.framework.apollo.portal.spi.custom.LeHostHolder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 不是ctrip的公司默认提供一个假用户
 */
public class DefaultUserInfoHolder implements UserInfoHolder {

    public DefaultUserInfoHolder() {

    }

    @Override
    public UserInfo getUser() {
        return LeHostHolder.getUser();
    }
}
