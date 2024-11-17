package com.adlternative.tinyhacknews.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class NewsData extends NewsInfo {
  private String title;
  private String url;
  private String text;
}
