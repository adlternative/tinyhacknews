package com.adlternative.tinyhacknews.service;

import static com.adlternative.tinyhacknews.constants.NewsRankingConstants.NEWS_RANKING_DEFAULT_TOPN;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
public class NewsRankingCacheService {

  private static final String NEWS_RANKING_ZSET_KEY = "news:ranking";

  @Autowired private StringRedisTemplate stringRedisTemplate;

  /**
   * 使用 Lua 脚本原子性地添加或更新新闻分数，并裁剪 zset 保留 Top N
   *
   * @param newsId 新闻ID
   * @param score 分数
   */
  public void addOrUpdateNewsScore(Long newsId, double score) {
    String newsIdStr = String.valueOf(newsId);
    String script =
        "redis.call('ZADD', KEYS[1], ARGV[1], ARGV[2]); "
            + "redis.call('ZREMRANGEBYRANK', KEYS[1], 0, -"
            + (NEWS_RANKING_DEFAULT_TOPN + 1)
            + "); "
            + "return 1;";

    stringRedisTemplate.execute(
        (RedisConnection connection) ->
            connection
                .scriptingCommands()
                .eval(
                    script.getBytes(StandardCharsets.UTF_8),
                    ReturnType.INTEGER,
                    1,
                    NEWS_RANKING_ZSET_KEY.getBytes(StandardCharsets.UTF_8),
                    String.valueOf(score).getBytes(StandardCharsets.UTF_8),
                    newsIdStr.getBytes(StandardCharsets.UTF_8)),
        true);
  }

  public void batchAdd(List<Map.Entry<Long, Double>> news2Score) {
    // TODO: 使用 Lua 脚本，原子性地添加或更新新闻分数
    batchAddToZSet(NEWS_RANKING_ZSET_KEY, news2Score);
  }

  public void batchAddToZSet(String zsetKey, List<java.util.Map.Entry<Long, Double>> news2Score) {
    if (news2Score == null || news2Score.isEmpty()) {
      return;
    }

    Set<ZSetOperations.TypedTuple<String>> tuples =
        news2Score.stream()
            .map(entry -> new DefaultTypedTuple<>(String.valueOf(entry.getKey()), entry.getValue()))
            .collect(Collectors.toSet());

    stringRedisTemplate.opsForZSet().add(zsetKey, tuples);
  }

  /**
   * 获取 Top N 新闻 ID
   *
   * @param topN 需要获取的排名数量，最大为 TOP_N
   * @return 按分数从高到低排序的新闻ID集合
   */
  public Set<String> getTopNNews(int topN) {
    if (topN > NEWS_RANKING_DEFAULT_TOPN) {
      topN = NEWS_RANKING_DEFAULT_TOPN.intValue();
    }
    return stringRedisTemplate.opsForZSet().reverseRange(NEWS_RANKING_ZSET_KEY, 0, topN - 1);
  }

  /**
   * 获取所有新闻
   *
   * @param offset 偏移量
   * @param limit 获取的数量
   * @return 指定范围内的新闻ID集合
   */
  public Set<String> getNews(int offset, int limit) {
    return stringRedisTemplate
        .opsForZSet()
        .reverseRange(NEWS_RANKING_ZSET_KEY, offset, offset + limit - 1);
  }

  /**
   * 获取所有新闻，并返回分数
   *
   * @param offset
   * @param limit
   * @return
   */
  public Set<ZSetOperations.TypedTuple<String>> getNewsWithScore(int offset, int limit) {
    return stringRedisTemplate
        .opsForZSet()
        .reverseRangeWithScores(NEWS_RANKING_ZSET_KEY, offset, offset + limit - 1);
  }

  /**
   * 获取排行榜中的新闻数量
   *
   * @return
   */
  public Long getNewsCount() {
    return stringRedisTemplate.opsForZSet().zCard(NEWS_RANKING_ZSET_KEY);
  }

  /**
   * 从缓存中移除某个新闻
   *
   * @param newsId 新闻ID
   */
  public void removeNews(Long newsId) {
    String newsIdStr = String.valueOf(newsId);
    stringRedisTemplate.opsForZSet().remove(NEWS_RANKING_ZSET_KEY, newsIdStr);
  }

  /** 取出 zset 中所有数据 */
  public Set<String> getAllNews() {
    return stringRedisTemplate.opsForZSet().range(NEWS_RANKING_ZSET_KEY, 0, -1);
  }
}
