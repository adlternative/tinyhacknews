package com.adlternative.tinyhacknews.service.impl;

import com.adlternative.tinyhacknews.context.RequestContext;
import com.adlternative.tinyhacknews.entity.Comments;
import com.adlternative.tinyhacknews.entity.News;
import com.adlternative.tinyhacknews.entity.Users;
import com.adlternative.tinyhacknews.entity.Votes;
import com.adlternative.tinyhacknews.exception.CommentNotFoundException;
import com.adlternative.tinyhacknews.exception.DBException;
import com.adlternative.tinyhacknews.exception.ForbiddenException;
import com.adlternative.tinyhacknews.exception.InternalErrorException;
import com.adlternative.tinyhacknews.exception.NewsNotFoundException;
import com.adlternative.tinyhacknews.exception.UserNotFoundException;
import com.adlternative.tinyhacknews.mapper.CommentsMapper;
import com.adlternative.tinyhacknews.mapper.NewsMapper;
import com.adlternative.tinyhacknews.mapper.UsersMapper;
import com.adlternative.tinyhacknews.mapper.VotesMapper;
import com.adlternative.tinyhacknews.models.UserInfo;
import com.adlternative.tinyhacknews.models.enums.VoteItemTypeEnum;
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
    Users user = getUserByUserId(userId);
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
    Users user = getUserByUserId(userId);
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
    Users user = getUserByUserId(userId);

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
    Users user = getUserByUserId(comment.getAuthorId());
    return CommentOutPutDTO.convertFrom(comment, user);
  }

  @Override
  public List<CommentOutPutDTO> getComments(Long newsId) {
    // 证明新闻存在
    News news =
        Optional.ofNullable(newsMapper.selectById(newsId))
            .orElseThrow(() -> new NewsNotFoundException("News not found for id: " + newsId));
    // TODO: 一条 sql, join 一下就好
    return commentMapper.selectByNewsId(news.getId()).stream()
        .map(
            comment ->
                CommentOutPutDTO.convertFrom(comment, getUserByUserId(comment.getAuthorId())))
        .collect(Collectors.toList());
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
            comment ->
                CommentOutPutDTO.convertFrom(comment, getUserByUserId(comment.getAuthorId())))
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
              Users user = getUserByUserId(comment.getAuthorId());
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

  @Override
  public void vote(Long commentId) {
    // 如果已经投票过，则不允许重复投票
    if (votesMapper.selectCount(
            new QueryWrapper<Votes>()
                .eq("item_id", commentId)
                .eq("item_type", VoteItemTypeEnum.COMMENT.name())
                .eq("user_id", RequestContext.getUserId()))
        > 0) {
      throw new ForbiddenException("You have already voted for this comment.");
    }

    // 如果这条新闻的作者是当前用户，则不允许投票
    if (Objects.equals(
        commentMapper.selectById(commentId).getAuthorId(), RequestContext.getUserId())) {
      throw new ForbiddenException("You cannot vote for your own comment.");
    }

    // 插入投票记录
    Date now = new Date();
    votesMapper.insert(
        new Votes()
            .setUserId(RequestContext.getUserId())
            .setItemId(commentId)
            .setItemType(VoteItemTypeEnum.COMMENT.name())
            .setUpdatedAt(now)
            .setCreatedAt(now));
  }

  @Override
  public void unvote(Long commentId) {
    try {
      // 如果没有投票过，则不允许取消投票
      Votes votes =
          votesMapper.selectOne(
              new QueryWrapper<Votes>()
                  .eq("item_id", commentId)
                  .eq("item_type", VoteItemTypeEnum.COMMENT.name())
                  .eq("user_id", RequestContext.getUserId()));
      if (votes == null) {
        throw new ForbiddenException("You have not voted for this comment.");
      }

      votesMapper.deleteById(votes.getId());
    } catch (Exception e) {
      log.error("Failed to unvote comment", e);
      throw new InternalErrorException("Failed to unvote comment", e);
    }
  }

  @Override
  public Long getVoteCount(Long commentId) {
    return votesMapper.selectCount(
        new QueryWrapper<Votes>()
            .eq("item_id", commentId)
            .eq("item_type", VoteItemTypeEnum.COMMENT.name()));
  }

  private Users getUserByUserId(Long userId) {
    return Optional.ofNullable(userMapper.selectById(userId))
        .orElseThrow(() -> new UserNotFoundException("Failed to get user, user not found"));
  }

  private final CommentsMapper commentMapper;
  private final NewsMapper newsMapper;
  private final UsersMapper userMapper;
  private final VotesMapper votesMapper;
}
