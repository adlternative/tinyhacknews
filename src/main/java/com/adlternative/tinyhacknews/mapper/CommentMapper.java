package com.adlternative.tinyhacknews.mapper;

import com.adlternative.tinyhacknews.entity.Comment;
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
public interface CommentMapper {

  /**
   * 插入评论
   *
   * @param comment
   * @return
   */
  @Insert(
      "insert into Comments(text, parentCommentId, newsId, authorId, createdAt, updatedAt) values(#{text}, #{parentCommentId},#{newsId}, #{authorId}, #{createAt}, #{updateAt})")
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  int insert(Comment comment);

  @Delete("delete from Comments where id = #{id}")
  int delete(Long id);

  @Update(
      "update Comments set text = #{text}, parentCommentId = #{parentCommentId}, newsId = #{newsId}, authorId = #{authorId}, createdAt = #{createdAt}, updatedAt = #{updatedAt} where id = #{id}")
  int modify(Comment comment);

  @Select("select * from Comments where id = #{id}")
  Optional<Comment> selectById(Long id);

  @Select("select * from Comments where newsId = #{newsId}")
  List<Comment> selectByNewsId(Long newsId);

  /**
   * 根据父评论 id 获取子评论
   *
   * @param parentCommentId
   * @return
   */
  @Select("select * from Comments where parentCommentId = #{parentCommentId}")
  List<Comment> selectByParentCommentId(Long parentCommentId);
}
