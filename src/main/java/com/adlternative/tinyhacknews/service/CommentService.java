package com.adlternative.tinyhacknews.service;

import com.adlternative.tinyhacknews.models.input.SubmitCommentInputDTO;
import com.adlternative.tinyhacknews.models.input.UpdateCommentInputDTO;
import com.adlternative.tinyhacknews.models.output.CommentOutPutDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

public interface CommentService {

  /**
   * 提交评论
   *
   * @param submitCommentInputDTO
   * @return
   */
  CommentOutPutDTO submitComment(Long newsId, SubmitCommentInputDTO submitCommentInputDTO);

  /**
   * 删除评论
   *
   * @param id
   * @param userId
   */
  void deleteComment(Long id);

  /**
   * 修改评论
   *
   * @param id
   * @param updateCommentInputDTO
   * @param userId
   */
  CommentOutPutDTO modifyComment(Long id, UpdateCommentInputDTO updateCommentInputDTO);

  /**
   * 获取评论内容
   *
   * @param id
   * @param userId
   */
  CommentOutPutDTO getComment(Long id);

  /**
   * 获取某个新闻下的所有评论
   *
   * @param newsId
   * @param userId
   * @return
   */
  IPage<CommentOutPutDTO> getComments(Long newsId, Long pageNum, Long pageSize);

  List<CommentOutPutDTO> getSubComments(Long commentId);
}
