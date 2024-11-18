package com.adlternative.tinyhacknews.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment implements Serializable {
  private static final long serialVersionUID = -214L;
  /** 主键 */
  private Long id;

  /** 内容 */
  private String text;

  /** 父评论id */
  private Long parentCommentId;

  /** 新闻id */
  private Long newsId;

  /** 作者id */
  private Long authorId;

  /** 创建时间 */
  private Date createdAt;

  /** 上次更新时间 */
  private Date updatedAt;
}
