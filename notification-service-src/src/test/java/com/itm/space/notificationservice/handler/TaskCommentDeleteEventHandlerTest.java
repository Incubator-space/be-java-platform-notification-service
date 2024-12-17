package com.itm.space.notificationservice.handler;

import com.itm.space.itmplatformcommonmodels.kafka.TaskCommentEvent;
import com.itm.space.itmplatformcommonmodels.kafka.enums.TaskCommentType;
import com.itm.space.notificationservice.BaseUnitTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.kafka.handler.impl.TaskCommentDeleteEventHandler;
import com.itm.space.notificationservice.mapper.NotificationMapper;
import com.itm.space.notificationservice.service.impl.NotificationServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



public class TaskCommentDeleteEventHandlerTest extends BaseUnitTest {
    @InjectMocks
    private TaskCommentDeleteEventHandler handler;
    @Mock
    private NotificationServiceImpl notificationService;
    @Mock
    private NotificationMapper mapper;


    @SneakyThrows
    @Test
    void IsHandleDeletedCommentShoudReturnTrue() {
        TaskCommentEvent taskCommentEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_COMMENT_DELETE + "TaskCommentEventDelete.json", TaskCommentEvent.class);
        var isHandleTest = handler.isHandle(taskCommentEvent);
        Assertions.assertTrue(isHandleTest);
    }

    @Test
    @SneakyThrows
    void isHandleDeleteCommetShouldReturnFalse() {
        TaskCommentEvent taskCommentEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_COMMENT_DELETE + "TaskCommentEventNotDelete.json", TaskCommentEvent.class);
        var isHandleTest = handler.isHandle(taskCommentEvent);
        Assertions.assertFalse(isHandleTest);

    }

    @Test
    @SneakyThrows
    void handle() {
        TaskCommentEvent taskCommentEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_COMMENT_DELETE+ "TaskCommentEventDelete.json", TaskCommentEvent.class);
        Notification notification = new Notification();
        when(mapper.taskCommentDeleteEventToNotification(taskCommentEvent)).thenReturn(notification);
        handler.handle(taskCommentEvent);
        Assertions.assertEquals(TaskCommentType.DELETED, taskCommentEvent.type());
        verify(notificationService, Mockito.only()).saveNotification(notification);
    }

}

