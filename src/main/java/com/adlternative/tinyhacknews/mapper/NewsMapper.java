package com.adlternative.tinyhacknews.mapper;

import com.adlternative.tinyhacknews.entity.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface NewsMapper extends BaseMapper<News> {
  @Select("SELECT * FROM news WHERE author_id = #{authorId} AND is_deleted = 0")
  List<News> selectByAuthorId(@Param("authorId") Long authorId);

  @Select("SELECT * FROM news WHERE is_deleted = 0")
  List<News> selectAll();
}
