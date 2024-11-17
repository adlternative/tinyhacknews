package com.adlternative.tinyhacknews.entity;

import lombok.Data;

@Data
public class SubmitNewsInputDTO {
  String title;
  String url;
  String text;
}
