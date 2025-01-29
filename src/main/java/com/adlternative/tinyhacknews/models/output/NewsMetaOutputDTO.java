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
public class NewsMetaOutputDTO extends NewsInfo {
  private String title;
  private String url;
  private String newsType;

  public static NewsMetaOutputDTO from(News news, Users author) {
    return NewsMetaOutputDTO.builder()
        .id(news.getId())
        .title(news.getTitle())
        .url(news.getUrl())
        .createdAt(news.getCreatedAt())
        .updatedAt(news.getUpdatedAt())
        .author(SimpleUserInfoOutputDTO.from(author))
        .newsType(news.getNewsType())
        .build();
  }
}
