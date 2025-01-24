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
 * 投票
 *
 * @author adlternative
 * @since 2025-01-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("votes")
public class Votes implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @TableField("user_id")
  private Long userId;

  @TableField("item_id")
  private Long itemId;

  @TableField("item_type")
  private String itemType;

  @TableField("created_at")
  private Date createdAt;

  @TableField("updated_at")
  private Date updatedAt;

  /** 是否删除 */
  @TableField("is_deleted")
  @TableLogic
  private Boolean deleted;
}
