package com.adlternative.tinyhacknews.mapper;

import com.adlternative.tinyhacknews.entity.Comments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * Mapper 接口
 *
 * @author Baomidou
 * @since 2024-11-24
 */
@Mapper
@Component
public interface CommentsMapper extends BaseMapper<Comments> {
  /**
   * 根据父评论 id 获取子评论
   *
   * @param parentCommentId
   * @return
   */
  @Select("select * from comments where parent_comment_id = #{parentCommentId} and is_deleted = 0")
  List<Comments> selectByParentCommentId(@Param("parentCommentId") Long parentCommentId);

  @Select("select * from comments where news_id = #{newsId} and is_deleted = 0")
  List<Comments> selectByNewsId(@Param("newsId") Long newsId);
}
