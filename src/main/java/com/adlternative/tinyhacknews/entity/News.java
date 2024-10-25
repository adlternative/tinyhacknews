package com.adlternative.tinyhacknews.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News {
    private Long id;
    private String title;
    private String content;
    private String author;
    private Date createdAt;
    private Date updatedAt;
}
