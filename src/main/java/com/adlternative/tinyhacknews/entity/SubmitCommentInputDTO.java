package com.adlternative.tinyhacknews.entity;

import lombok.Data;

@Data
public class SubmitCommentInputDTO {
  private String text;
  private Long newsId;
  private Long parentCommentId;
}
