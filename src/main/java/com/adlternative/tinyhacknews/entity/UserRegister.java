package com.adlternative.tinyhacknews.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 用户注册参数 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRegister {
  @JSONField(name = "name")
  String name;

  @JSONField(name = "password")
  String password;

  @JSONField(name = "email")
  String email;
}
