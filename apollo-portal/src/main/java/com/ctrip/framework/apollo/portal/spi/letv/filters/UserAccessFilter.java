package com.ctrip.framework.apollo.portal.spi.letv.filters;

import com.ctrip.framework.apollo.portal.entity.bo.UserInfo;
import com.ctrip.framework.apollo.tracer.Tracer;
import com.google.common.base.Strings;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ctrip.framework.apollo.portal.spi.letv.LeHostHolder.setUser;

public class UserAccessFilter implements Filter {

    private static final String STATIC_RESOURCE_REGEX = ".*\\.(js|htm|png|css|woff2)$";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String requestUri = ((HttpServletRequest) request).getRequestURI();
        try {
            if (!isOpenAPIRequest(requestUri) && !isStaticResource(requestUri) && !isToLogin(requestUri)) {
                //拦截非静态资源
                String ticket = null;
                if (((HttpServletRequest) request).getCookies() != null) {
                    for (Cookie cookie : ((HttpServletRequest) request).getCookies()) {
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
                    ((HttpServletResponse) response).sendRedirect("/signin");
                    return;
                }
            }
        } catch (Throwable e) {
            Tracer.logError("Record user access info error.", e);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    private boolean isOpenAPIRequest(String uri) {
        return !Strings.isNullOrEmpty(uri) && uri.startsWith("/openapi");
    }

    private boolean isStaticResource(String uri) {
        return !Strings.isNullOrEmpty(uri) && uri.matches(STATIC_RESOURCE_REGEX);
    }

    private boolean isToLogin(String uri) {
        return uri.contains("signin") || uri.contains("logon") || uri.contains("login");
    }

}
