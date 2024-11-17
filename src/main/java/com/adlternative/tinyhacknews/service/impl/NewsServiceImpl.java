package com.adlternative.tinyhacknews.service.impl;

import com.adlternative.tinyhacknews.entity.News;
import com.adlternative.tinyhacknews.entity.NewsData;
import com.adlternative.tinyhacknews.entity.NewsInfo;
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
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
  @Override
  public NewsInfo submit(Long userId, SubmitNewsInputDTO submitNewsInputDTO) {
    User user =
        userMapper
            .selectById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

    News news =
        News.builder()
            .title(submitNewsInputDTO.getTitle())
            .url(submitNewsInputDTO.getUrl())
            .text(submitNewsInputDTO.getText())
            .authorId(userId)
            .build();
    try {
      int affectedRows = newsMapper.insert(news);
      if (affectedRows == 0) {
        throw new DBException("Failed to insert news, affectedRows equals to zero");
      }
      return NewsInfo.builder().id(news.getId()).author(UserInfo.convertFrom(user)).build();
    } catch (Exception e) {
      throw new InternalErrorException("submit news failed", e);
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
      throw new InternalErrorException("delete news failed", e);
    }
  }

  @Override
  public NewsData getNews(Long id, Long userId) {
    // TODO: 校验用户权限

    try {
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
          .build();
    } catch (Exception e) {
      throw new InternalErrorException("get news failed", e);
    }
  }

  private final NewsMapper newsMapper;
  private final UserMapper userMapper;
}