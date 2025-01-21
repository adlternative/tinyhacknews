package com.adlternative.tinyhacknews.controller;

import com.adlternative.tinyhacknews.models.input.UpdateCommentInputDTO;
import com.adlternative.tinyhacknews.models.output.CommentOutPutDTO;
import com.adlternative.tinyhacknews.models.output.CommentWithNewsMetaOutPutDTO;
import com.adlternative.tinyhacknews.service.CommentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentsController {
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

  // 分页获取所有评论
  @GetMapping("/all")
  public IPage<CommentWithNewsMetaOutPutDTO> getComments(
      @RequestParam(name = "page_num", defaultValue = "1") Long pageNum,
      @RequestParam(name = "page_size", defaultValue = "10") Long pageSize,
      @RequestParam(name = "user_name", defaultValue = "", required = false) String userName) {
    return commentService.getAllComments(pageNum, pageSize, userName);
  }

  private final CommentService commentService;
}
