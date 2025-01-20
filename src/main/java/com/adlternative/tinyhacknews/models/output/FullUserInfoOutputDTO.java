package com.adlternative.tinyhacknews.models.output;

import com.adlternative.tinyhacknews.models.UserInfo;
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
public class FullUserInfoOutputDTO extends UserInfoOutputDTO {
  String email;

  public static FullUserInfoOutputDTO from(UserInfo userInfo) {
    return FullUserInfoOutputDTO.builder()
        .id(userInfo.getId())
        .name(userInfo.getName())
        .createdAt(userInfo.getCreatedAt())
        .about(userInfo.getAbout())
        .karma(0L)
        .email(userInfo.getEmail())
        .build();
  }
}
