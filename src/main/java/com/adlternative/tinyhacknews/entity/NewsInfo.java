package com.adlternative.tinyhacknews.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class NewsInfo {
  Long id;
  UserInfo author;
  LocalDateTime createdAt;
  LocalDateTime updatedAt;
}
