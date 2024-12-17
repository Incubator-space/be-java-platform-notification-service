package com.itm.space.notificationservice.service.impl;

import com.itm.space.notificationservice.api.enums.NewsTargetRole;
import com.itm.space.notificationservice.api.request.NotificationRequest;
import com.itm.space.notificationservice.api.request.UserRequestInfo;
import com.itm.space.notificationservice.api.request.ViewNotificationsRequest;
import com.itm.space.notificationservice.api.response.NotificationResponse;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.exception.ConflictException;
import com.itm.space.notificationservice.exception.NotFoundException;
import com.itm.space.notificationservice.exception.PlatformException;
import com.itm.space.notificationservice.mapper.NotificationMapper;
import com.itm.space.notificationservice.repository.NewsRepository;
import com.itm.space.notificationservice.repository.NotificationRepository;
import com.itm.space.notificationservice.repository.UserRepository;
import com.itm.space.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.itm.space.notificationservice.api.constants.RoleConstains.ADMIN_AND_MODERATOR_ROLES;
import static com.itm.space.notificationservice.enums.NotificationStatus.VIEWED;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;
    private final UserRequestInfo userRequestInfo;
    private final NewsRepository newsRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreads(UUID userId) {
        if (ADMIN_AND_MODERATOR_ROLES.contains(userRequestInfo.getUserRole())) {
            return Collections.emptyList();
        } else {
            if (!userRepository.existsById(userId)) {
                throw new PlatformException("User с UUID " + userId + " не найден", HttpStatus.NOT_FOUND);
            }
            List<Notification> notifications = notificationRepository.getAllNotifications(userId);
            return notifications.stream()
                    .map(notificationMapper::toNotificationResponse)
                    .toList();
        }
    }

    @Override
    public List<NotificationResponse> getViewedNotifications(UUID userId) {
        List<Notification> viewedNotifications = notificationRepository.getViewedNotifications(userId);
        return viewedNotifications.stream().map(notificationMapper::toNotificationResponse).toList();
    }

    @Override
    @Transactional
    public NotificationResponse viewNotification(UUID id, UUID userId) {
        return notificationRepository.findById(id)
                .map(notification -> {
                    if(Objects.equals(notification.getTarget(), userId)) {
                        notificationRepository.updateNotificationStatus(id, VIEWED);
                        return notification;
                    }else throw new ConflictException("Viewing the notification is forbidden to the user with id: " + userId);
                })
                .map(notificationMapper::toNotificationResponse)
                .orElseThrow(() -> new NotFoundException("Notification not found with id: " + id));
    }

    @Override
    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public NotificationResponse saveNotificationAndReturnResponse(NotificationRequest request) {
        Notification mappedNotification = notificationMapper.toNotification(request);
        Notification response = notificationRepository.save(mappedNotification);
        return notificationMapper.toNotificationResponse(response);
    }

    @Override
    @Transactional
    public void viewNotifications(ViewNotificationsRequest request) {
        UUID userId = userRequestInfo.getUserId();
        List<UUID> userIds = notificationRepository.findUserIdsByNotificationTargetIds(request.getIds());
        if (userIds.isEmpty()) {
            throw new PlatformException("Notification user targets for checks were not found here", HttpStatus.NOT_FOUND);
        }
        if (userIds.stream()
                .anyMatch(id -> !id.equals(userId))) {
            throw new PlatformException("You can't view notifications of other users", HttpStatus.CONFLICT);
        }
        int updatedCount = notificationRepository.updateNotificationsStatus(request.getIds(), VIEWED);
        if (request.getIds().size() != updatedCount) {
            throw new PlatformException("Some notifications were not found", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUnreadNotificationsCount(UUID userId) {
        String userRole = userRequestInfo.getUserRole();
        Set<NewsTargetRole> targetRoles = new HashSet<>();
        if (ADMIN_AND_MODERATOR_ROLES.contains(userRole)) {
            return newsRepository.getUnreadNewsCount();
        }
        if (!userRepository.existsById(userId)) {
            throw new PlatformException("User с UUID " + userId + " не найден", HttpStatus.NOT_FOUND);
        }
        if (NewsTargetRole.STUDENT.name().equals(userRole) || NewsTargetRole.MENTOR.name().equals(userRole)) {
            targetRoles.add(NewsTargetRole.ALL);
            targetRoles.add(NewsTargetRole.valueOf(userRole));
        }
        return notificationRepository.getCountOfUnviewedNotifications(userId) +
               newsRepository.getUnreadNewsCountByTargetRole(targetRoles);
    }
}