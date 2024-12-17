package com.itm.space.notificationservice.kafka.handler.impl;

import com.itm.space.itmplatformcommonmodels.kafka.ReviewEvent;
import com.itm.space.itmplatformcommonmodels.kafka.enums.ReviewEventStatus;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.kafka.handler.ReviewEventHandler;
import com.itm.space.notificationservice.mapper.NotificationMapper;
import com.itm.space.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReviewCreateEventHandler implements ReviewEventHandler<ReviewEvent> {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper mapper;

    @Override
    public boolean isHandle(ReviewEvent event) {
        return event.status() == ReviewEventStatus.START_WAITING;
    }

    @Override
    public void handle(ReviewEvent event) {
        Notification notification = mapper.reviewEventToNotificationWithStatusStartWaiting(event);
        notificationRepository.save(notification);
    }
}
