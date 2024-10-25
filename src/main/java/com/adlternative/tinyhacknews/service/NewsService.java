package com.adlternative.tinyhacknews.service;

import com.adlternative.tinyhacknews.entity.News;
import java.util.List;

public interface NewsService {
    News createNews(News news);
    News getNewsById(Long id);
    List<News> getAllNews();
    News updateNews(Long id, News news);
    void deleteNews(Long id);
}
