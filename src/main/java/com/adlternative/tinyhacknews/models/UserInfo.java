package com.adlternative.tinyhacknews.models;

import com.adlternative.tinyhacknews.entity.Users;
import java.time.LocalDateTime;
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
  Long id;

  String name;

  String email;

  LocalDateTime createdAt;

  String about;

  public UserInfo(Users user) {
    id = user.getId();
    name = user.getUsername();
    email = user.getEmail();
    createdAt = user.getCreatedAt();
    about = user.getAbout();
  }

  public static UserInfo convertFrom(Users user) {
    return UserInfo.builder()
        .id(user.getId())
        .name(user.getUsername())
        .email(user.getEmail())
        .createdAt(user.getCreatedAt())
        .about(user.getAbout())
        .build();
  }
}
