package com.adlternative.tinyhacknews.service.impl;

import com.adlternative.tinyhacknews.entity.CommentData;
import com.adlternative.tinyhacknews.entity.Comments;
import com.adlternative.tinyhacknews.entity.News;
import com.adlternative.tinyhacknews.entity.SubmitCommentInputDTO;
import com.adlternative.tinyhacknews.entity.UpdateCommentInputDTO;
import com.adlternative.tinyhacknews.entity.UserInfo;
import com.adlternative.tinyhacknews.entity.Users;
import com.adlternative.tinyhacknews.exception.CommentNotFoundException;
import com.adlternative.tinyhacknews.exception.DBException;
import com.adlternative.tinyhacknews.exception.ForbiddenException;
import com.adlternative.tinyhacknews.exception.InternalErrorException;
import com.adlternative.tinyhacknews.exception.NewsNotFoundException;
import com.adlternative.tinyhacknews.exception.UserNotFoundException;
import com.adlternative.tinyhacknews.mapper.CommentsMapper;
import com.adlternative.tinyhacknews.mapper.NewsMapper;
import com.adlternative.tinyhacknews.mapper.UsersMapper;
import com.adlternative.tinyhacknews.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
  /**
   * 提交评论
   *
   * @param submitCommentInputDTO
   * @param userId
   * @return
   */
  @Override
  public CommentData submitComment(SubmitCommentInputDTO submitCommentInputDTO, Long userId) {
    Users user =
        Optional.ofNullable(userMapper.selectById(userId))
            .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

    News news =
        Optional.ofNullable(newsMapper.selectById(submitCommentInputDTO.getNewsId()))
            .orElseThrow(
                () ->
                    new NewsNotFoundException(
                        "News not found for id: " + submitCommentInputDTO.getNewsId()));

    if (submitCommentInputDTO.getParentCommentId() != null) {
      Comments parentComment =
          Optional.ofNullable(commentMapper.selectById(submitCommentInputDTO.getParentCommentId()))
              .orElseThrow(
                  () ->
                      new CommentNotFoundException(
                          "Parent comment not found for id: "
                              + submitCommentInputDTO.getParentCommentId()));
      if (!parentComment.getNewsId().equals(news.getId())) {
        throw new InternalErrorException(
            "Parent comment news id not equals to your comment news id");
      }
    }

    LocalDateTime date = LocalDateTime.now();
    Comments comment =
        Comments.builder()
            .text(submitCommentInputDTO.getText())
            .authorId(userId)
            .newsId(news.getId())
            .parentCommentId(submitCommentInputDTO.getParentCommentId())
            .createdAt(date)
            .updatedAt(date)
            .build();
    try {
      int affectedRows = commentMapper.insert(comment);
      if (affectedRows == 0) {
        throw new DBException("Failed to insert comment, affectedRows equals to zero");
      }

      return CommentData.builder()
          .id(comment.getId())
          .newsId(news.getId())
          .parentCommentId(comment.getParentCommentId())
          .author(UserInfo.convertFrom(user))
          .text(comment.getText())
          .createdAt(date)
          .updatedAt(date)
          .build();
    } catch (DBException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalErrorException("Submit comment failed", e);
    }
  }

  /**
   * 删除评论
   *
   * @param id
   * @param userId
   */
  @Override
  public void deleteComment(Long id, Long userId) {
    Users user =
        Optional.ofNullable(userMapper.selectById(userId))
            .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

    Comments comment =
        Optional.ofNullable(commentMapper.selectById(id))
            .orElseThrow(() -> new CommentNotFoundException("Comment not found for id: " + id));

    if (!Objects.equals(comment.getAuthorId(), user.getId())) {
      throw new ForbiddenException("You do not have permission to delete this comment.");
    }
    try {
      int affectedRows = commentMapper.delete(new QueryWrapper<Comments>().eq("id", id));
      if (affectedRows == 0) {
        throw new DBException("Failed to delete comment, affectedRows equals to zero");
      }

    } catch (DBException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalErrorException("Delete comment failed", e);
    }
  }

  /**
   * 修改评论
   *
   * @param id
   * @param updateCommentInputDTO
   * @param userId
   */
  @Override
  public CommentData modifyComment(
      Long id, UpdateCommentInputDTO updateCommentInputDTO, Long userId) {
    Users user =
        Optional.ofNullable(userMapper.selectById(userId))
            .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

    Comments comment =
        Optional.ofNullable(commentMapper.selectById(id))
            .orElseThrow(() -> new CommentNotFoundException("Comment not found for id: " + id));

    if (!Objects.equals(comment.getAuthorId(), user.getId())) {
      throw new ForbiddenException("You do not have permission to modify this comment.");
    }
    comment.setText(updateCommentInputDTO.getText());
    comment.setUpdatedAt(LocalDateTime.now());

    try {
      int affectedRows = commentMapper.update(comment, new QueryWrapper<Comments>().eq("id", id));
      if (affectedRows == 0) {
        throw new DBException("Failed to modify comment, affectedRows equals to zero");
      }
      return CommentData.builder()
          .id(comment.getId())
          .author(UserInfo.convertFrom(user))
          .createdAt(comment.getCreatedAt())
          .updatedAt(comment.getUpdatedAt())
          .text(comment.getText())
          .parentCommentId(comment.getParentCommentId())
          .newsId(comment.getNewsId())
          .build();
    } catch (DBException e) {
      throw e;
    } catch (Exception e) {
      throw new InternalErrorException("Modify comment failed", e);
    }
  }

  /**
   * 获取评论内容
   *
   * @param id
   * @param userId
   */
  @Override
  public CommentData getComment(Long id, Long userId) {
    // TODO: 一条 sql, join 一下就好
    Comments comment =
        Optional.ofNullable(commentMapper.selectById(id))
            .orElseThrow(() -> new CommentNotFoundException("Comment not found for id: " + id));
    Users user =
        Optional.ofNullable(userMapper.selectById(comment.getAuthorId()))
            .orElseThrow(
                () -> new UserNotFoundException("User not found for id: " + comment.getAuthorId()));

    return CommentData.convertFrom(comment, user);
  }

  @Override
  public List<CommentData> getComments(Long newsId, Long userId) {
    // TODO: userId 用于权限检查

    // 证明新闻存在
    News news =
        Optional.ofNullable(newsMapper.selectById(newsId))
            .orElseThrow(() -> new NewsNotFoundException("News not found for id: " + newsId));
    // TODO: 一条 sql, join 一下就好
    return commentMapper.selectByNewsId(news.getId()).stream()
        .map(
            comment -> {
              Users user =
                  Optional.ofNullable(userMapper.selectById(comment.getAuthorId()))
                      .orElseThrow(
                          () ->
                              new UserNotFoundException(
                                  "User not found for id: " + comment.getAuthorId()));
              return CommentData.convertFrom(comment, user);
            })
        .collect(Collectors.toList());
  }

  @Override
  public List<CommentData> getSubComments(Long commentId, Long userId) {
    // TODO: userId 用于权限检查

    // 证明评论存在
    Comments parentComment =
        Optional.ofNullable(commentMapper.selectById(commentId))
            .orElseThrow(
                () ->
                    new CommentNotFoundException("Parent comment not found for id: " + commentId));

    return commentMapper.selectByParentCommentId(parentComment.getId()).stream()
        .map(
            comment -> {
              Users user =
                  Optional.ofNullable(userMapper.selectById(comment.getAuthorId()))
                      .orElseThrow(
                          () ->
                              new UserNotFoundException(
                                  "User not found for id: " + comment.getAuthorId()));
              return CommentData.convertFrom(comment, user);
            })
        .collect(Collectors.toList());
  }

  private final CommentsMapper commentMapper;
  private final NewsMapper newsMapper;
  private final UsersMapper userMapper;
}
