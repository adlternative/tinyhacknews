package com.adlternative.tinyhacknews.controller;

import com.adlternative.tinyhacknews.entity.CommentData;
import com.adlternative.tinyhacknews.entity.NewsData;
import com.adlternative.tinyhacknews.entity.SubmitNewsInputDTO;
import com.adlternative.tinyhacknews.service.CommentService;
import com.adlternative.tinyhacknews.service.NewsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
   * @param submitNewsInputDTO
   * @return
   */
  @PostMapping
  public NewsData submitNews(@RequestBody SubmitNewsInputDTO submitNewsInputDTO) {
    return newsService.submit(submitNewsInputDTO);
  }

  /**
   * 修改新闻内容
   *
   * @param id
   * @param submitNewsInputDTO
   * @return
   */
  @PutMapping("/{id}")
  public NewsData changeNews(
      @PathVariable(name = "id") Long id, @RequestBody SubmitNewsInputDTO submitNewsInputDTO) {
    return newsService.changeNews(id, submitNewsInputDTO);
  }

  /**
   * 查看某条新闻内容
   *
   * @param id
   * @return
   */
  @GetMapping("/{id}")
  public NewsData getNews(@PathVariable(name = "id") Long id) {
    return newsService.getNews(id);
  }

  /**
   * 删除新闻
   *
   * @param id
   */
  @DeleteMapping("/{id}")
  public void deleteNews(@PathVariable(name = "id") Long id) {
    newsService.deleteNews(id);
  }

  /**
   * 获取某条新闻的所有评论
   *
   * @param newsId
   * @return
   */
  @GetMapping("/{id}/comments")
  public IPage<CommentData> getComments(
      @PathVariable(name = "id") Long newsId,
      @RequestParam(name = "page_num", defaultValue = "1") Long pageNum,
      @RequestParam(name = "page_size", defaultValue = "10") Long pageSize) {
    return commentService.getComments(newsId, pageNum, pageSize);
  }

  /**
   * 获取所有新闻
   *
   * @return
   */
  @GetMapping("/all")
  public IPage<NewsData> getAllNews(
      @RequestParam(name = "page_num", defaultValue = "1") Long pageNum,
      @RequestParam(name = "page_size", defaultValue = "10") Long pageSize) {
    return newsService.getAllNews(pageNum, pageSize);
  }

  private final NewsService newsService;
  private final CommentService commentService;
}
