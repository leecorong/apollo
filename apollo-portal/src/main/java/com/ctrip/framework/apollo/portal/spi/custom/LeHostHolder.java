package com.ctrip.framework.apollo.portal.spi.custom;

import com.ctrip.framework.apollo.portal.entity.bo.UserInfo;
import org.springframework.stereotype.Component;

/**
 * 线程本地变量，保存用户相关信息
 *
 * @author evilhex.
 * @date 2018/9/4 上午10:25.
 */
public class LeHostHolder {

    private static ThreadLocal<UserInfo> users = new ThreadLocal<UserInfo>();

    public static UserInfo getUser() {
        return users.get();
    }

    public  static void setUser(UserInfo user) {
        users.set(user);
    }

    public  static void clear() {
        users.remove();
    }
}
