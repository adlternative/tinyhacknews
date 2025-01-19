package com.adlternative.tinyhacknews.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author adlternative
 * @since 2025-01-19
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("comments")
public class Comments implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @TableField("text")
  private String text;

  @TableField("parent_comment_id")
  private Long parentCommentId;

  @TableField("news_id")
  private Long newsId;

  @TableField("author_id")
  private Long authorId;

  @TableField("created_at")
  private Date createdAt;

  @TableField("updated_at")
  private Date updatedAt;

  /** 是否删除 */
  @TableField("is_deleted")
  @TableLogic
  private Boolean deleted;
}
