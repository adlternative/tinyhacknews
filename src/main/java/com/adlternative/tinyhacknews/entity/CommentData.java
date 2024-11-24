package com.adlternative.tinyhacknews.entity;

import java.time.LocalDateTime;
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

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public static CommentData convertFrom(Comments comment, Users user) {
    return CommentData.builder()
        .id(comment.getId())
        .author(UserInfo.convertFrom(user))
        .createdAt(comment.getCreatedAt())
        .updatedAt(comment.getUpdatedAt())
        .text(comment.getText())
        .parentCommentId(comment.getParentCommentId())
        .newsId(comment.getNewsId())
        .build();
  }
}
