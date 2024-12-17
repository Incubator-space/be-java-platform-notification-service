package com.itm.space.notificationservice.service;

import com.itm.space.notificationservice.api.request.NewsRequest;
import com.itm.space.notificationservice.api.response.NewsResponse;
import com.itm.space.notificationservice.domain.entity.News;
import com.itm.space.notificationservice.domain.enums.NewsStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface NewsService {
    boolean isExistByCreatedBeforeAndStatus(LocalDateTime created, NewsStatus status);

    void updateStatusForOldNews(NewsStatus newStatus,
                                LocalDateTime oneDayAgo,
                                NewsStatus currentStatus);

    void saveNews(News news);

    NewsResponse saveNewsAndReturnResponse(NewsRequest newsRequest);

    List<NewsResponse> getNews(UUID userId);

}
