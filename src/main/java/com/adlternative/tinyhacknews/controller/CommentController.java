package com.adlternative.tinyhacknews.controller;

import com.adlternative.tinyhacknews.entity.CommentData;
import com.adlternative.tinyhacknews.entity.SubmitCommentInputDTO;
import com.adlternative.tinyhacknews.entity.UpdateCommentInputDTO;
import com.adlternative.tinyhacknews.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
  /**
   * 提交评论
   *
   * @param submitCommentInputDTO
   * @param userId
   * @return
   */
  @PostMapping
  public CommentData submitComment(
      @RequestBody SubmitCommentInputDTO submitCommentInputDTO,
      @RequestParam(name = "user_id") Long userId) {
    return commentService.submitComment(submitCommentInputDTO, userId);
  }

  /**
   * 删除评论
   *
   * @param id
   * @param userId
   */
  @DeleteMapping("/{id}")
  public void deleteComment(@PathVariable Long id, @RequestParam(name = "user_id") Long userId) {
    commentService.deleteComment(id, userId);
  }

  /**
   * 修改评论
   *
   * @param id
   * @param updateCommentInputDTO
   * @param userId
   * @return
   */
  @PutMapping("/{id}")
  public CommentData modifyComment(
      @PathVariable Long id,
      @RequestBody UpdateCommentInputDTO updateCommentInputDTO,
      @RequestParam(name = "user_id") Long userId) {
    return commentService.modifyComment(id, updateCommentInputDTO, userId);
  }

  /**
   * 获取评论
   *
   * @param id
   * @param userId
   * @return
   */
  @GetMapping("/{id}")
  public CommentData getComment(
      @PathVariable Long id, @RequestParam(name = "user_id") Long userId) {
    return commentService.getComment(id, userId);
  }

  private final CommentService commentService;
}
