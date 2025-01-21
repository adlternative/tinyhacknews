package com.adlternative.tinyhacknews.service;

import com.adlternative.tinyhacknews.models.enums.ListAllNewsOrderEnum;
import com.adlternative.tinyhacknews.models.enums.NewsTypeEnum;
import com.adlternative.tinyhacknews.models.input.SubmitNewsInputDTO;
import com.adlternative.tinyhacknews.models.output.NewsDataOutputDTO;
import com.adlternative.tinyhacknews.models.output.NewsMetaOutputDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface NewsService {

  /**
   * 提交一条新闻
   *
   * @param submitNewsInputDTO
   * @return
   */
  NewsDataOutputDTO submit(SubmitNewsInputDTO submitNewsInputDTO);

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
  NewsDataOutputDTO getNews(Long id);

  /**
   * 修改一条新闻
   *
   * @param id
   * @param submitNewsInputDTO
   * @return
   */
  NewsDataOutputDTO changeNews(Long id, SubmitNewsInputDTO submitNewsInputDTO);

  /**
   * 获取用户所有新闻
   *
   * @param userId
   * @return
   */
  IPage<NewsDataOutputDTO> getAllNewsOfUser(Long userId, Long pageNum, Long pageSize);

  /**
   * 获取所有新闻
   *
   * @return
   */
  IPage<NewsMetaOutputDTO> getAllNews(
      Long pageNum, Long pageSize, ListAllNewsOrderEnum order, NewsTypeEnum type, String date);
}
