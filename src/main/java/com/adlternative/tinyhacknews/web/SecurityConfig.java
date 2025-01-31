package com.adlternative.tinyhacknews.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// @EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // 禁用 CSRF（根据需要）
        .csrf(AbstractHttpConfigurer::disable)

        // 配置授权请求
        .authorizeRequests(
            authz ->
                authz
                    // 允许公共 API 访问
                    .antMatchers("/api/v1/**")
                    .permitAll()
                    // 其他所有请求需要认证
                    .anyRequest()
                    .authenticated())
        // 配置表单登录
        .formLogin(
            form ->
                form.loginPage("/login") // 自定义登录页面
                    .permitAll());

    return http.build();
  }
}
