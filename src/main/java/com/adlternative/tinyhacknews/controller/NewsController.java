package com.adlternative.tinyhacknews.controller;

import com.adlternative.tinyhacknews.models.enums.ListAllNewsOrderEnum;
import com.adlternative.tinyhacknews.models.enums.NewsTypeEnum;
import com.adlternative.tinyhacknews.models.input.SubmitCommentInputDTO;
import com.adlternative.tinyhacknews.models.input.SubmitNewsInputDTO;
import com.adlternative.tinyhacknews.models.output.CommentOutPutDTO;
import com.adlternative.tinyhacknews.models.output.NewsDataOutputDTO;
import com.adlternative.tinyhacknews.models.output.NewsMetaDetailsOutputDTO;
import com.adlternative.tinyhacknews.service.CommentService;
import com.adlternative.tinyhacknews.service.NewsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

  @GetMapping("/top")
  public List<NewsMetaDetailsOutputDTO> getTopNews(
      @RequestParam(required = false, defaultValue = "0") Long offset,
      @RequestParam(required = false, defaultValue = "30") Long limit) {
    return newsService.getTopNews(offset, limit);
  }

  /**
   * 提交新闻
   *
   * @param submitNewsInputDTO
   * @return
   */
  @PostMapping
  public NewsDataOutputDTO submitNews(@RequestBody SubmitNewsInputDTO submitNewsInputDTO) {
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
  public NewsDataOutputDTO changeNews(
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
  public NewsDataOutputDTO getNews(@PathVariable(name = "id") Long id) {
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
  public IPage<CommentOutPutDTO> getComments(
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
  public IPage<NewsMetaDetailsOutputDTO> getAllNews(
      @RequestParam(name = "page_num", defaultValue = "1", required = false) Long pageNum,
      @RequestParam(name = "page_size", defaultValue = "10", required = false) Long pageSize,
      @RequestParam(name = "type", defaultValue = "NORMAL", required = false) NewsTypeEnum type,
      @RequestParam(name = "order", defaultValue = "DATE_DESC", required = false)
          ListAllNewsOrderEnum order,
      @RequestParam(name = "date", defaultValue = "", required = false) String date) {
    return newsService.getAllNews(pageNum, pageSize, order, type, date);
  }

  /**
   * 向一条新闻添加评论
   *
   * @param newsId
   * @param submitCommentInputDTO
   * @return
   */
  @PostMapping("/{id}/comments")
  public CommentOutPutDTO addComment(
      @PathVariable(name = "id") Long newsId,
      @RequestBody SubmitCommentInputDTO submitCommentInputDTO) {
    return commentService.submitComment(newsId, submitCommentInputDTO);
  }

  /**
   * 为某个新闻投票
   *
   * @param newsId
   */
  @PostMapping("/{id}/vote")
  public void vote(@PathVariable(name = "id") Long newsId) {
    newsService.vote(newsId);
  }

  //  unvote
  @PostMapping("/{id}/unvote")
  public void unvote(@PathVariable(name = "id") Long newsId) {
    newsService.unvote(newsId);
  }

  // 获取票数
  @GetMapping("/{id}/vote_count")
  public Long getVoteCount(@PathVariable(name = "id") Long newsId) {
    return newsService.getVoteCount(newsId);
  }

  private final NewsService newsService;
  private final CommentService commentService;
}
