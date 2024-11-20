package com.adlternative.tinyhacknews.service.impl;

import com.adlternative.tinyhacknews.entity.Comment;
import com.adlternative.tinyhacknews.entity.CommentData;
import com.adlternative.tinyhacknews.entity.News;
import com.adlternative.tinyhacknews.entity.SubmitCommentInputDTO;
import com.adlternative.tinyhacknews.entity.UpdateCommentInputDTO;
import com.adlternative.tinyhacknews.entity.User;
import com.adlternative.tinyhacknews.entity.UserInfo;
import com.adlternative.tinyhacknews.exception.CommentNotFoundException;
import com.adlternative.tinyhacknews.exception.DBException;
import com.adlternative.tinyhacknews.exception.ForbiddenException;
import com.adlternative.tinyhacknews.exception.InternalErrorException;
import com.adlternative.tinyhacknews.exception.NewsNotFoundException;
import com.adlternative.tinyhacknews.exception.UserNotFoundException;
import com.adlternative.tinyhacknews.mapper.CommentMapper;
import com.adlternative.tinyhacknews.mapper.NewsMapper;
import com.adlternative.tinyhacknews.mapper.UserMapper;
import com.adlternative.tinyhacknews.service.CommentService;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
    User user =
        userMapper
            .selectById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

    News news =
        newsMapper
            .selectById(submitCommentInputDTO.getNewsId())
            .orElseThrow(
                () ->
                    new NewsNotFoundException(
                        "News not found for id: " + submitCommentInputDTO.getNewsId()));

    if (submitCommentInputDTO.getParentCommentId() != null) {
      Comment parentComment =
          commentMapper
              .selectById(submitCommentInputDTO.getParentCommentId())
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

    Date date = new Date();
    Comment comment =
        Comment.builder()
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
    // TODO: 软删除 || 删除所有子评论

    User user =
        userMapper
            .selectById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

    Comment comment =
        commentMapper
            .selectById(id)
            .orElseThrow(() -> new CommentNotFoundException("Comment not found for id: " + id));

    if (!Objects.equals(comment.getAuthorId(), user.getId())) {
      throw new ForbiddenException("You do not have permission to delete this comment.");
    }
    try {
      int affectedRows = commentMapper.delete(id);
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
    User user =
        userMapper
            .selectById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

    Comment comment =
        commentMapper
            .selectById(id)
            .orElseThrow(() -> new CommentNotFoundException("Comment not found for id: " + id));

    if (!Objects.equals(comment.getAuthorId(), user.getId())) {
      throw new ForbiddenException("You do not have permission to modify this comment.");
    }
    comment.setText(updateCommentInputDTO.getText());
    comment.setUpdatedAt(new Date());

    try {
      int affectedRows = commentMapper.modify(comment);
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
    Comment comment =
        commentMapper
            .selectById(id)
            .orElseThrow(() -> new CommentNotFoundException("Comment not found for id: " + id));
    User user =
        userMapper
            .selectById(comment.getAuthorId())
            .orElseThrow(
                () -> new UserNotFoundException("User not found for id: " + comment.getAuthorId()));

    return CommentData.convertFrom(comment, user);
  }

  @Override
  public List<CommentData> getComments(Long newsId, Long userId) {
    // TODO: userId 用于权限检查

    // 证明新闻存在
    News news =
        newsMapper
            .selectById(newsId)
            .orElseThrow(() -> new NewsNotFoundException("News not found for id: " + newsId));
    // TODO: 一条 sql, join 一下就好
    return commentMapper.selectByNewsId(news.getId()).stream()
        .map(
            comment -> {
              User user =
                  userMapper
                      .selectById(comment.getAuthorId())
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
    Comment parentComment =
        commentMapper
            .selectById(commentId)
            .orElseThrow(
                () ->
                    new CommentNotFoundException("Parent comment not found for id: " + commentId));

    return commentMapper.selectByParentCommentId(parentComment.getId()).stream()
        .map(
            comment -> {
              User user =
                  userMapper
                      .selectById(comment.getAuthorId())
                      .orElseThrow(
                          () ->
                              new UserNotFoundException(
                                  "User not found for id: " + comment.getAuthorId()));
              return CommentData.convertFrom(comment, user);
            })
        .collect(Collectors.toList());
  }

  private final CommentMapper commentMapper;
  private final NewsMapper newsMapper;
  private final UserMapper userMapper;
}
