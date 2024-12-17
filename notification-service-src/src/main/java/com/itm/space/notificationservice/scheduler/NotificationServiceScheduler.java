package com.itm.space.notificationservice.scheduler;

import com.itm.space.itmplatformcommonmodels.kafka.NotificationEvent;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.enums.NotificationStatus;
import com.itm.space.notificationservice.kafka.producer.NotificationEventProducer;
import com.itm.space.notificationservice.mapper.NotificationToNotificationEventMapper;
import com.itm.space.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationServiceScheduler {

    private final NotificationRepository notificationRepository;
    private final NotificationToNotificationEventMapper notificationToNotificationEventMapper;
    private final NotificationEventProducer notificationEventProducer;

    @Transactional
    @Scheduled(cron = "${scheduler.change-wait-notifications-to-sent-cron}")
    public void changeNotificationStatus() {
        List<Notification> notifications = notificationRepository.findWaitingNotifications();
        List<NotificationEvent> notificationEvents = notifications.stream()
                .map(notification -> {
                    notification.setStatus(NotificationStatus.SENT);
                    return notificationToNotificationEventMapper.mapToEvent(notification);
                })
                .toList();
        notificationRepository.saveAll(notifications);
        notificationEventProducer.sendBatch(notificationEvents);
    }
}
