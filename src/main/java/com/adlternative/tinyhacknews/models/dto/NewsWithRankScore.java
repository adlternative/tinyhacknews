package com.adlternative.tinyhacknews.models.dto;

import com.adlternative.tinyhacknews.entity.News;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class NewsWithRankScore implements Comparable<NewsWithRankScore> {
  News news;
  Double rankScore;

  @Override
  public int compareTo(NewsWithRankScore other) {
    // 如果news.id相同，认为是同一个对象
    if (this.news.getId().equals(other.news.getId())) {
      return 0;
    }

    // 优先比较rankScore（降序）
    int rankCompare = other.rankScore.compareTo(this.rankScore);
    if (rankCompare != 0) {
      return rankCompare;
    }

    int timeCompare = other.news.getCreatedAt().compareTo(this.news.getCreatedAt());
    if (timeCompare != 0) {
      return timeCompare;
    }
    // 最后比较news.id以确保不同id的元素不会被认为相同
    return other.news.getId().compareTo(this.news.getId());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NewsWithRankScore that = (NewsWithRankScore) o;

    return Objects.equals(news.getId(), that.news.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(news.getId());
  }
}
