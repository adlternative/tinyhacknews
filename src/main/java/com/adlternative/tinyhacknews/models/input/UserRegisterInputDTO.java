package com.adlternative.tinyhacknews.models.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 用户注册参数 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRegisterInputDTO {
  String name;

  String password;

  String email;
}
