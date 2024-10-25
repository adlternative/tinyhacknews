package com.adlternative.tinyhacknews.service.impl;

import com.adlternative.tinyhacknews.entity.News;
import com.adlternative.tinyhacknews.mapper.NewsMapper;
import com.adlternative.tinyhacknews.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Override
    public News createNews(News news) {
        newsMapper.insert(news);
        return news;
    }

    @Override
    public News getNewsById(Long id) {
        return newsMapper.selectById(id).orElse(null);
    }

    @Override
    public List<News> getAllNews() {
        return newsMapper.selectAll();
    }

    @Override
    public News updateNews(Long id, News news) {
        news.setId(id);
        newsMapper.update(news);
        return news;
    }

    @Override
    public void deleteNews(Long id) {
        newsMapper.delete(id);
    }
}
