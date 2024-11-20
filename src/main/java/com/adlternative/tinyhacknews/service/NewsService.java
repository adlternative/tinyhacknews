package com.adlternative.tinyhacknews.service;

import com.adlternative.tinyhacknews.entity.NewsData;
import com.adlternative.tinyhacknews.entity.SubmitNewsInputDTO;

public interface NewsService {

  /**
   * 提交一条新闻
   *
   * @param userId
   * @param submitNewsInputDTO
   * @return
   */
  NewsData submit(Long userId, SubmitNewsInputDTO submitNewsInputDTO);

  /**
   * 删除一条新闻
   *
   * @param id
   * @param userId
   */
  void deleteNews(Long id, Long userId);

  /**
   * 获取一条新闻
   *
   * @param id
   * @param userId
   * @return
   */
  NewsData getNews(Long id, Long userId);

  /**
   * 修改一条新闻
   *
   * @param id
   * @param userId
   * @param submitNewsInputDTO
   * @return
   */
  NewsData changeNews(Long id, Long userId, SubmitNewsInputDTO submitNewsInputDTO);
}
