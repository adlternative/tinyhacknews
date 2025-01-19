package com.adlternative.tinyhacknews.models.input;

import lombok.Data;

@Data
public class UpdateUserInfoInputDTO {
  private String name;
  private String email;
  private String about;
}
