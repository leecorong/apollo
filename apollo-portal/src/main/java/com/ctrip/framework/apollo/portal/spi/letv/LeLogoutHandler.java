package com.ctrip.framework.apollo.portal.spi.letv;

import com.ctrip.framework.apollo.portal.spi.LogoutHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出操作
 *
 * @author evilhex.
 * @date 2018/9/6 下午6:50.
 */
public class LeLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            //清除cookie
            for (Cookie cookie : request.getCookies()) {
                Cookie cook = new Cookie(cookie.getName(), null);
                cook.setPath("/");
                cook.setMaxAge(0);
                response.addCookie(cook);
            }
            response.sendRedirect("/signin");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
