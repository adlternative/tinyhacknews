package com.adlternative.tinyhacknews.mapper;

import com.adlternative.tinyhacknews.entity.News;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
      "INSERT INTO News (title, url, text, authorId, createdAt, updatedAt) VALUES (#{title}, #{url}, #{text}, #{authorId}, #{createdAt}, #{updatedAt})")
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  int insert(News news);

  @Delete("DELETE FROM News WHERE id = #{id}")
  int delete(Long id);

  @Select("SELECT * FROM News WHERE id = #{id}")
  Optional<News> selectById(Long id);

  @Select("SELECT * FROM News WHERE authorId = #{authorId}")
  List<News> selectByAuthorId(Long authorId);

  @Update(
      "UPDATE News SET title=#{title}, url=#{url}, text=#{text}, updatedAt=#{updatedAt} WHERE id=#{id}")
  int update(News news);
}
