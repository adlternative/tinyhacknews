package com.adlternative.tinyhacknews.models.output;

import com.adlternative.tinyhacknews.entity.News;
import com.adlternative.tinyhacknews.entity.Users;
import com.adlternative.tinyhacknews.models.NewsInfo;
import com.adlternative.tinyhacknews.models.UserInfo;
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

  public static NewsDataOutputDTO convertFromNews(News news, Users author) {
    return NewsDataOutputDTO.builder()
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
