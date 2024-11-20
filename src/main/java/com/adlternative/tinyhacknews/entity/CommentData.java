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

  public static CommentData convertFrom(Comment comment, User user) {
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
