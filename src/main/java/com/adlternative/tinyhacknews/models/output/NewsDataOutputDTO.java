package com.adlternative.tinyhacknews.models.output;

import com.adlternative.tinyhacknews.entity.News;
import com.adlternative.tinyhacknews.entity.Users;
import com.adlternative.tinyhacknews.models.NewsInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class NewsDataOutputDTO extends NewsInfo {
  private String title;
  private String url;
  private String text;
  private Long commentsCount;
  private Long pointsCount;
  // 当前用户是否已投票
  private Boolean hasVote;

  public static NewsDataOutputDTO from(
      News news, Users author, Long commentCount, Long pointCount, Boolean hasVote) {
    return NewsDataOutputDTO.builder()
        .id(news.getId())
        .title(news.getTitle())
        .text(news.getText())
        .url(news.getUrl())
        .createdAt(news.getCreatedAt())
        .updatedAt(news.getUpdatedAt())
        .author(SimpleUserInfoOutputDTO.from(author))
        .commentsCount(commentCount)
        .pointsCount(pointCount)
        .hasVote(hasVote)
        .build();
  }
}
