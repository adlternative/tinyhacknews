package com.adlternative.tinyhacknews.service.impl;

import com.adlternative.tinyhacknews.context.RequestContext;
import com.adlternative.tinyhacknews.entity.News;
import com.adlternative.tinyhacknews.entity.NewsData;
import com.adlternative.tinyhacknews.entity.SubmitNewsInputDTO;
import com.adlternative.tinyhacknews.entity.UserInfo;
import com.adlternative.tinyhacknews.entity.Users;
import com.adlternative.tinyhacknews.exception.DBException;
import com.adlternative.tinyhacknews.exception.ForbiddenException;
import com.adlternative.tinyhacknews.exception.InternalErrorException;
import com.adlternative.tinyhacknews.exception.NewsNotFoundException;
import com.adlternative.tinyhacknews.exception.UserNotFoundException;
import com.adlternative.tinyhacknews.mapper.NewsMapper;
import com.adlternative.tinyhacknews.mapper.UsersMapper;
import com.adlternative.tinyhacknews.service.NewsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
  @Override
  public NewsData submit(SubmitNewsInputDTO submitNewsInputDTO) {
    Long userId = RequestContext.getUserId();
    Users user =
        Optional.ofNullable(userMapper.selectById(userId))
            .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

    LocalDateTime date = LocalDateTime.now();
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
  public void deleteNews(Long id) {
    Long userId = RequestContext.getUserId();
    // TODO: 校验用户权限
    News news =
        Optional.ofNullable(newsMapper.selectById(id))
            .orElseThrow(() -> new NewsNotFoundException("Failed to get news, news not found"));
    // 目前只是判断 userId 是否和 news.authorId 相等，之后会改成使用权限校验的方式
    if (!Objects.equals(news.getAuthorId(), userId)) {
      throw new ForbiddenException("You do not have permission to delete this news.");
    }

    try {
      int affectedRows = newsMapper.delete(new QueryWrapper<News>().eq("id", id));
      if (affectedRows == 0) {
        throw new DBException("Failed to delete news, affectedRows equals to zero");
      }
    } catch (Exception e) {
      throw new InternalErrorException("Delete news failed", e);
    }
  }

  @Override
  public NewsData getNews(Long id) {
    // TODO: 校验用户权限
    News news =
        Optional.ofNullable(newsMapper.selectById(id))
            .orElseThrow(() -> new NewsNotFoundException("Failed to get news, news not found"));
    Users user =
        Optional.ofNullable(userMapper.selectById(news.getAuthorId()))
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
  public NewsData changeNews(Long id, SubmitNewsInputDTO submitNewsInputDTO) {
    Long userId = RequestContext.getUserId();
    News news =
        Optional.ofNullable(newsMapper.selectById(id))
            .orElseThrow(() -> new NewsNotFoundException("Failed to get news, news not found"));
    Users user =
        Optional.ofNullable(userMapper.selectById(news.getAuthorId()))
            .orElseThrow(() -> new UserNotFoundException("Failed to get user, user not found"));

    // 目前只是判断 userId 是否和 news.authorId 相等，之后会改成使用权限校验的方式
    if (!Objects.equals(news.getAuthorId(), userId)) {
      throw new ForbiddenException("You do not have permission to change this news.");
    }

    news.setTitle(submitNewsInputDTO.getTitle());
    news.setText(submitNewsInputDTO.getText());
    news.setUrl(submitNewsInputDTO.getUrl());
    news.setUpdatedAt(LocalDateTime.now());
    try {
      int affectedRows = newsMapper.update(news, new QueryWrapper<News>().eq("id", id));
      if (affectedRows == 0) {
        throw new DBException("Failed to update news, affectedRows equals to zero");
      }
      return NewsData.convertFromNews(news, user);

    } catch (Exception e) {
      throw new InternalErrorException("Update news failed", e);
    }
  }

  @Override
  public IPage<NewsData> getAllNewsOfUser(Long userId, Long pageNum, Long pageSize) {
    Users user =
        Optional.ofNullable(userMapper.selectById(userId))
            .orElseThrow(() -> new UserNotFoundException("Failed to get user, user not found"));
    return newsMapper
        .selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<News>().eq("author_id", userId))
        .convert(singleNew -> NewsData.convertFromNews(singleNew, user));
  }

  @Override
  public IPage<NewsData> getAllNews(Long pageNum, Long pageSize) {
    return newsMapper
        .selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<>())
        .convert(
            singleNew -> {
              Long userId = singleNew.getAuthorId();
              Users user =
                  Optional.ofNullable(userMapper.selectById(userId))
                      .orElseThrow(
                          () -> new UserNotFoundException("Failed to get user, user not found"));

              return NewsData.convertFromNews(singleNew, user);
            });
  }

  private final NewsMapper newsMapper;
  private final UsersMapper userMapper;
}
