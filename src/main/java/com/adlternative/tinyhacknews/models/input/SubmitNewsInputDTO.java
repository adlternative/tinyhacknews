package com.adlternative.tinyhacknews.models.input;

import lombok.Data;

@Data
public class SubmitNewsInputDTO {
  String title;
  String url;
  String text;
}
