package com.adlternative.tinyhacknews.mapper;

import com.adlternative.tinyhacknews.entity.News;
import com.adlternative.tinyhacknews.models.enums.ListAllNewsOrderEnum;
import com.adlternative.tinyhacknews.models.enums.NewsTypeEnum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface NewsMapper extends BaseMapper<News> {
  List<News> selectByAuthorId(@Param("authorId") Long authorId);

  IPage<News> selectAllInOrder(
      IPage<?> page,
      @Param("order") ListAllNewsOrderEnum order,
      @Param("news_type") NewsTypeEnum newsType,
      @Param("date") String date);
}
