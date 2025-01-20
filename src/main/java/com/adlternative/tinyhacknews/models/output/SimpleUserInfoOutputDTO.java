package com.adlternative.tinyhacknews.models.output;

import com.adlternative.tinyhacknews.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleUserInfoOutputDTO {
  Long id;

  String name;

  public static SimpleUserInfoOutputDTO from(Users users) {
    return SimpleUserInfoOutputDTO.builder().id(users.getId()).name(users.getUsername()).build();
  }
}
