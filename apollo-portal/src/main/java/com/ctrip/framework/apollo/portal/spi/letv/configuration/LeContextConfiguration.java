package com.ctrip.framework.apollo.portal.spi.letv.configuration;

import com.ctrip.framework.apollo.portal.spi.letv.filters.UserAccessFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 注册自定义的filter
 *
 * @author evilhex.
 * @date 2018/9/6 下午4:29.
 */
@Configuration
@Profile("letv")
public class LeContextConfiguration {
    @Bean
    public FilterRegistrationBean userAccessFilter() {
        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new UserAccessFilter());
        filter.addUrlPatterns("/*");
        return filter;
    }
}
