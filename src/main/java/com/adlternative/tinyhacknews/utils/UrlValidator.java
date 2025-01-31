package com.adlternative.tinyhacknews.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlValidator {
  public static boolean isValidHttpUrl(String url) {
    if (url.isEmpty()) {
      return false;
    }

    try {

      // 创建URL对象
      URL myUrl = new URL(url);

      // 验证协议是否为http或者https
      return "http".equals(myUrl.getProtocol()) || "https".equals(myUrl.getProtocol());
    } catch (MalformedURLException e) {
      // 如果创建URL对象失败，则说明url格式不正确
      return false;
    }
  }
}
