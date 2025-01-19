package com.adlternative.tinyhacknews.entity;

import lombok.Data;

@Data
public class UpdateUserInfoDTO {
  private String name;
  private String email;
  private String about;
}
