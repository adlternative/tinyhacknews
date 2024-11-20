package com.adlternative.tinyhacknews.mapper;

import com.adlternative.tinyhacknews.entity.News;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
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

  @Update("UPDATE News SET is_deleted = 1 WHERE id = #{id}")
  int delete(@Param("id") Long id);

  @Select("SELECT * FROM News WHERE id = #{id} AND is_deleted = 0")
  Optional<News> selectById(@Param("id") Long id);

  @Select("SELECT * FROM News WHERE authorId = #{authorId} AND is_deleted = 0")
  List<News> selectByAuthorId(@Param("authorId") Long authorId);

  @Update(
      "UPDATE News SET title=#{title}, url=#{url}, text=#{text}, updatedAt=#{updatedAt} WHERE id=#{id} AND is_deleted = 0")
  int update(News news);
}
