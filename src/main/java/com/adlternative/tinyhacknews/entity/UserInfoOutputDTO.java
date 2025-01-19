package com.adlternative.tinyhacknews.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserInfoOutputDTO extends SimpleUserInfoOutputDTO {
  String email;

  public static UserInfoOutputDTO from(UserInfo userInfo) {
    return UserInfoOutputDTO.builder()
        .id(userInfo.getId())
        .name(userInfo.getName())
        .createdAt(userInfo.getCreatedAt())
        .about(userInfo.getAbout())
        .karma(0L)
        .email(userInfo.getEmail())
        .build();
  }
}
