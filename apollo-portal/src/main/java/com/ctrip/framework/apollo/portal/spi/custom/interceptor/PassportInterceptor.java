package com.ctrip.framework.apollo.portal.spi.custom.interceptor;

import com.ctrip.framework.apollo.portal.entity.bo.UserInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.ctrip.framework.apollo.portal.spi.custom.LeHostHolder.clear;
import static com.ctrip.framework.apollo.portal.spi.custom.LeHostHolder.setUser;

/**
 * @author evilhex.
 * @date 2018/9/4 下午2:59.
 */
@Component
public class PassportInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String ticket = null;
        if (httpServletRequest.getCookies() != null) {
            for (Cookie cookie : httpServletRequest.getCookies()) {
                if (cookie.getName().equals("tk")) {
                    ticket = cookie.getValue();
                }
            }
        }
        if (ticket != null && ticket.contains("magic")) {
            //hostHolder中保存用户信息
            UserInfo userInfo = new UserInfo(ticket.substring(20));
            setUser(userInfo);
        } else {
            httpServletResponse.sendRedirect("/signin");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView)
            throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e)
            throws Exception {
    }

}
