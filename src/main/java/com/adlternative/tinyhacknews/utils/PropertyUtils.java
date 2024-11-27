package com.adlternative.tinyhacknews.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyUtils {

  private final String jwtSecretKey;

  private static PropertyUtils propertyUtils;

  private PropertyUtils(@Value("${jwt.secret-key}") String jwtSecretKey) {
    propertyUtils = this;
    this.jwtSecretKey = jwtSecretKey;
  }

  public static String getJwtSecretKey() {
    return propertyUtils.jwtSecretKey;
  }
}
