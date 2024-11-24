package com.adlternative.tinyhacknews.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Baomidou
 * @since 2024-11-24
 */
@Getter
@Setter
@Builder
public class Users implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  private String username;

  private String password;

  private String email;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  /** 是否被删除 */
  @TableLogic private Byte isDeleted;
}
