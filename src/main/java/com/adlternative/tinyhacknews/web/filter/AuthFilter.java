package com.adlternative.tinyhacknews.web.filter;

import com.adlternative.tinyhacknews.auth.JwtUtils;
import com.adlternative.tinyhacknews.context.RequestContext;
import com.adlternative.tinyhacknews.entity.UserInfo;
import com.adlternative.tinyhacknews.exception.UnauthorizedException;
import com.adlternative.tinyhacknews.service.UserService;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@AllArgsConstructor
public class AuthFilter implements Filter {
  private UserService userService;

  private HandlerExceptionResolver handlerExceptionResolver;

  private static final List<String> WHITE_LIST_URLS =
      Arrays.asList("/api/v1/users/login", "/api/v1/users");

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    // TODO: 使用 spring security 重构
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    if (Objects.equals(httpRequest.getMethod(), HttpMethod.OPTIONS.name())
        || isWhiteListUrl(httpRequest)) {
      chain.doFilter(request, response);
      return;
    }

    try {
      boolean authFlag = false;

      // 从请求中的Cookies获取JWT
      Cookie[] cookies = httpRequest.getCookies();
      if (cookies == null || cookies.length == 0) {
        throw new UnauthorizedException("缺少用户信息");
      }

      log.debug("cookies: {}", Arrays.toString(cookies));

      for (Cookie cookie : cookies) {
        if ("jwt".equals(cookie.getName())) {
          // 验证和解析JWT
          String jwtToken = cookie.getValue();
          if (jwtToken == null) {
            throw new UnauthorizedException("缺少用户信息");
          }
          Claims claims = JwtUtils.extractClaims(jwtToken);
          if (claims.getExpiration().before(new Date())) {
            throw new UnauthorizedException("Token已过期");
          }

          // 从JWT的Claims中获取用户ID和用户名
          Long userId = Long.valueOf(claims.getSubject());
          String username = (String) claims.get(JwtUtils.USER_NAME_CLAIM);

          UserInfo userInfo = userService.getSingleUserInfo(userId);
          if (!Objects.equals(userInfo.getName(), username)) {
            log.error("用户信息不匹配, userInfo: {}, username: {}", userInfo, username);
            throw new UnauthorizedException("用户信息不匹配");
          }

          authFlag = true;
          RequestContext.setUserId(userId);
          break;
        }
      }
      if (!authFlag) {
        throw new UnauthorizedException("缺少用户信息");
      }
    } catch (Exception e) {
      // 处理JWT解析异常
      log.error("Auth error", e);
      handlerExceptionResolver.resolveException(httpRequest, httpResponse, null, e);
      return;
    }
    // 继续过滤器链
    chain.doFilter(request, response);
  }

  private boolean isWhiteListUrl(HttpServletRequest request) {
    String requestURI = request.getRequestURI();
    for (String url : WHITE_LIST_URLS) {
      // 后面应该改造为权限注解
      if ((Objects.equals(request.getMethod(), HttpMethod.POST.name())) && requestURI.equals(url)) {
        return true;
      }
    }
    log.info("requestURI: {}, request.getMethod(): {}", requestURI, request.getMethod());
    return false;
  }

  @Override
  public void destroy() {
    // 清理资源
  }
}
