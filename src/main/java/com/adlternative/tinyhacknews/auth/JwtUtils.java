package com.adlternative.tinyhacknews.auth;

import com.adlternative.tinyhacknews.exception.UnauthorizedException;
import com.adlternative.tinyhacknews.utils.PropertyUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JwtUtils {
  public static final String USER_NAME_CLAIM = "user_name";

  private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds
  // 生成JWT
  public static String generateToken(Long userId, String username) {
    return Jwts.builder()
        .setSubject(String.valueOf(userId))
        .claim(USER_NAME_CLAIM, username)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 10 hours
        .signWith(SignatureAlgorithm.HS256, PropertyUtils.getJwtSecretKey())
        .compact();
  }

  // 验证JWT并获取声明
  public static Claims extractClaims(String token) {
    try {
      return Jwts.parser()
          .setSigningKey(PropertyUtils.getJwtSecretKey())
          .parseClaimsJws(token)
          .getBody();
    } catch (Exception e) {
      throw new UnauthorizedException("Token无效");
    }
  }

  // 从JWT中提取用户名
  public static String extractUserName(String token) {
    return extractClaims(token).get(USER_NAME_CLAIM).toString();
  }

  public static Long extractUserId(String token) {
    return Long.valueOf(extractClaims(token).getSubject());
  }

  // 检查JWT是否过期
  public static boolean isTokenExpired(String token) {
    return extractClaims(token).getExpiration().before(new Date());
  }

  public static boolean validateToken(String token, String username) {
    String tokenUsername = extractClaims(token).getSubject();
    return (username.equals(tokenUsername) && !isTokenExpired(token));
  }
}
