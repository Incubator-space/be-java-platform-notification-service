package com.itm.space.notificationservice.handler;

import com.itm.space.itmplatformcommonmodels.kafka.ReviewEvent;
import com.itm.space.notificationservice.BaseUnitTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.kafka.handler.impl.ReviewDeleteEventHandler;
import com.itm.space.notificationservice.mapper.NotificationMapper;
import com.itm.space.notificationservice.repository.NotificationRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class ReviewDeleteEventHandlerTest extends BaseUnitTest {
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private NotificationMapper mapper;
    @InjectMocks
    private ReviewDeleteEventHandler handler;

    @Test
    @SneakyThrows
    void isHandle_WhenStatusIsCanceled_ShouldReturnTrue() {
        ReviewEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_REVIEW_DELETE + "ReviewEventCanceled.json",
                ReviewEvent.class
        );
        boolean result = handler.isHandle(event);
        assertTrue(result);
    }

    @Test
    @SneakyThrows
    void isHandle_WhenStatusIsNotCanceled_ShouldReturnFalse() {
        ReviewEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_REVIEW_DELETE + "ReviewEventNotCanceled.json",
                ReviewEvent.class
        );
        boolean result = handler.isHandle(event);
        assertFalse(result);
    }

    @Test
    @SneakyThrows
    void handle() {
        ReviewEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_REVIEW_DELETE + "ReviewEventCanceled.json",
                ReviewEvent.class
        );
        when(mapper.reviewEventToNotificationWithStatusCanceledMentor(event)).thenReturn(new Notification());
        when(mapper.reviewEventToNotificationWithStatusCanceledStudent(event)).thenReturn(new Notification());
        handler.handle(event);
        verify(mapper, times(1)).reviewEventToNotificationWithStatusCanceledMentor(event);
        verify(mapper, times(1)).reviewEventToNotificationWithStatusCanceledStudent(event);
        verify(notificationRepository, times(1)).saveAll(anyList());
    }
}
