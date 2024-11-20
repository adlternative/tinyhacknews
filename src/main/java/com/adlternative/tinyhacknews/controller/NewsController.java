package com.adlternative.tinyhacknews.controller;

import com.adlternative.tinyhacknews.entity.CommentData;
import com.adlternative.tinyhacknews.entity.NewsData;
import com.adlternative.tinyhacknews.entity.SubmitNewsInputDTO;
import com.adlternative.tinyhacknews.service.CommentService;
import com.adlternative.tinyhacknews.service.NewsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

  /**
   * 提交新闻
   *
   * @param userId
   * @param submitNewsInputDTO
   * @return
   */
  @PostMapping
  public NewsData submitNews(
      @RequestParam(name = "user_id") Long userId,
      @RequestBody SubmitNewsInputDTO submitNewsInputDTO) {
    return newsService.submit(userId, submitNewsInputDTO);
  }

  /**
   * 修改新闻内容
   *
   * @param id
   * @param userId
   * @param submitNewsInputDTO
   * @return
   */
  @PutMapping("/{id}")
  public NewsData changeNews(
      @PathVariable(name = "id") Long id,
      @RequestParam(name = "user_id") Long userId,
      @RequestBody SubmitNewsInputDTO submitNewsInputDTO) {
    return newsService.changeNews(id, userId, submitNewsInputDTO);
  }

  /**
   * 查看某条新闻内容
   *
   * @param id
   * @param userId
   * @return
   */
  @GetMapping("/{id}")
  public NewsData getNews(
      @PathVariable(name = "id") Long id, @RequestParam(name = "user_id") Long userId) {
    return newsService.getNews(id, userId);
  }

  /**
   * 删除新闻
   *
   * @param id
   * @param userId
   */
  @DeleteMapping("/{id}")
  public void deleteNews(
      @PathVariable(name = "id") Long id, @RequestParam(name = "user_id") Long userId) {
    newsService.deleteNews(id, userId);
  }

  /**
   * 获取某条新闻的所有评论
   *
   * @param newsId
   * @param userId
   * @return
   */
  @GetMapping("/{id}/comments")
  public List<CommentData> getComments(
      @PathVariable(name = "id") Long newsId, @RequestParam(name = "user_id") Long userId) {
    // TODO: 添加分页参数
    return commentService.getComments(newsId, userId);
  }

  private final NewsService newsService;
  private final CommentService commentService;
}
