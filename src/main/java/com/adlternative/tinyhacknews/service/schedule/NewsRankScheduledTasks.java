package com.adlternative.tinyhacknews.service.schedule;

import com.adlternative.tinyhacknews.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NewsRankScheduledTasks {

  // 每分钟执行任务
  @Scheduled(fixedDelay = 10000)
  public void doTask() {
    log.info("do schedule task!");
    // 将 redis 中排名中每个新闻重新计算分数
    newsService.reCalculateNewsRankScore();
    log.info("after schedule task!");
  }

  private final NewsService newsService;
}
