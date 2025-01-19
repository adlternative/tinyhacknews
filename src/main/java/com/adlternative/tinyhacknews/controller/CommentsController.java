package com.adlternative.tinyhacknews.controller;

import com.adlternative.tinyhacknews.models.input.SubmitCommentInputDTO;
import com.adlternative.tinyhacknews.models.input.UpdateCommentInputDTO;
import com.adlternative.tinyhacknews.models.output.CommentOutPutDTO;
import com.adlternative.tinyhacknews.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentsController {
  /**
   * 提交评论
   *
   * @param submitCommentInputDTO
   * @return
   */
  @PostMapping
  public CommentOutPutDTO submitComment(@RequestBody SubmitCommentInputDTO submitCommentInputDTO) {
    return commentService.submitComment(submitCommentInputDTO);
  }

  /**
   * 删除评论
   *
   * @param id
   */
  @DeleteMapping("/{id}")
  public void deleteComment(@PathVariable Long id) {
    commentService.deleteComment(id);
  }

  /**
   * 修改评论
   *
   * @param id
   * @param updateCommentInputDTO
   * @return
   */
  @PutMapping("/{id}")
  public CommentOutPutDTO modifyComment(
      @PathVariable Long id, @RequestBody UpdateCommentInputDTO updateCommentInputDTO) {
    return commentService.modifyComment(id, updateCommentInputDTO);
  }

  /**
   * 获取评论
   *
   * @param id
   * @return
   */
  @GetMapping("/{id}")
  public CommentOutPutDTO getComment(@PathVariable Long id) {
    return commentService.getComment(id);
  }

  /**
   * 获取某个评论的所有子评论
   *
   * @param commentId
   * @return
   */
  @GetMapping("/{id}/sub_comments")
  public List<CommentOutPutDTO> getSubComments(@PathVariable(value = "id") Long commentId) {
    return commentService.getSubComments(commentId);
  }

  private final CommentService commentService;
}
