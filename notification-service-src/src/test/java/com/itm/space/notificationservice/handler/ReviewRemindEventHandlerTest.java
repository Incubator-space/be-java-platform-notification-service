package com.itm.space.notificationservice.handler;

import com.itm.space.itmplatformcommonmodels.kafka.ReviewEvent;
import com.itm.space.notificationservice.BaseUnitTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.kafka.handler.impl.ReviewRemindEventHandler;
import com.itm.space.notificationservice.mapper.NotificationMapper;
import com.itm.space.notificationservice.repository.NotificationRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class ReviewRemindEventHandlerTest extends BaseUnitTest {
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private NotificationMapper mapper;
    @InjectMocks
    private ReviewRemindEventHandler remindEventHandler;

    @Test
    @SneakyThrows
    void isHandleReturnTrueWhenIsRemindTrue() {
        ReviewEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_REVIEW_REMIND + "ReviewEventRemindTrue.json",
                ReviewEvent.class
        );
        Assertions.assertTrue(remindEventHandler.isHandle(event));
    }

    @Test
    @SneakyThrows
    void isHandleReturnFalseWhenIsRemindFalse() {
        ReviewEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_REVIEW_REMIND + "ReviewEventRemindFalse.json",
                ReviewEvent.class
        );
        Assertions.assertFalse(remindEventHandler.isHandle(event));
    }

    @Test
    @SneakyThrows
    void handleMappingEventsToNotificationsThenAddToListOneTime() {
        ReviewEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_REVIEW_REMIND + "ReviewEventRemindTrue.json",
                ReviewEvent.class
        );
        when(mapper.reviewEventReminderToNotificationForMentor(event)).thenReturn(new Notification());
        when(mapper.reviewEventReminderToNotificationForStudent(event)).thenReturn(new Notification());
        remindEventHandler.handle(event);
        verify(mapper,times(1)).reviewEventReminderToNotificationForMentor(event);
        verify(mapper,times(1)).reviewEventReminderToNotificationForStudent(event);
        verify(notificationRepository,times(1)).saveAll(anyList());
    }
}