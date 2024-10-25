package com.adlternative.tinyhacknews.controller;

import com.adlternative.tinyhacknews.entity.News;
import com.adlternative.tinyhacknews.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

  @Autowired
  private NewsService newsService;

  @PostMapping
  public News createNews(@RequestBody News news) {
    return newsService.createNews(news);
  }

  @GetMapping("/{id}")
  public News getNewsById(@PathVariable("id") Long id) {
    return newsService.getNewsById(id);
  }

  @GetMapping
  public List<News> getAllNews() {
    return newsService.getAllNews();
  }

  @PutMapping("/{id}")
  public News updateNews(@PathVariable("id") Long id, @RequestBody News news) {
    return newsService.updateNews(id, news);
  }

  @DeleteMapping("/{id}")
  public void deleteNews(@PathVariable("id") Long id) {
    newsService.deleteNews(id);
  }
}
