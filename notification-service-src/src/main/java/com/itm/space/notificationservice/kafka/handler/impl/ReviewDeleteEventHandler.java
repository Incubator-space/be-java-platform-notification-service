package com.itm.space.notificationservice.kafka.handler.impl;

import com.itm.space.itmplatformcommonmodels.kafka.ReviewEvent;
import com.itm.space.itmplatformcommonmodels.kafka.enums.ReviewEventStatus;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.kafka.handler.ReviewEventHandler;
import com.itm.space.notificationservice.mapper.NotificationMapper;
import com.itm.space.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewDeleteEventHandler implements ReviewEventHandler<ReviewEvent> {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper mapper;

    @Override
    public boolean isHandle(ReviewEvent event) {
        return event.status() == ReviewEventStatus.CANCELED;
    }

    @Override
    public void handle(ReviewEvent event) {
        List<Notification> notifications = new ArrayList<>();
        notifications.add(mapper.reviewEventToNotificationWithStatusCanceledStudent(event));
        notifications.add(mapper.reviewEventToNotificationWithStatusCanceledMentor(event));
        notificationRepository.saveAll(notifications);
    }
}
