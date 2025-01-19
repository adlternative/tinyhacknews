package com.adlternative.tinyhacknews.models.output;

import com.adlternative.tinyhacknews.entity.Comments;
import com.adlternative.tinyhacknews.entity.Users;
import com.adlternative.tinyhacknews.models.UserInfo;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentOutPutDTO {
  private Long id;
  private UserInfo author;
  private String text;

  private Long newsId;

  private Long parentCommentId;

  private Date createdAt;

  private Date updatedAt;

  public static CommentOutPutDTO convertFrom(Comments comment, Users user) {
    return CommentOutPutDTO.builder()
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
