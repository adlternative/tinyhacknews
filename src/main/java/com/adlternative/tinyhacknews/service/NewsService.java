package com.adlternative.tinyhacknews.service;

import com.adlternative.tinyhacknews.entity.NewsInfo;
import com.adlternative.tinyhacknews.entity.SubmitNewsInputDTO;

public interface NewsService {

  NewsInfo submit(Long userId, SubmitNewsInputDTO submitNewsInputDTO);

  /**
   * 删除一条新闻
   *
   * @param id
   * @param userId
   */
  void deleteNews(Long id, Long userId);

  NewsInfo getNews(Long id, Long userId);

  NewsInfo changeNews(Long id, Long userId, SubmitNewsInputDTO submitNewsInputDTO);
}
