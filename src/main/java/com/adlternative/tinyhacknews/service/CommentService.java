package com.adlternative.tinyhacknews.service;

import com.adlternative.tinyhacknews.models.input.SubmitCommentInputDTO;
import com.adlternative.tinyhacknews.models.input.UpdateCommentInputDTO;
import com.adlternative.tinyhacknews.models.output.CommentOutPutDTO;
import com.adlternative.tinyhacknews.models.output.CommentWithNewsMetaOutPutDTO;
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
   */
  void deleteComment(Long id);

  /**
   * 修改评论
   *
   * @param id
   * @param updateCommentInputDTO
   */
  CommentOutPutDTO modifyComment(Long id, UpdateCommentInputDTO updateCommentInputDTO);

  /**
   * 获取评论内容
   *
   * @param id
   */
  CommentOutPutDTO getComment(Long id);

  /**
   * 获取某个新闻下的所有评论
   *
   * @param newsId
   * @return
   */
  List<CommentOutPutDTO> getComments(Long newsId);

  List<CommentOutPutDTO> getSubComments(Long commentId);

  IPage<CommentWithNewsMetaOutPutDTO> getAllComments(Long pageNum, Long pageSize, String userName);

  /**
   * 获取某个新闻下的评论数量
   *
   * @param newsId
   * @return
   */
  //
  Long getCommentCount(Long newsId);

  void vote(Long commentId);

  void unvote(Long commentId);

  Long getVoteCount(Long commentId);
}
