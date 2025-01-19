package com.adlternative.tinyhacknews.models.output;

import com.adlternative.tinyhacknews.models.UserInfo;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/** 普通用户信息，不包含邮箱 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SimpleUserInfoOutputDTO {
  Long id;

  String name;

  LocalDateTime createdAt;

  String about;

  // 用户的分数，被点赞越多分数越高，默认 0
  Long karma = 0L;

  public static SimpleUserInfoOutputDTO from(UserInfo userInfo) {
    return SimpleUserInfoOutputDTO.builder()
        .id(userInfo.getId())
        .name(userInfo.getName())
        .createdAt(userInfo.getCreatedAt())
        .about(userInfo.getAbout())
        .karma(0L)
        .build();
  }
}
