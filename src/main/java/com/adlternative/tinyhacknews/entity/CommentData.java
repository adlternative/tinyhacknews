package com.adlternative.tinyhacknews.entity;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentData {
  private Long id;
  private UserInfo author;
  private String text;

  private Long newsId;

  private Long parentCommentId;

  private Date createdAt;

  private Date updatedAt;
}
