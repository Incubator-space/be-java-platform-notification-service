package com.itm.space.notificationservice.handler;

import com.itm.space.itmplatformcommonmodels.kafka.ReviewEvent;
import com.itm.space.notificationservice.BaseUnitTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.kafka.handler.impl.ReviewCreateEventHandler;
import com.itm.space.notificationservice.mapper.NotificationMapper;
import com.itm.space.notificationservice.repository.NotificationRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
class ReviewCreateEventHandlerTest extends BaseUnitTest {
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private NotificationMapper mapper;
    @InjectMocks
    private ReviewCreateEventHandler handler ;

    @Test
    @SneakyThrows
    void isHandle_StartWaiting() {
        ReviewEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_REVIEW_CREATE + "ReviewEventStartWaiting.json",
                ReviewEvent.class
        );
        boolean result = handler.isHandle(event);
        assertTrue(result);
    }

    @Test
    @SneakyThrows
    void isHandle_NotStartWaiting() {
        ReviewEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_REVIEW_CREATE + "ReviewEventNotStartWaiting.json",
                ReviewEvent.class
        );
        boolean result = handler.isHandle(event);
        assertFalse(result);
    }

    @Test
    @SneakyThrows
    void handle() {
        ReviewEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_REVIEW_CREATE + "ReviewEventStartWaiting.json",
                ReviewEvent.class
        );

        Notification notification = new Notification();

        when(mapper.reviewEventToNotificationWithStatusStartWaiting(event)).thenReturn(notification);
        handler.handle(event);

        Mockito.verify(mapper).reviewEventToNotificationWithStatusStartWaiting(event);
        Mockito.verify(notificationRepository, times(1)).save(notification);
    }
}