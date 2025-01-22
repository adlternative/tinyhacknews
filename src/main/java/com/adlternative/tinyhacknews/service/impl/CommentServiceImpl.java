package com.adlternative.tinyhacknews.service.impl;

import com.adlternative.tinyhacknews.context.RequestContext;
import com.adlternative.tinyhacknews.entity.Comments;
import com.adlternative.tinyhacknews.entity.News;
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
import com.adlternative.tinyhacknews.models.UserInfo;
import com.adlternative.tinyhacknews.models.input.SubmitCommentInputDTO;
import com.adlternative.tinyhacknews.models.input.UpdateCommentInputDTO;
import com.adlternative.tinyhacknews.models.output.CommentOutPutDTO;
import com.adlternative.tinyhacknews.models.output.CommentWithNewsMetaOutPutDTO;
import com.adlternative.tinyhacknews.service.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
  /**
   * 提交评论
   *
   * @param newsId
   * @param submitCommentInputDTO
   * @return
   */
  @Override
  public CommentOutPutDTO submitComment(Long newsId, SubmitCommentInputDTO submitCommentInputDTO) {
    Long userId = RequestContext.getUserId();
    Users user =
        Optional.ofNullable(userMapper.selectById(userId))
            .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

    News news =
        Optional.ofNullable(newsMapper.selectById(newsId))
            .orElseThrow(() -> new NewsNotFoundException("News not found for id: " + newsId));

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

    Date date = new Date();
    Comments comment =
        new Comments()
            .setText(submitCommentInputDTO.getText())
            .setAuthorId(userId)
            .setNewsId(news.getId())
            .setParentCommentId(submitCommentInputDTO.getParentCommentId())
            .setCreatedAt(date)
            .setUpdatedAt(date);
    try {
      int affectedRows = commentMapper.insert(comment);
      if (affectedRows == 0) {
        throw new DBException("Failed to insert comment, affectedRows equals to zero");
      }

      return CommentOutPutDTO.convertFrom(comment, user);
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
   */
  @Override
  public void deleteComment(Long id) {
    Long userId = RequestContext.getUserId();
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
   */
  @Override
  public CommentOutPutDTO modifyComment(Long id, UpdateCommentInputDTO updateCommentInputDTO) {
    Long userId = RequestContext.getUserId();
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
    comment.setUpdatedAt(new Date());

    try {
      int affectedRows = commentMapper.update(comment, new QueryWrapper<Comments>().eq("id", id));
      if (affectedRows == 0) {
        throw new DBException("Failed to modify comment, affectedRows equals to zero");
      }
      return CommentOutPutDTO.convertFrom(comment, user);
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
  public CommentOutPutDTO getComment(Long id) {
    // TODO: 一条 sql, join 一下就好
    Comments comment =
        Optional.ofNullable(commentMapper.selectById(id))
            .orElseThrow(() -> new CommentNotFoundException("Comment not found for id: " + id));
    Users user =
        Optional.ofNullable(userMapper.selectById(comment.getAuthorId()))
            .orElseThrow(
                () -> new UserNotFoundException("User not found for id: " + comment.getAuthorId()));

    return CommentOutPutDTO.convertFrom(comment, user);
  }

  @Override
  public IPage<CommentOutPutDTO> getComments(Long newsId, Long pageNum, Long pageSize) {
    // 证明新闻存在
    News news =
        Optional.ofNullable(newsMapper.selectById(newsId))
            .orElseThrow(() -> new NewsNotFoundException("News not found for id: " + newsId));
    // TODO: 一条 sql, join 一下就好
    return commentMapper
        .selectPage(
            new Page<>(pageNum, pageSize), new QueryWrapper<Comments>().eq("news_id", news.getId()))
        .convert(
            comment -> {
              Users user =
                  Optional.ofNullable(userMapper.selectById(comment.getAuthorId()))
                      .orElseThrow(
                          () ->
                              new UserNotFoundException(
                                  "User not found for id: " + comment.getAuthorId()));
              return CommentOutPutDTO.convertFrom(comment, user);
            });
  }

  @Override
  public List<CommentOutPutDTO> getSubComments(Long commentId) {
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
              return CommentOutPutDTO.convertFrom(comment, user);
            })
        .collect(Collectors.toList());
  }

  @Override
  public IPage<CommentWithNewsMetaOutPutDTO> getAllComments(
      Long pageNum, Long pageSize, String username) {
    QueryWrapper<Comments> query = new QueryWrapper<>();
    query.orderByDesc("created_at");
    if (StringUtils.isNotEmpty(username)) {
      UserInfo userInfo =
          new UserInfo(
              userMapper
                  .findByUserName(username)
                  .orElseThrow(
                      (() -> new UserNotFoundException("User not found for name: " + username))));
      query.eq("author_id", userInfo.getId());
    }

    return commentMapper
        .selectPage(new Page<>(pageNum, pageSize), query)
        .convert(
            comment -> {
              Users user =
                  Optional.ofNullable(userMapper.selectById(comment.getAuthorId()))
                      .orElseThrow(
                          () ->
                              new UserNotFoundException(
                                  "User not found for id: " + comment.getAuthorId()));
              News news =
                  Optional.ofNullable(newsMapper.selectById(comment.getNewsId()))
                      .orElseThrow(
                          () ->
                              new NewsNotFoundException(
                                  "News not found for id: " + comment.getNewsId()));
              return CommentWithNewsMetaOutPutDTO.from(comment, news, user);
            });
  }

  @Override
  public Long getCommentCount(Long newsId) {
    return commentMapper.selectCount(new QueryWrapper<Comments>().eq("news_id", newsId));
  }

  private final CommentsMapper commentMapper;
  private final NewsMapper newsMapper;
  private final UsersMapper userMapper;
}
