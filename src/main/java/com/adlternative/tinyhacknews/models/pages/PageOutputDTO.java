package com.adlternative.tinyhacknews.models.pages;

import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;
import lombok.Data;

@Data
public class PageOutputDTO<T> {
  private List<T> list;
  private PagePara page;

  public static <T> PageOutputDTO<T> from(IPage<T> pageData) {
    PageOutputDTO<T> dto = new PageOutputDTO<>();
    dto.setList(pageData.getRecords());
    dto.setPage(
        new PagePara(
            pageData.getCurrent(), pageData.getSize(), pageData.getTotal(), pageData.getPages()));
    return dto;
  }
}
