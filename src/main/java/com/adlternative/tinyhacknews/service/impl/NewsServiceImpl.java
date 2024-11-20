package com.adlternative.tinyhacknews.service.impl;

import com.adlternative.tinyhacknews.entity.News;
import com.adlternative.tinyhacknews.entity.NewsData;
import com.adlternative.tinyhacknews.entity.SubmitNewsInputDTO;
import com.adlternative.tinyhacknews.entity.User;
import com.adlternative.tinyhacknews.entity.UserInfo;
import com.adlternative.tinyhacknews.exception.DBException;
import com.adlternative.tinyhacknews.exception.ForbiddenException;
import com.adlternative.tinyhacknews.exception.InternalErrorException;
import com.adlternative.tinyhacknews.exception.NewsNotFoundException;
import com.adlternative.tinyhacknews.exception.UserNotFoundException;
import com.adlternative.tinyhacknews.mapper.NewsMapper;
import com.adlternative.tinyhacknews.mapper.UserMapper;
import com.adlternative.tinyhacknews.service.NewsService;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
  @Override
  public NewsData submit(Long userId, SubmitNewsInputDTO submitNewsInputDTO) {
    User user =
        userMapper
            .selectById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

    Date date = new Date();
    News news =
        News.builder()
            .title(submitNewsInputDTO.getTitle())
            .url(submitNewsInputDTO.getUrl())
            .text(submitNewsInputDTO.getText())
            .authorId(userId)
            .createdAt(date)
            .updatedAt(date)
            .build();
    try {
      int affectedRows = newsMapper.insert(news);
      if (affectedRows == 0) {
        throw new DBException("Failed to insert news, affectedRows equals to zero");
      }
      return NewsData.builder()
          .id(news.getId())
          .title(news.getTitle())
          .text(news.getText())
          .url(news.getUrl())
          .createdAt(news.getCreatedAt())
          .updatedAt(news.getUpdatedAt())
          .author(UserInfo.convertFrom(user))
          .build();
    } catch (Exception e) {
      throw new InternalErrorException("Submit news failed", e);
    }
  }

  @Override
  public void deleteNews(Long id, Long userId) {
    // TODO: 校验用户权限
    News news =
        newsMapper
            .selectById(id)
            .orElseThrow(() -> new NewsNotFoundException("Failed to get news, news not found"));
    // 目前只是判断 userId 是否和 news.authorId 相等，之后会改成使用权限校验的方式
    if (!Objects.equals(news.getAuthorId(), userId)) {
      throw new ForbiddenException("You do not have permission to delete this news.");
    }

    try {
      int affectedRows = newsMapper.delete(id);
      if (affectedRows == 0) {
        throw new DBException("Failed to delete news, affectedRows equals to zero");
      }
    } catch (Exception e) {
      throw new InternalErrorException("Delete news failed", e);
    }
  }

  @Override
  public NewsData getNews(Long id, Long userId) {
    // TODO: 校验用户权限
    News news =
        newsMapper
            .selectById(id)
            .orElseThrow(() -> new NewsNotFoundException("Failed to get news, news not found"));
    User user =
        userMapper
            .selectById(news.getAuthorId())
            .orElseThrow(() -> new UserNotFoundException("Failed to get news, user not found"));
    return NewsData.builder()
        .id(news.getId())
        .url(news.getUrl())
        .title(news.getTitle())
        .text(news.getText())
        .author(UserInfo.convertFrom(user))
        .createdAt(news.getCreatedAt())
        .updatedAt(news.getUpdatedAt())
        .build();
  }

  @Override
  public NewsData changeNews(Long id, Long userId, SubmitNewsInputDTO submitNewsInputDTO) {
    News news =
        newsMapper
            .selectById(id)
            .orElseThrow(() -> new NewsNotFoundException("Failed to get news, news not found"));
    User user =
        userMapper
            .selectById(news.getAuthorId())
            .orElseThrow(() -> new UserNotFoundException("Failed to get user, user not found"));

    // 目前只是判断 userId 是否和 news.authorId 相等，之后会改成使用权限校验的方式
    if (!Objects.equals(news.getAuthorId(), userId)) {
      throw new ForbiddenException("You do not have permission to change this news.");
    }

    news.setTitle(submitNewsInputDTO.getTitle());
    news.setText(submitNewsInputDTO.getText());
    news.setUrl(submitNewsInputDTO.getUrl());
    news.setUpdatedAt(new Date());
    try {
      int affectedRows = newsMapper.update(news);
      if (affectedRows == 0) {
        throw new DBException("Failed to update news, affectedRows equals to zero");
      }
      return NewsData.convertFromNews(news, user);

    } catch (Exception e) {
      throw new InternalErrorException("Update news failed", e);
    }
  }

  @Override
  public List<NewsData> getAllNewsOfUser(Long userId) {
    User user =
        userMapper
            .selectById(userId)
            .orElseThrow(() -> new UserNotFoundException("Failed to get user, user not found"));
    return newsMapper.selectByAuthorId(userId).stream()
        .map(singleNew -> NewsData.convertFromNews(singleNew, user))
        .collect(Collectors.toList());
  }

  @Override
  public List<NewsData> getAllNews(Long userId) {
    User user =
        userMapper
            .selectById(userId)
            .orElseThrow(() -> new UserNotFoundException("Failed to get user, user not found"));
    return newsMapper.selectAll().stream()
        .map(singleNew -> NewsData.convertFromNews(singleNew, user))
        .collect(Collectors.toList());
  }

  private final NewsMapper newsMapper;
  private final UserMapper userMapper;
}
