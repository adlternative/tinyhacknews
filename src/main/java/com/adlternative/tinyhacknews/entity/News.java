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
 * @since 2025-01-20
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("news")
public class News implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @TableField("title")
  private String title;

  @TableField("url")
  private String url;

  @TableField("text")
  private String text;

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

  /** 新闻类型 */
  @TableField("news_type")
  private String newsType;
}
