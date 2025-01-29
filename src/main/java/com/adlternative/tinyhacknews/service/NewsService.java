package com.adlternative.tinyhacknews.service;

import com.adlternative.tinyhacknews.models.enums.ListAllNewsOrderEnum;
import com.adlternative.tinyhacknews.models.enums.NewsTypeEnum;
import com.adlternative.tinyhacknews.models.input.SubmitNewsInputDTO;
import com.adlternative.tinyhacknews.models.output.NewsDataOutputDTO;
import com.adlternative.tinyhacknews.models.output.NewsMetaDetailsOutputDTO;
import com.adlternative.tinyhacknews.models.pages.PageOutputDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

public interface NewsService {

  /**
   * 提交一条新闻
   *
   * @param submitNewsInputDTO
   * @return
   */
  NewsDataOutputDTO submit(SubmitNewsInputDTO submitNewsInputDTO);

  //  getNewsScore S= P /(T + 2)^1.8
  double getNewsScore(Long newsId);

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
  PageOutputDTO<NewsMetaDetailsOutputDTO> getAllNews(
      Long pageNum, Long pageSize, ListAllNewsOrderEnum order, NewsTypeEnum type, String date);

  /**
   * 为某个新闻投票
   *
   * @param newsId
   */
  void vote(Long newsId);

  void unvote(Long newsId);

  /**
   * 获取新闻票数
   *
   * @param newsId
   * @return
   */
  Long getVoteCount(Long newsId);

  /**
   * 获取按照热点分数排序的新闻
   *
   * @param offset
   * @param limit
   * @return
   */
  List<NewsMetaDetailsOutputDTO> getTopNews(Long offset, Long limit);

  /** 重新计算新闻排名分数 */
  void reCalculateNewsRankScore();
}
