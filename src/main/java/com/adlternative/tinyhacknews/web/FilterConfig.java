package com.adlternative.tinyhacknews.web;

import com.adlternative.tinyhacknews.service.UserService;
import com.adlternative.tinyhacknews.web.filter.AuthFilter;
import javax.annotation.Resource;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class FilterConfig {
  @Resource private UserService userService;

  @Resource private HandlerExceptionResolver handlerExceptionResolver;

  @Bean
  public FilterRegistrationBean<AuthFilter> filterRegistrationAuthBean() {
    FilterRegistrationBean<AuthFilter> bean = new FilterRegistrationBean<>();
    bean.setFilter(new AuthFilter(userService, handlerExceptionResolver));
    bean.addUrlPatterns("/*");
    return bean;
  }
}
