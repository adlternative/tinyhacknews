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
public class User implements Serializable {
  private static final long serialVersionUID = -213L;
  /** 主键 */
  private Long id;

  /** 用户名 */
  private String username;

  /** 密码 */
  private String password;

  /** 邮箱 */
  private String email;

  /** 创建时间 */
  private Date createAt;

  /** 上次更新时间 */
  private Date updateAt;
}
