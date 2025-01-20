package com.adlternative.tinyhacknews.models.input;

import lombok.Data;

@Data
public class SubmitCommentInputDTO {
  private String text;
  private Long parentCommentId;
}
