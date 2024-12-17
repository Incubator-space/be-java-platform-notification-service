package com.itm.space.notificationservice.service.impl;

import com.itm.space.notificationservice.api.enums.NewsTargetRole;
import com.itm.space.notificationservice.api.request.NewsRequest;
import com.itm.space.notificationservice.api.request.UserRequestInfo;
import com.itm.space.notificationservice.api.response.NewsResponse;
import com.itm.space.notificationservice.domain.entity.News;
import com.itm.space.notificationservice.domain.enums.NewsStatus;
import com.itm.space.notificationservice.mapper.NewsMapper;
import com.itm.space.notificationservice.repository.NewsRepository;
import com.itm.space.notificationservice.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final UserRequestInfo userRequestInfo;

    @Override
    @Transactional(readOnly = true)
    public boolean isExistByCreatedBeforeAndStatus(LocalDateTime created, NewsStatus status) {
        return newsRepository.existsByCreatedBeforeAndStatus(created, status);
    }

    @Override
    @Transactional
    public void updateStatusForOldNews(NewsStatus newStatus, LocalDateTime oneDayAgo, NewsStatus currentStatus) {
        newsRepository.updateStatusForOldNews(newStatus, oneDayAgo, currentStatus);
    }

    @Override
    public void saveNews(News news) {
        newsRepository.save(news);
    }

    @Override
    @Transactional
    public NewsResponse saveNewsAndReturnResponse(NewsRequest newsRequest) {
        News mapperNews = newsMapper.toNews(newsRequest);
        News news = newsRepository.save(mapperNews);
        return newsMapper.toNewsResponse(news);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponse> getNews(UUID userId) {
        String userRole = userRequestInfo.getUserRole();
        List<NewsTargetRole> targets = new ArrayList<>();
        if (NewsTargetRole.STUDENT.toString().equals(userRole) || NewsTargetRole.MENTOR.toString().equals(userRole)) {
            targets.add(NewsTargetRole.valueOf(userRole));
            targets.add(NewsTargetRole.ALL);
        }
        if (targets.isEmpty()) {
            return Collections.emptyList();
        }
        List<News> news = newsRepository.getNewsByTargetRoles(targets);
        return news.stream()
                .map(newsMapper::toNewsResponse)
                .collect(Collectors.toList());
    }
}
