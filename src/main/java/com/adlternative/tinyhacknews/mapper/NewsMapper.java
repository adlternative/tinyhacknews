package com.adlternative.tinyhacknews.mapper;

import com.adlternative.tinyhacknews.entity.News;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface NewsMapper {
  /**
   * 插入一条新闻
   *
   * @param news
   */
  @Insert(
      "INSERT INTO News (title, url, text, authorId) VALUES (#{title}, #{url}, #{text}, #{authorId})")
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  int insert(News news);
}
