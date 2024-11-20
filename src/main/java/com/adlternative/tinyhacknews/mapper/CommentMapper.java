package com.adlternative.tinyhacknews.mapper;

import com.adlternative.tinyhacknews.entity.Comment;
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
public interface CommentMapper {

  /**
   * 插入评论
   *
   * @param comment
   * @return
   */
  @Insert(
      "insert into Comments(text, parentCommentId, newsId, authorId, createdAt, updatedAt) values(#{text}, #{parentCommentId},#{newsId}, #{authorId}, #{createdAt}, #{updatedAt})")
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  int insert(Comment comment);

  @Update("update Comments set is_deleted = 1 where id = #{id}")
  int delete(@Param("id") Long id);

  @Update(
      "update Comments set text = #{text}, parentCommentId = #{parentCommentId}, newsId = #{newsId}, authorId = #{authorId}, createdAt = #{createdAt}, updatedAt = #{updatedAt} where id = #{id} and is_deleted = 0")
  int modify(Comment comment);

  @Select("select * from Comments where id = #{id} and is_deleted = 0")
  Optional<Comment> selectById(@Param("id") Long id);

  @Select("select * from Comments where newsId = #{newsId} and is_deleted = 0")
  List<Comment> selectByNewsId(@Param("newsId") Long newsId);

  /**
   * 根据父评论 id 获取子评论
   *
   * @param parentCommentId
   * @return
   */
  @Select("select * from Comments where parentCommentId = #{parentCommentId} and is_deleted = 0")
  List<Comment> selectByParentCommentId(@Param("parentCommentId") Long parentCommentId);
}
