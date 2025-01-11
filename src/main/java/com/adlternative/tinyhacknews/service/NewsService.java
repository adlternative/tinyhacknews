package com.adlternative.tinyhacknews.service;

import com.adlternative.tinyhacknews.entity.ListAllNewsOrderEnum;
import com.adlternative.tinyhacknews.entity.NewsData;
import com.adlternative.tinyhacknews.entity.SubmitNewsInputDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface NewsService {

  /**
   * 提交一条新闻
   *
   * @param userId
   * @param submitNewsInputDTO
   * @return
   */
  NewsData submit(SubmitNewsInputDTO submitNewsInputDTO);

  /**
   * 删除一条新闻
   *
   * @param id
   */
  void deleteNews(Long id);

  /**
   * 获取一条新闻
   *
   * @param id
   * @return
   */
  NewsData getNews(Long id);

  /**
   * 修改一条新闻
   *
   * @param id
   * @param userId
   * @param submitNewsInputDTO
   * @return
   */
  NewsData changeNews(Long id, SubmitNewsInputDTO submitNewsInputDTO);

  /**
   * 获取用户所有新闻
   *
   * @param userId
   * @return
   */
  IPage<NewsData> getAllNewsOfUser(Long userId, Long pageNum, Long pageSize);

  /**
   * 获取所有新闻
   *
   * @return
   */
  IPage<NewsData> getAllNews(Long pageNum, Long pageSize, ListAllNewsOrderEnum order);
}
