package com.adlternative.tinyhacknews.service;

import com.adlternative.tinyhacknews.entity.NewsInfo;
import com.adlternative.tinyhacknews.entity.SubmitNewsInputDTO;

public interface NewsService {

  NewsInfo submit(Long userId, SubmitNewsInputDTO submitNewsInputDTO);
}
