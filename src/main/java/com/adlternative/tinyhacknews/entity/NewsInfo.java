package com.adlternative.tinyhacknews.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewsInfo {
  Long id;
  UserInfo author;
}
