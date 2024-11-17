package com.adlternative.tinyhacknews.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 用户信息 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfo {
  @JSONField(name = "id")
  Long id;

  @JSONField(name = "name")
  String name;

  @JSONField(name = "email")
  String email;

  public UserInfo(User user) {
    id = user.getId();
    name = user.getUsername();
    email = user.getEmail();
  }

  public static UserInfo convertFrom(User user) {
    return UserInfo.builder()
        .id(user.getId())
        .name(user.getUsername())
        .email(user.getEmail())
        .build();
  }
}
