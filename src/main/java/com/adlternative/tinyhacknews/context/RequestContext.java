package com.adlternative.tinyhacknews.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestContext {
  public static void setUserId(Long userId) {
    REQUEST_THREAD_LOCAL.get().put(CUR_USER_KEY, userId);
  }

  public static Long getUserId() {
    return (Long) REQUEST_THREAD_LOCAL.get().get(CUR_USER_KEY);
  }

  public static void set(String key, Object value) {
    REQUEST_THREAD_LOCAL.get().put(key, value);
  }

  public static Object get(String key) {
    return REQUEST_THREAD_LOCAL.get().get(key);
  }

  public static <T> T get(String key, Class<T> clazz) {
    Object value = REQUEST_THREAD_LOCAL.get().get(key);
    return clazz.cast(value);
  }

  public static void remove() {
    REQUEST_THREAD_LOCAL.remove();
  }

  private static final ThreadLocal<Map<String, Object>> REQUEST_THREAD_LOCAL =
      ThreadLocal.withInitial(ConcurrentHashMap::new);

  private static final String CUR_USER_KEY = "CUR_USER";
}
