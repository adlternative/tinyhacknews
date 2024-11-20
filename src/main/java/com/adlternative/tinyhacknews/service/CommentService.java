package com.adlternative.tinyhacknews.service;

import com.adlternative.tinyhacknews.entity.CommentData;
import com.adlternative.tinyhacknews.entity.SubmitCommentInputDTO;
import com.adlternative.tinyhacknews.entity.UpdateCommentInputDTO;
import java.util.List;

public interface CommentService {

  /**
   * 提交评论
   *
   * @param submitCommentInputDTO
   * @param userId
   * @return
   */
  CommentData submitComment(SubmitCommentInputDTO submitCommentInputDTO, Long userId);

  /**
   * 删除评论
   *
   * @param id
   * @param userId
   */
  void deleteComment(Long id, Long userId);

  /**
   * 修改评论
   *
   * @param id
   * @param updateCommentInputDTO
   * @param userId
   */
  CommentData modifyComment(Long id, UpdateCommentInputDTO updateCommentInputDTO, Long userId);

  /**
   * 获取评论内容
   *
   * @param id
   * @param userId
   */
  CommentData getComment(Long id, Long userId);

  /**
   * 获取某个新闻下的所有评论
   *
   * @param newsId
   * @param userId
   * @return
   */
  List<CommentData> getComments(Long newsId, Long userId);
}
