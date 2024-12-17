package com.itm.space.notificationservice.handler;


import com.itm.space.itmplatformcommonmodels.kafka.AchievementEvent;
import com.itm.space.notificationservice.BaseUnitTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.kafka.handler.impl.AchievementDefaultEventHandler;
import com.itm.space.notificationservice.mapper.NotificationMapper;
import com.itm.space.notificationservice.service.impl.NotificationServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.itm.space.itmplatformcommonmodels.kafka.enums.AchievementStatus.IN_PROGRESS;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class AchievementDefaultEventHandlerTest extends BaseUnitTest {
    @Mock
    private NotificationServiceImpl notificationService;
    @Mock
    private NotificationMapper mapper;
    @InjectMocks
    private AchievementDefaultEventHandler handler;

    @Test
    @SneakyThrows
    void isHandleReturnsTrueWhenProgressEqualsGoal() {
        AchievementEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_ACHIEVEMENT_DEFAULT + "AchievementEventProgressEqualsGoal.json",
                AchievementEvent.class
        );
        Assertions.assertTrue(handler.isHandle(event));
    }

    @Test
    @SneakyThrows
    void isHandleReturnsFalseWhenProgressDoesNotEqualGoal() {
        AchievementEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_ACHIEVEMENT_DEFAULT + "AchievementEventProgressDoesNotEqualsGoal.json",
                AchievementEvent.class
        );
        Assertions.assertFalse(handler.isHandle(event));
    }

    @Test
    @SneakyThrows
    void handleSavesNotificationToRepository() {
        AchievementEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_ACHIEVEMENT_DEFAULT + "AchievementEventSavesNotificationRepository.json",
                AchievementEvent.class
        );
        Notification notification = new Notification();
        when(mapper.achievementEventToNotification(event)).thenReturn(notification);
        handler.handle(event);
        Assertions.assertEquals(IN_PROGRESS.toString(), event.status());
        verify(mapper).achievementEventToNotification(event);
        verify(notificationService, times(1)).saveNotification(notification);
    }
}