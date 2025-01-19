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
@TableName("users")
public class Users implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @TableField("username")
  private String username;

  @TableField("password")
  private String password;

  @TableField("email")
  private String email;

  @TableField("created_at")
  private Date createdAt;

  @TableField("updated_at")
  private Date updatedAt;

  /** 是否被删除 */
  @TableField("is_deleted")
  @TableLogic
  private Byte isDeleted;

  /** 个人说明 */
  @TableField("about")
  private String about;
}
