package com.itm.space.notificationservice.service.impl;

import com.itm.space.notificationservice.BaseUnitTest;
import com.itm.space.notificationservice.api.enums.NewsTargetRole;
import com.itm.space.notificationservice.api.request.UserRequestInfo;
import com.itm.space.notificationservice.api.response.NotificationResponse;
import com.itm.space.notificationservice.domain.entity.News;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.domain.enums.NewsStatus;
import com.itm.space.notificationservice.domain.enums.NotificationType;
import com.itm.space.notificationservice.enums.NotificationStatus;
import com.itm.space.notificationservice.exception.ConflictException;
import com.itm.space.notificationservice.exception.NotFoundException;
import com.itm.space.notificationservice.exception.PlatformException;
import com.itm.space.notificationservice.mapper.NotificationMapper;
import com.itm.space.notificationservice.repository.NewsRepository;
import com.itm.space.notificationservice.repository.NotificationRepository;
import com.itm.space.notificationservice.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class NotificationServiceImplTest extends BaseUnitTest {
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRequestInfo userRequestInfo;
    @Mock
    private NewsRepository newsRepository;
    @Mock
    private NotificationMapper notificationMapper;
    @InjectMocks
    private NotificationServiceImpl notificationServiceImpl;

    @Test
    @DisplayName("Должен вернуть для модератора и админа количество непрочитанных новостей")
    void shouldCountForAdminModeratorRoles() {

        List<News> newsList = List.of(
                new News(UUID.randomUUID(), NewsStatus.NEW, NewsTargetRole.ALL, "title", "message"),
                new News(UUID.randomUUID(), NewsStatus.NEW, NewsTargetRole.STUDENT, "title", "message"),
                new News(UUID.randomUUID(), NewsStatus.NEW, NewsTargetRole.MENTOR, "title", "message")
        );

        when(userRequestInfo.getUserRole()).thenReturn("ROLE_ADMIN").thenReturn("ROLE_MODERATOR");
        when(newsRepository.getUnreadNewsCount()).thenReturn((long) newsList.size());

        long count = notificationServiceImpl.getUnreadNotificationsCount(UUID.randomUUID());

        assertEquals(newsList.size(), count);
        verify(userRequestInfo, times(1)).getUserRole();
    }

    @Test
    @DisplayName("Должен вернуть для ROLE_MENTOR количество непрочитанных уведомлений и новостей")
    void shouldCountForMentorRole() {
        UUID userId = UUID.randomUUID();
        String userRole = "MENTOR";
        Set<NewsTargetRole> roles = Set.of(NewsTargetRole.ALL, NewsTargetRole.valueOf(userRole));

        List<Notification> notificationsList = List.of(
                new Notification(NotificationType.EMAIL, "action", UUID.randomUUID(), NotificationStatus.WAIT,
                        UUID.randomUUID(), "title", "message"),
                new Notification(NotificationType.EMAIL, "action", UUID.randomUUID(), NotificationStatus.SENT,
                        UUID.randomUUID(), "title", "message")
        );

        List<News> newsList = List.of(
                new News(UUID.randomUUID(), NewsStatus.NEW, NewsTargetRole.MENTOR, "title", "message"),
                new News(UUID.randomUUID(), NewsStatus.NEW, NewsTargetRole.ALL, "title", "message")
        );

        when(userRequestInfo.getUserRole()).thenReturn("MENTOR");
        when(userRepository.existsById(userId)).thenReturn(true);
        when(notificationRepository.getCountOfUnviewedNotifications(userId)).thenReturn((long) notificationsList.size());
        when(newsRepository.getUnreadNewsCountByTargetRole(roles)).thenReturn((long) newsList.size());

        long count = notificationServiceImpl.getUnreadNotificationsCount(userId);

        assertEquals(newsList.size() + notificationsList.size(), count);
        verify(userRequestInfo, times(1)).getUserRole();
        verify(notificationRepository, times(1)).getCountOfUnviewedNotifications(userId);
        verify(newsRepository, times(1)).getUnreadNewsCountByTargetRole(roles);
    }

    @Test
    @DisplayName("Должен вернуть для ROLE_STUDENT количество новых уведомлений и новостей")
    void shouldCountForStudentRole() {
        UUID userId = UUID.randomUUID();
        String userRole = "STUDENT";
        Set<NewsTargetRole> roles = Set.of(NewsTargetRole.ALL, NewsTargetRole.valueOf(userRole));

        List<Notification> notificationsList = List.of(
                new Notification(NotificationType.EMAIL, "action", UUID.randomUUID(), NotificationStatus.WAIT,
                        UUID.randomUUID(), "title", "message"),
                new Notification(NotificationType.EMAIL, "action", UUID.randomUUID(), NotificationStatus.SENT,
                        UUID.randomUUID(), "title", "message")
        );

        List<News> newsList = List.of(
                new News(UUID.randomUUID(), NewsStatus.NEW, NewsTargetRole.STUDENT, "title", "message"),
                new News(UUID.randomUUID(), NewsStatus.NEW, NewsTargetRole.ALL, "title", "message")
        );

        when(userRequestInfo.getUserRole()).thenReturn("STUDENT");
        when(userRepository.existsById(userId)).thenReturn(true);
        when(notificationRepository.getCountOfUnviewedNotifications(userId)).thenReturn((long) notificationsList.size());
        when(newsRepository.getUnreadNewsCountByTargetRole(roles)).thenReturn((long) newsList.size());

        long count = notificationServiceImpl.getUnreadNotificationsCount(userId);

        assertEquals(newsList.size() + notificationsList.size(), count);
        verify(userRequestInfo, times(1)).getUserRole();
        verify(notificationRepository, times(1)).getCountOfUnviewedNotifications(userId);
        verify(newsRepository, times(1)).getUnreadNewsCountByTargetRole(roles);
    }

    @Test
    @DisplayName("Должен бросить PlatformException при отсутствии пользователя в БД")
    void throwsPlatformException() {
        UUID userId = UUID.randomUUID();

        when(userRequestInfo.getUserRole()).thenReturn("STUDENT");
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(PlatformException.class, () -> {
            notificationServiceImpl.getUnreadNotificationsCount(userId);
        });
    }

    @Test
    @DisplayName("Должен достать нофтификацию по ID из БД")
    void shouldViewNotification() {
        UUID userId = UUID.fromString("d55d32bf-7a0f-4b29-8abd-195f3b358da7");
        Notification notification = jsonParserUtil.getObjectFromJson(
                "json/service/UserServiceImplTest/Notification.json", Notification.class);
        NotificationResponse exchangeResponse = jsonParserUtil.getObjectFromJson(
                "json/service/UserServiceImplTest/NotificationResponse.json", NotificationResponse.class);

        when(notificationRepository.findById(notification.getId())).thenReturn(Optional.of(notification));
        when(notificationMapper.toNotificationResponse(notification)).thenReturn(exchangeResponse);

        NotificationResponse notificationResponse = notificationServiceImpl.viewNotification(notification.getId(), userId);
        assertEquals(notificationResponse, exchangeResponse);
    }
    @Test
    @DisplayName("Должен выбросить исключение ConflictException")
    void shouldViewNotificationConflictException() {
        UUID userId = UUID.randomUUID();
        Notification notification = jsonParserUtil.getObjectFromJson(
                "json/service/UserServiceImplTest/Notification.json", Notification.class);
        NotificationResponse exchangeResponse = jsonParserUtil.getObjectFromJson(
                "json/service/UserServiceImplTest/NotificationResponse.json", NotificationResponse.class);

        when(notificationRepository.findById(notification.getId())).thenReturn(Optional.of(notification));

        assertThrows(ConflictException.class, () ->{
            notificationServiceImpl.viewNotification(notification.getId(), userId);
        });
    }
    @Test
    @DisplayName("Должен выбросить исключение NotFoundException")
    void shouldViewNotificationNotFoundException() {
        UUID userId = UUID.fromString("d55d32bf-7a0f-4b29-8abd-195f3b358da7");
        Notification notification = jsonParserUtil.getObjectFromJson(
                "json/service/UserServiceImplTest/Notification.json", Notification.class);

        when(notificationRepository.findById(notification.getId())).thenReturn(Optional.of(notification));

        assertThrows(NotFoundException.class, () ->{
            notificationServiceImpl.viewNotification(notification.getId(), userId);
        });
    }
}