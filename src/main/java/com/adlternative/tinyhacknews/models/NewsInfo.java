package com.adlternative.tinyhacknews.models;

import com.adlternative.tinyhacknews.models.output.SimpleUserInfoOutputDTO;
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
  SimpleUserInfoOutputDTO author;
  Date createdAt;
  Date updatedAt;
}
