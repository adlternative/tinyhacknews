package com.adlternative.tinyhacknews.service.impl;

import com.adlternative.tinyhacknews.entity.News;
import com.adlternative.tinyhacknews.entity.NewsInfo;
import com.adlternative.tinyhacknews.entity.SubmitNewsInputDTO;
import com.adlternative.tinyhacknews.entity.User;
import com.adlternative.tinyhacknews.entity.UserInfo;
import com.adlternative.tinyhacknews.exception.DBException;
import com.adlternative.tinyhacknews.exception.InternalErrorException;
import com.adlternative.tinyhacknews.exception.UserNotFoundException;
import com.adlternative.tinyhacknews.mapper.NewsMapper;
import com.adlternative.tinyhacknews.mapper.UserMapper;
import com.adlternative.tinyhacknews.service.NewsService;
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

  private final NewsMapper newsMapper;
  private final UserMapper userMapper;
}
