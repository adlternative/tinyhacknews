package com.adlternative.tinyhacknews.service;

import com.adlternative.tinyhacknews.entity.NewsData;
import com.adlternative.tinyhacknews.entity.SubmitNewsInputDTO;

public interface NewsService {

  NewsData submit(Long userId, SubmitNewsInputDTO submitNewsInputDTO);

  /**
   * 删除一条新闻
   *
   * @param id
   * @param userId
   */
  void deleteNews(Long id, Long userId);

  NewsData getNews(Long id, Long userId);

  NewsData changeNews(Long id, Long userId, SubmitNewsInputDTO submitNewsInputDTO);
}
