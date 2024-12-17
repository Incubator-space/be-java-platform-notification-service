package com.itm.space.notificationservice.kafka.handler.impl;

import com.itm.space.itmplatformcommonmodels.kafka.AchievementEvent;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.kafka.handler.AchievementEventHandler;
import com.itm.space.notificationservice.mapper.NotificationMapper;
import com.itm.space.notificationservice.service.impl.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AchievementDefaultEventHandler implements AchievementEventHandler<AchievementEvent> {

    private final NotificationMapper mapper;
    private final NotificationServiceImpl notificationService;

    @Override
    public boolean isHandle(AchievementEvent event) {
        return event.progress() == event.goal();
    }

    @Override
    public void handle(AchievementEvent event) {
        Notification notification = mapper.achievementEventToNotification(event);
        notificationService.saveNotification(notification);
    }
}
