package com.adlternative.tinyhacknews.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class News implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  private String title;

  private String url;

  private String text;

  private Long authorId;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  /** 是否删除 */
  @TableLogic private Boolean isDeleted;
}
