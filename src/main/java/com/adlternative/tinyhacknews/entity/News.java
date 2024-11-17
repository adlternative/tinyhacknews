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
public class News implements Serializable {
  private static final long serialVersionUID = -214L;
  /** 主键 */
  private Long id;

  /** 标题 */
  private String title;

  /** 链接 */
  private String url;

  /** 内容 */
  private String text;

  /** 作者id */
  private Long authorId;

  /** 创建时间 */
  private Date createAt;

  /** 上次更新时间 */
  private Date updateAt;
}
