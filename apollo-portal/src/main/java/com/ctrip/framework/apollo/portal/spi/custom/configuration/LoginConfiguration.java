package com.ctrip.framework.apollo.portal.spi.custom.configuration;

import com.ctrip.framework.apollo.portal.spi.custom.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 注册拦截器
 *
 * @author evilhex.
 * @date 2018/9/4 上午10:32.
 */
@Configuration
public class LoginConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private PassportInterceptor passportInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor).addPathPatterns("/").addPathPatterns("/user/**").addPathPatterns("/permissions/**");
        super.addInterceptors(registry);
    }
}
