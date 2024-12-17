package com.itm.space.notificationservice.kafka.handler.impl;

import com.itm.space.itmplatformcommonmodels.kafka.TaskCommentEvent;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.mapper.NotificationMapper;
import com.itm.space.notificationservice.kafka.handler.TaskCommentEventHandler;
import com.itm.space.notificationservice.service.impl.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.itm.space.itmplatformcommonmodels.kafka.enums.TaskCommentType.CREATED;

@Component
@RequiredArgsConstructor
public class TaskCommentReplyEventHandler implements TaskCommentEventHandler<TaskCommentEvent> {
    private final NotificationMapper mapper;
    private final NotificationServiceImpl notificationService;

    @Override
    public boolean isHandle(TaskCommentEvent event) {
        return event.type() == CREATED && event.parentAuthorId() != null;
    }

    @Override
    public void handle(TaskCommentEvent event) {
        Notification notification = mapper.taskCommentEventToNotification(event);
        notificationService.saveNotification(notification);
    }
}
