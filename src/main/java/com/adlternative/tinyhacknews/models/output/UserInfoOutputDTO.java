package com.adlternative.tinyhacknews.models.output;

import com.adlternative.tinyhacknews.models.UserInfo;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/** 普通用户信息，不包含邮箱 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserInfoOutputDTO {
  Long id;

  String name;

  Date createdAt;

  String about;

  // 用户的分数，被点赞越多分数越高，默认 0
  Long karma = 0L;

  public static UserInfoOutputDTO from(UserInfo userInfo) {
    return UserInfoOutputDTO.builder()
        .id(userInfo.getId())
        .name(userInfo.getName())
        .createdAt(userInfo.getCreatedAt())
        .about(userInfo.getAbout())
        .karma(0L)
        .build();
  }
}
