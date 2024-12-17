package com.itm.space.notificationservice.handler;

import com.itm.space.itmplatformcommonmodels.kafka.TaskCommentEvent;
import com.itm.space.itmplatformcommonmodels.kafka.enums.TaskCommentType;
import com.itm.space.notificationservice.BaseUnitTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.kafka.handler.impl.TaskCommentReplyEventHandler;
import com.itm.space.notificationservice.mapper.NotificationMapper;
import com.itm.space.notificationservice.repository.NotificationRepository;
import com.itm.space.notificationservice.service.impl.NotificationServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.only;


class TaskCommentReplyEventHandlerTest extends BaseUnitTest {
    @Mock
    private NotificationMapper mapper;
    @Mock
    private NotificationServiceImpl notificationService;
    @InjectMocks
    private TaskCommentReplyEventHandler taskCommentReplyEventHandler;

    @Test
    @SneakyThrows
    void isHandleShouldReturnTrue() {
        TaskCommentEvent taskCommentEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_TASK_COMMENT_REPLY_EVENT + "TaskCommentEventTrue.json", TaskCommentEvent.class);
        var isHandleTest = taskCommentReplyEventHandler.isHandle(taskCommentEvent);
        Assertions.assertTrue(isHandleTest);
    }

    @Test
    @SneakyThrows
    void isHandleShouldReturnFalse() {
        TaskCommentEvent taskCommentEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_TASK_COMMENT_REPLY_EVENT + "TaskCommentEventFalse.json", TaskCommentEvent.class);
        var isHandleTest = taskCommentReplyEventHandler.isHandle(taskCommentEvent);
        Assertions.assertFalse(isHandleTest);

    }

    @Test
    @SneakyThrows
    void handle() {
        TaskCommentEvent taskCommentEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_TASK_COMMENT_REPLY_EVENT + "TaskCommentEventTrue.json", TaskCommentEvent.class
        );
        Notification notification = new Notification();
        when(mapper.taskCommentEventToNotification(taskCommentEvent)).thenReturn(notification);
        taskCommentReplyEventHandler.handle(taskCommentEvent);
        Assertions.assertEquals(TaskCommentType.CREATED, taskCommentEvent.type());
        Assertions.assertNotEquals(null, taskCommentEvent.parentAuthorId());
        verify(notificationService, only()).saveNotification(notification);
    }
}