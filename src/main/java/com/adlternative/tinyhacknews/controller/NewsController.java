package com.adlternative.tinyhacknews.controller;

import com.adlternative.tinyhacknews.entity.NewsInfo;
import com.adlternative.tinyhacknews.entity.SubmitNewsInputDTO;
import com.adlternative.tinyhacknews.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

  // 提交新闻

  @PostMapping
  public NewsInfo submitNews(
      @RequestParam(name = "user_id") Long userId,
      @RequestBody SubmitNewsInputDTO submitNewsInputDTO) {
    return newsService.submit(userId, submitNewsInputDTO);
  }

  // 修改新闻

  // 查看新闻
  @GetMapping("/{id}")
  public NewsInfo getNews(
      @PathVariable(name = "id") Long id, @RequestParam(name = "user_id") Long userId) {
    return newsService.getNews(id, userId);
  }

  // 删除新闻
  @DeleteMapping("/{id}")
  public void deleteNews(
      @PathVariable(name = "id") Long id, @RequestParam(name = "user_id") Long userId) {
    newsService.deleteNews(id, userId);
  }

  private final NewsService newsService;
}
