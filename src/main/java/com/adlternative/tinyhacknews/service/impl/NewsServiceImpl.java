package com.adlternative.tinyhacknews.service.impl;

import static com.adlternative.tinyhacknews.constants.NewsRankingConstants.NEWS_RANKING_DEFAULT_TOPN;
import static com.adlternative.tinyhacknews.utils.UrlValidator.isValidHttpUrl;
import static java.util.Map.Entry.comparingByValue;

import com.adlternative.tinyhacknews.context.RequestContext;
import com.adlternative.tinyhacknews.entity.Comments;
import com.adlternative.tinyhacknews.entity.News;
import com.adlternative.tinyhacknews.entity.Users;
import com.adlternative.tinyhacknews.entity.Votes;
import com.adlternative.tinyhacknews.exception.DBException;
import com.adlternative.tinyhacknews.exception.ForbiddenException;
import com.adlternative.tinyhacknews.exception.InternalErrorException;
import com.adlternative.tinyhacknews.exception.NewsNotFoundException;
import com.adlternative.tinyhacknews.exception.UserNotFoundException;
import com.adlternative.tinyhacknews.mapper.CommentsMapper;
import com.adlternative.tinyhacknews.mapper.NewsMapper;
import com.adlternative.tinyhacknews.mapper.UsersMapper;
import com.adlternative.tinyhacknews.mapper.VotesMapper;
import com.adlternative.tinyhacknews.models.dto.NewsWithRankScore;
import com.adlternative.tinyhacknews.models.enums.ListAllNewsOrderEnum;
import com.adlternative.tinyhacknews.models.enums.NewsTypeEnum;
import com.adlternative.tinyhacknews.models.enums.VoteItemTypeEnum;
import com.adlternative.tinyhacknews.models.input.SubmitNewsInputDTO;
import com.adlternative.tinyhacknews.models.output.NewsDataOutputDTO;
import com.adlternative.tinyhacknews.models.output.NewsMetaDetailsOutputDTO;
import com.adlternative.tinyhacknews.models.pages.PageOutputDTO;
import com.adlternative.tinyhacknews.models.pages.PagePara;
import com.adlternative.tinyhacknews.service.NewsRankingCacheService;
import com.adlternative.tinyhacknews.service.NewsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

  @Override
  public NewsDataOutputDTO submit(SubmitNewsInputDTO submitNewsInputDTO) {
    String url = submitNewsInputDTO.getUrl();

    // 如果 url 不包含协议，则默认使用 https
    if (!url.contains("://")) {
      url = "https://" + url;
    }

    boolean validHttpUrl = isValidHttpUrl(url);
    if (!validHttpUrl) {
      throw new IllegalArgumentException("Invalid url");
    }

    Long userId = RequestContext.getUserId();

    Users user = getUserByUserId(userId);
    String title = submitNewsInputDTO.getTitle();
    NewsTypeEnum newsType = guessNewsType(title);

    Date date = new Date();
    News news =
        new News()
            .setTitle(title)
            .setUrl(url)
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
      NewsDataOutputDTO from = NewsDataOutputDTO.from(news, user, 0L, 0L, false);
      // TODO: 改成异步更新缓存
      newsRankingCacheService.addOrUpdateNewsScore(news.getId(), getNewsScore(news.getId()));
      return from;
    } catch (Exception e) {
      throw new InternalErrorException("Submit news failed", e);
    }
  }

  private static NewsTypeEnum guessNewsType(String title) {
    NewsTypeEnum newsType = NewsTypeEnum.NORMAL;

    // 将标题转换为小写以忽略大小写的差异
    String lowerCaseTitle = title.toLowerCase();
    if (lowerCaseTitle.startsWith("show hn:")) {
      newsType = NewsTypeEnum.SHOW;
    } else if (lowerCaseTitle.startsWith("ask hn:")) {
      newsType = NewsTypeEnum.ASK;
    } else if (lowerCaseTitle.startsWith("jobs hn:")) {
      newsType = NewsTypeEnum.JOBS;
    }
    return newsType;
  }

  @Override
  public double getNewsScore(Long newsId) {
    //  S= (1 + P) /(T + 2)^1.8
    // P  是新闻的投票数量
    // T  是新闻的创建时间（小时数）

    long diffInHours =
        (new Date().getTime() - getNews(newsId).getCreatedAt().getTime()) / (60 * 60 * 1000);

    return (1 + getVoteCount(newsId)) / Math.pow(diffInHours + 2, 1.8);
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
      // TODO: 改成异步更新缓存
      newsRankingCacheService.removeNews(news.getId());
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
    Users user = getUserByUserId(news.getAuthorId());

    return NewsDataOutputDTO.from(
        news,
        user,
        getCommentCount(news.getId()),
        getVoteCount(news.getId()),
        checkHasVote(news.getId()));
  }

  @Override
  public NewsDataOutputDTO changeNews(Long id, SubmitNewsInputDTO submitNewsInputDTO) {
    String url = submitNewsInputDTO.getUrl();

    // 如果 url 不包含协议，则默认使用 https
    if (!url.contains("://")) {
      url = "https://" + url;
    }

    boolean validHttpUrl = isValidHttpUrl(url);
    if (!validHttpUrl) {
      throw new IllegalArgumentException("Invalid url");
    }

    Long userId = RequestContext.getUserId();
    News news =
        Optional.ofNullable(newsMapper.selectById(id))
            .orElseThrow(() -> new NewsNotFoundException("Failed to get news, news not found"));
    Users user = getUserByUserId(news.getAuthorId());

    // 目前只是判断 userId 是否和 news.authorId 相等，之后会改成使用权限校验的方式
    if (!Objects.equals(news.getAuthorId(), userId)) {
      throw new ForbiddenException("You do not have permission to change this news.");
    }

    news.setTitle(submitNewsInputDTO.getTitle());
    news.setText(submitNewsInputDTO.getText());
    news.setUrl(url);
    news.setUpdatedAt(new Date());
    try {
      int affectedRows = newsMapper.update(news, new QueryWrapper<News>().eq("id", id));
      if (affectedRows == 0) {
        throw new DBException("Failed to update news, affectedRows equals to zero");
      }

      return NewsDataOutputDTO.from(
          news,
          user,
          getCommentCount(news.getId()),
          getVoteCount(news.getId()),
          checkHasVote(news.getId()));

    } catch (Exception e) {
      throw new InternalErrorException("Update news failed", e);
    }
  }

  @Override
  public IPage<NewsDataOutputDTO> getAllNewsOfUser(Long userId, Long pageNum, Long pageSize) {
    Users user = getUserByUserId(userId);
    return newsMapper
        .selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<News>().eq("author_id", userId))
        .convert(
            singleNew ->
                NewsDataOutputDTO.from(
                    singleNew,
                    user,
                    getCommentCount(singleNew.getId()),
                    getVoteCount(singleNew.getId()),
                    checkHasVote(singleNew.getId())));
  }

  private Boolean checkHasVote(Long newId) {
    return votesMapper.selectCount(
            new QueryWrapper<Votes>()
                .eq("item_id", newId)
                .eq("item_type", VoteItemTypeEnum.NEWS.name())
                .eq("user_id", RequestContext.getUserId()))
        > 0;
  }

  @Override
  public PageOutputDTO<NewsMetaDetailsOutputDTO> getAllNews(
      Long pageNum, Long pageSize, ListAllNewsOrderEnum order, NewsTypeEnum type, String date) {
    // TODO: date 必须是形如 2020-01-01 的格式, 否则会抛出异常
    // 检查日期格式是否正确
    if (StringUtils.isNotEmpty(date) && !date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
      throw new IllegalArgumentException("Date must be in YYYY-MM-DD format");
    }

    // 日期模式不能与分数模式一起使用
    if (StringUtils.isNotEmpty(date) && order == ListAllNewsOrderEnum.POINT) {
      throw new IllegalArgumentException("Date mode cannot be used with POINT order");
    }

    // 按分数排序
    if (order == ListAllNewsOrderEnum.POINT) {
      PageOutputDTO<NewsMetaDetailsOutputDTO> allNewsOrderByRank =
          getAllNewsOrderByRank(pageNum, pageSize, type);
      if (allNewsOrderByRank.getList().size() >= 30) {
        return allNewsOrderByRank;
      }
      // <= 30 特殊处理，兜底从db捞出来，然后排序
    }

    IPage<News> news =
        newsMapper.selectAllInOrder(new Page<>(pageNum, pageSize), order, type, date);

    if (order == ListAllNewsOrderEnum.POINT) {
      // 计算 news 分数，首先按照分数排序，然后按照日期倒序
      IPage<NewsWithRankScore> newsWithRankScores =
          news.convert(
              singleNews -> new NewsWithRankScore(singleNews, getNewsScore(singleNews.getId())));
      Collections.sort(newsWithRankScores.getRecords());
      news = newsWithRankScores.convert(NewsWithRankScore::getNews);
    }

    return PageOutputDTO.from(
        news.convert(
            singleNew -> {
              Users user = getUserByUserId(singleNew.getAuthorId());
              // TODO: 直接 mapper xml join 实现
              Long commentCount = getCommentCount(singleNew.getId());

              return NewsMetaDetailsOutputDTO.from(
                  singleNew,
                  user,
                  commentCount,
                  getVoteCount(singleNew.getId()),
                  checkHasVote(singleNew.getId()));
            }));
  }

  private PageOutputDTO<NewsMetaDetailsOutputDTO> getAllNewsOrderByRank(
      Long pageNum, Long pageSize, NewsTypeEnum type) {
    // 按分数排序
    List<NewsMetaDetailsOutputDTO> topNews = getTopNews((pageNum - 1) * pageSize, pageSize);

    if (type != NewsTypeEnum.NORMAL) {
      topNews =
          topNews.stream()
              .filter(news -> news.getNewsType().equals(type.toString()))
              .collect(Collectors.toList());
    }

    PageOutputDTO<NewsMetaDetailsOutputDTO> pageOutputDTO = new PageOutputDTO<>();
    pageOutputDTO.setList(topNews);
    pageOutputDTO.setPage(new PagePara(pageNum, pageSize, -1L, -1L));
    return pageOutputDTO;
  }

  private Long getCommentCount(Long newsId) {
    return commentMapper.selectCount(new QueryWrapper<Comments>().eq("news_id", newsId));
  }

  @Override
  public void vote(Long newsId) {
    // 如果已经投票过，则不允许重复投票
    if (votesMapper.selectCount(
            new QueryWrapper<Votes>()
                .eq("item_id", newsId)
                .eq("item_type", VoteItemTypeEnum.NEWS.name())
                .eq("user_id", RequestContext.getUserId()))
        > 0) {
      throw new ForbiddenException("You have already voted for this news.");
    }

    // 如果这条新闻的作者是当前用户，则不允许投票
    if (Objects.equals(newsMapper.selectById(newsId).getAuthorId(), RequestContext.getUserId())) {
      throw new ForbiddenException("You cannot vote for your own news.");
    }

    // 插入投票记录
    Date now = new Date();
    votesMapper.insert(
        new Votes()
            .setUserId(RequestContext.getUserId())
            .setItemId(newsId)
            .setItemType(VoteItemTypeEnum.NEWS.name())
            .setUpdatedAt(now)
            .setCreatedAt(now));
    // 更新缓存分数
    newsRankingCacheService.addOrUpdateNewsScore(newsId, getNewsScore(newsId));
  }

  @Override
  public void unvote(Long newsId) {
    try {
      // 如果没有投票过，则不允许取消投票

      Votes votes =
          votesMapper.selectOne(
              new QueryWrapper<Votes>()
                  .eq("item_id", newsId)
                  .eq("item_type", VoteItemTypeEnum.NEWS.name())
                  .eq("user_id", RequestContext.getUserId()));
      if (votes == null) {
        throw new ForbiddenException("You have not voted for this news.");
      }

      votesMapper.deleteById(votes.getId());
    } catch (Exception e) {
      log.error("Failed to unvote news", e);
      throw new InternalErrorException("Failed to unvote news", e);
    }
  }

  @Override
  public Long getVoteCount(Long newsId) {
    return votesMapper.selectCount(
        new QueryWrapper<Votes>()
            .eq("item_id", newsId)
            .eq("item_type", VoteItemTypeEnum.NEWS.name()));
  }

  @Override
  public List<NewsMetaDetailsOutputDTO> getTopNews(Long offset, Long limit) {
    // TODO: 改成一个 SQL
    log.info("Get top news, offset: {}, limit: {}", offset, limit);

    Set<ZSetOperations.TypedTuple<String>> newsWithScore =
        newsRankingCacheService.getNewsWithScore(offset.intValue(), limit.intValue());
    log.debug("newsWithScore length: {}", newsWithScore.size());

    // 默认按照 score 倒序，如果 score 相同，则按照新闻创建时间倒序
    Set<NewsWithRankScore> newsWithRankScore = new TreeSet<>();
    for (ZSetOperations.TypedTuple<String> newsWithScoreTuple : newsWithScore) {
      newsWithRankScore.add(
          new NewsWithRankScore(
              newsMapper.selectById(
                  Long.valueOf(Objects.requireNonNull(newsWithScoreTuple.getValue()))),
              newsWithScoreTuple.getScore()));
    }

    log.debug("newsWithRankScore length: {}", newsWithRankScore.size());

    return newsWithRankScore.stream()
        .map(NewsWithRankScore::getNews)
        .map(News::getId)
        .map(newsMapper::selectById)
        .map(
            singleNew -> {
              Users user = getUserByUserId(singleNew.getAuthorId());
              // TODO: 直接 mapper xml join 实现
              Long commentCount = getCommentCount(singleNew.getId());

              return NewsMetaDetailsOutputDTO.from(
                  singleNew,
                  user,
                  commentCount,
                  getVoteCount(singleNew.getId()),
                  checkHasVote(singleNew.getId()));
            })
        .collect(Collectors.toList());
  }

  @Override
  public void reCalculateNewsRankScore() {
    Set<String> cacheRankNews = newsRankingCacheService.getAllNews();
    if (cacheRankNews.isEmpty()) {
      preHeatingNewsRank();
    }

    cacheRankNews.stream()
        .map(Long::valueOf)
        // TODO: 换成批量更新
        .forEach(
            newsId -> newsRankingCacheService.addOrUpdateNewsScore(newsId, getNewsScore(newsId)));
  }

  // 捞x周以内创建的新闻
  private Set<Long> getRecentNewsIds(long week) {
    return newsMapper
        .selectList(new QueryWrapper<News>().gt("created_at", LocalDateTime.now().minusWeeks(week)))
        .stream()
        .map(News::getId)
        .collect(Collectors.toSet());
  }

  // 捞x周以内有评论的新闻
  private Set<Long> getRecentNewsIdsWithComments(long week) {
    return commentMapper
        .selectList(
            new QueryWrapper<Comments>().gt("created_at", LocalDateTime.now().minusWeeks(week)))
        .stream()
        .map(Comments::getNewsId)
        .collect(Collectors.toSet());
  }

  private Users getUserByUserId(Long userId) {
    return Optional.ofNullable(userMapper.selectById(userId))
        .orElseThrow(() -> new UserNotFoundException("Failed to get user, user not found"));
  }

  // 捞x周以内有投票的新闻
  private Set<Long> getRecentNewsIdsWithVotes(long week) {
    return votesMapper
        .selectList(
            new QueryWrapper<Votes>().gt("created_at", LocalDateTime.now().minusWeeks(week)))
        .stream()
        .filter(vote -> vote.getItemType().equals(VoteItemTypeEnum.NEWS.name()))
        .map(Votes::getItemId)
        .collect(Collectors.toSet());
  }

  // 预热新闻排名
  public void preHeatingNewsRank() {
    // 只在 redis 中缺少新闻排名时才预热
    if (newsRankingCacheService.getNewsCount() > 0) {
      log.info("News rank is already pre-heated");
      return;
    }

    log.info("Pre-heating news rank to redis");
    // 1. 确定候选集

    Set<Long> newIds = new HashSet<>();

    // 1.1 如果数据库中新闻总数特别少，< N 直接将数据库中的全部数据放入候选集
    if (newsMapper.selectCount(new QueryWrapper<>()).intValue() < NEWS_RANKING_DEFAULT_TOPN) {
      newIds.addAll(
          newsMapper.selectList(new QueryWrapper<>()).stream()
              .map(News::getId)
              .collect(Collectors.toSet()));
    } else {
      // 1.2 选出一周内的热点新闻
      long week = 1L;

      // 合并3个集合
      newIds.addAll(getRecentNewsIds(week));
      newIds.addAll(getRecentNewsIdsWithVotes(week));
      newIds.addAll(getRecentNewsIdsWithComments(week));

      // 如果候选集数量不足 N，说明可能最近的新闻就是不够多（像是产品初期没有用户），那就按照创建时间倒序，取前 N 个
      if (newIds.size() < NEWS_RANKING_DEFAULT_TOPN) {
        newIds.addAll(
            newsMapper
                .selectList(
                    new QueryWrapper<News>()
                        .orderByDesc("created_at")
                        .last(String.format("limit %d", NEWS_RANKING_DEFAULT_TOPN)))
                .stream()
                .map(News::getId)
                .collect(Collectors.toSet()));
      }
    }

    // newsID -> score
    Map<Long, Double> news2Score = new HashMap<>();

    newIds.forEach(newsId -> news2Score.put(newsId, getNewsScore(newsId)));
    // 进行排序，过滤 前 N
    List<Map.Entry<Long, Double>> topKEntries =
        news2Score.entrySet().stream()
            .sorted(comparingByValue(Comparator.reverseOrder()))
            .limit(NEWS_RANKING_DEFAULT_TOPN)
            .collect(Collectors.toList());

    // 2. 预热新闻排名
    newsRankingCacheService.batchAdd(topKEntries);
  }

  private final NewsMapper newsMapper;
  private final UsersMapper userMapper;
  private final CommentsMapper commentMapper;
  private final VotesMapper votesMapper;
  private final NewsRankingCacheService newsRankingCacheService;
}
