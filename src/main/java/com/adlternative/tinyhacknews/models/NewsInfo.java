package com.adlternative.tinyhacknews.models;

import java.util.Date;
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
  Date createdAt;
  Date updatedAt;
}
