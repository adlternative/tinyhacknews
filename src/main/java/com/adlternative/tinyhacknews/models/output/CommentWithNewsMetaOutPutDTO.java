package com.adlternative.tinyhacknews.models.output;

import com.adlternative.tinyhacknews.entity.Comments;
import com.adlternative.tinyhacknews.entity.News;
import com.adlternative.tinyhacknews.entity.Users;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class CommentWithNewsMetaOutPutDTO extends CommentOutPutDTO {
  NewsMetaOutputDTO newsMeta;

  public static CommentWithNewsMetaOutPutDTO from(Comments comment, News news, Users user) {
    return CommentWithNewsMetaOutPutDTO.builder()
        .id(comment.getId())
        .author(SimpleUserInfoOutputDTO.from(user))
        .createdAt(comment.getCreatedAt())
        .updatedAt(comment.getUpdatedAt())
        .text(comment.getText())
        .parentCommentId(comment.getParentCommentId())
        .newsId(comment.getNewsId())
        .newsMeta(NewsMetaOutputDTO.from(news, user))
        .build();
  }
}
