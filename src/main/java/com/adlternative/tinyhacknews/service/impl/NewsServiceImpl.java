package com.adlternative.tinyhacknews.service.impl;

import com.adlternative.tinyhacknews.context.RequestContext;
import com.adlternative.tinyhacknews.entity.Comments;
import com.adlternative.tinyhacknews.entity.News;
import com.adlternative.tinyhacknews.entity.Users;
import com.adlternative.tinyhacknews.exception.DBException;
import com.adlternative.tinyhacknews.exception.ForbiddenException;
import com.adlternative.tinyhacknews.exception.InternalErrorException;
import com.adlternative.tinyhacknews.exception.NewsNotFoundException;
import com.adlternative.tinyhacknews.exception.UserNotFoundException;
import com.adlternative.tinyhacknews.mapper.CommentsMapper;
import com.adlternative.tinyhacknews.mapper.NewsMapper;
import com.adlternative.tinyhacknews.mapper.UsersMapper;
import com.adlternative.tinyhacknews.models.enums.ListAllNewsOrderEnum;
import com.adlternative.tinyhacknews.models.enums.NewsTypeEnum;
import com.adlternative.tinyhacknews.models.input.SubmitNewsInputDTO;
import com.adlternative.tinyhacknews.models.output.NewsDataOutputDTO;
import com.adlternative.tinyhacknews.models.output.NewsMetaDetailsOutputDTO;
import com.adlternative.tinyhacknews.service.NewsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
  @Override
  public NewsDataOutputDTO submit(SubmitNewsInputDTO submitNewsInputDTO) {
    Long userId = RequestContext.getUserId();
    Users user =
        Optional.ofNullable(userMapper.selectById(userId))
            .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));
    NewsTypeEnum newsType = NewsTypeEnum.NORMAL;
    String title = submitNewsInputDTO.getTitle();
    if (title.startsWith("Show HN:")) {
      newsType = NewsTypeEnum.SHOW;
    } else if (title.startsWith("Ask HN:")) {
      newsType = NewsTypeEnum.ASK;
    } else if (title.startsWith("Jobs HN:")) {
      newsType = NewsTypeEnum.JOBS;
    }

    Date date = new Date();
    News news =
        new News()
            .setTitle(submitNewsInputDTO.getTitle())
            .setUrl(submitNewsInputDTO.getUrl())
            .setText(submitNewsInputDTO.getText())
            .setNewsType(newsType.name())
            .setAuthorId(userId)
            .setCreatedAt(date)
            .setUpdatedAt(date);

    try {
      int affectedRows = newsMapper.insert(news);
      if (affectedRows == 0) {
        throw new DBException("Failed to insert news, affectedRows equals to zero");
      }
      return NewsDataOutputDTO.from(news, user);
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
  public NewsDataOutputDTO getNews(Long id) {
    // TODO: 校验用户权限
    News news =
        Optional.ofNullable(newsMapper.selectById(id))
            .orElseThrow(() -> new NewsNotFoundException("Failed to get news, news not found"));
    Users user =
        Optional.ofNullable(userMapper.selectById(news.getAuthorId()))
            .orElseThrow(() -> new UserNotFoundException("Failed to get news, user not found"));
    return NewsDataOutputDTO.from(news, user);
  }

  @Override
  public NewsDataOutputDTO changeNews(Long id, SubmitNewsInputDTO submitNewsInputDTO) {
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
    news.setUpdatedAt(new Date());
    try {
      int affectedRows = newsMapper.update(news, new QueryWrapper<News>().eq("id", id));
      if (affectedRows == 0) {
        throw new DBException("Failed to update news, affectedRows equals to zero");
      }
      return NewsDataOutputDTO.from(news, user);

    } catch (Exception e) {
      throw new InternalErrorException("Update news failed", e);
    }
  }

  @Override
  public IPage<NewsDataOutputDTO> getAllNewsOfUser(Long userId, Long pageNum, Long pageSize) {
    Users user =
        Optional.ofNullable(userMapper.selectById(userId))
            .orElseThrow(() -> new UserNotFoundException("Failed to get user, user not found"));
    return newsMapper
        .selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<News>().eq("author_id", userId))
        .convert(singleNew -> NewsDataOutputDTO.from(singleNew, user));
  }

  @Override
  public IPage<NewsMetaDetailsOutputDTO> getAllNews(
      Long pageNum, Long pageSize, ListAllNewsOrderEnum order, NewsTypeEnum type, String date) {
    // TODO: date 必须是形如 2020-01-01 的格式, 否则会抛出异常
    // 检查日期格式是否正确
    if (StringUtils.isNotEmpty(date) && !date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
      throw new IllegalArgumentException("Date must be in YYYY-MM-DD format");
    }

    return newsMapper
        .selectAllInOrder(new Page<>(pageNum, pageSize), order, type, date)
        .convert(
            singleNew -> {
              Long userId = singleNew.getAuthorId();
              Users user =
                  Optional.ofNullable(userMapper.selectById(userId))
                      .orElseThrow(
                          () -> new UserNotFoundException("Failed to get user, user not found"));
              // TODO: 直接 mapper xml join 实现
              Long commentCount =
                  commentMapper.selectCount(
                      new QueryWrapper<Comments>().eq("news_id", singleNew.getId()));
              return NewsMetaDetailsOutputDTO.from(singleNew, user, commentCount, 0L);
            });
  }

  private final NewsMapper newsMapper;
  private final UsersMapper userMapper;
  private final CommentsMapper commentMapper;
}
