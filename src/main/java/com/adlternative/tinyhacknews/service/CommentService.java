package com.adlternative.tinyhacknews.service;

import com.adlternative.tinyhacknews.entity.CommentData;
import com.adlternative.tinyhacknews.entity.SubmitCommentInputDTO;
import com.adlternative.tinyhacknews.entity.UpdateCommentInputDTO;

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
}
