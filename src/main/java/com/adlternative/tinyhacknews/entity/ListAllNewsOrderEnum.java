package com.adlternative.tinyhacknews.entity;

import lombok.Getter;

@Getter
public enum ListAllNewsOrderEnum {
  /** 按照时间降序 */
  DATE_DESC,
  /** 按照时间升序 */
  DATE_ASC,
  POINT;

  @Override
  public String toString() {
    return super.toString();
  }
}
