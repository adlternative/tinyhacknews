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

  @Insert("INSERT INTO News (title, content, author, createdAt, updatedAt) VALUES (#{title}, #{content}, #{author}, #{createdAt}, #{updatedAt})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  int insert(News news);

  @Select("SELECT * FROM News WHERE id = #{id}")
  Optional<News> selectById(Long id);

  @Select("SELECT * FROM News")
  List<News> selectAll();

  @Update("UPDATE News SET title=#{title}, content=#{content}, author=#{author}, updatedAt=#{updatedAt} WHERE id=#{id}")
  int update(News news);

  @Delete("DELETE FROM News WHERE id=#{id}")
  int delete(Long id);
}
