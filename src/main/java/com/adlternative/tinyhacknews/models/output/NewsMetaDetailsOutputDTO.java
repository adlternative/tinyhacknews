package com.adlternative.tinyhacknews.models.output;

import com.adlternative.tinyhacknews.entity.News;
import com.adlternative.tinyhacknews.entity.Users;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/** 更加详细的新闻元数据，包含评论数和点赞数 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class NewsMetaDetailsOutputDTO extends NewsMetaOutputDTO {
  // 评论数
  private Long commentsCount;
  // 点赞数
  private Long pointsCount;
  // 当前用户是否已投票
  private Boolean hasVote;

  public static NewsMetaDetailsOutputDTO from(
      News news, Users author, Long commentCount, Long pointCount, Boolean hasVote) {
    return NewsMetaDetailsOutputDTO.builder()
        .id(news.getId())
        .title(news.getTitle())
        .url(news.getUrl())
        .newsType(news.getNewsType())
        .createdAt(news.getCreatedAt())
        .updatedAt(news.getUpdatedAt())
        .author(SimpleUserInfoOutputDTO.from(author))
        .commentsCount(commentCount)
        .pointsCount(pointCount)
        .hasVote(hasVote)
        .build();
  }
}
