package com.adlternative.tinyhacknews.models.dto;

import com.adlternative.tinyhacknews.entity.News;
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

  // 注意是倒序
  @Override
  public int compareTo(NewsWithRankScore other) {
    int scoreComparison = this.rankScore.compareTo(other.rankScore);
    if (scoreComparison != 0) {
      return -scoreComparison;
    } else {
      return -this.news.getCreatedAt().compareTo(other.news.getCreatedAt());
    }
  }
}
