package com.adlternative.tinyhacknews.models.pages;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagePara {
  // 现在在第几页
  private Long currentPageNum;

  // 每页大小
  private Long pageSize;

  // 总数
  private Long totalCount;

  // 总页数
  private Long pagesCount;
}
