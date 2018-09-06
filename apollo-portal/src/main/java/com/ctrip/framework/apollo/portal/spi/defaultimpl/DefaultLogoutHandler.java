package com.ctrip.framework.apollo.portal.spi.defaultimpl;

import com.ctrip.framework.apollo.portal.spi.LogoutHandler;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultLogoutHandler implements LogoutHandler {

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
