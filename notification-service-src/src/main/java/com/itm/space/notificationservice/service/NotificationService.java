package com.itm.space.notificationservice.service;

import com.itm.space.notificationservice.api.request.NotificationRequest;
import com.itm.space.notificationservice.api.request.ViewNotificationsRequest;
import com.itm.space.notificationservice.api.response.NotificationResponse;
import com.itm.space.notificationservice.domain.entity.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    List<NotificationResponse> getUnreads(UUID userId);

    List<NotificationResponse> getViewedNotifications(UUID userId);

    NotificationResponse viewNotification(UUID id, UUID userId);

    void saveNotification(Notification notification);
    NotificationResponse saveNotificationAndReturnResponse(NotificationRequest request);

    void viewNotifications(ViewNotificationsRequest request);

    Long getUnreadNotificationsCount(UUID userId);
}
