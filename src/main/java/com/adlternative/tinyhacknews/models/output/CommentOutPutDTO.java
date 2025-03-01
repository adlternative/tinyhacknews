package com.adlternative.tinyhacknews.models.output;

import com.adlternative.tinyhacknews.entity.Comments;
import com.adlternative.tinyhacknews.entity.Users;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CommentOutPutDTO {
  private Long id;
  private SimpleUserInfoOutputDTO author;
  private String text;

  private Long newsId;

  private Long parentCommentId;

  private Date createdAt;

  private Date updatedAt;

  public static CommentOutPutDTO convertFrom(Comments comment, Users user) {
    return CommentOutPutDTO.builder()
        .id(comment.getId())
        .author(SimpleUserInfoOutputDTO.from(user))
        .createdAt(comment.getCreatedAt())
        .updatedAt(comment.getUpdatedAt())
        .text(comment.getText())
        .parentCommentId(comment.getParentCommentId())
        .newsId(comment.getNewsId())
        .build();
  }
}
