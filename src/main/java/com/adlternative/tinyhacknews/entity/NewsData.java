package com.adlternative.tinyhacknews.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class NewsData extends NewsInfo {
  private String title;
  private String url;
  private String text;

  public static NewsData convertFromNews(News news, Users author) {
    return NewsData.builder()
        .id(news.getId())
        .title(news.getTitle())
        .text(news.getText())
        .url(news.getUrl())
        .createdAt(news.getCreatedAt())
        .updatedAt(news.getUpdatedAt())
        .author(UserInfo.convertFrom(author))
        .build();
  }
}
