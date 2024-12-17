package com.itm.space.notificationservice.kafka.handler.impl;

import com.itm.space.itmplatformcommonmodels.kafka.DirectionEvent;
import com.itm.space.itmplatformcommonmodels.kafka.enums.Status;
import com.itm.space.notificationservice.domain.entity.News;
import com.itm.space.notificationservice.kafka.handler.DirectionEventHandler;
import com.itm.space.notificationservice.mapper.NewsMapper;
import com.itm.space.notificationservice.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DirectionNewEventHandler implements DirectionEventHandler<DirectionEvent> {

    private final NewsMapper newsMapper;
    private final NewsService newsService;

    @Override
    public boolean isHandle(DirectionEvent event) {
        return event.status() == Status.NEW;
    }

    @Override
    public void handle(DirectionEvent event) {
        News news = newsMapper.newDirectionEventToNews(event);
        newsService.saveNews(news);
    }
}