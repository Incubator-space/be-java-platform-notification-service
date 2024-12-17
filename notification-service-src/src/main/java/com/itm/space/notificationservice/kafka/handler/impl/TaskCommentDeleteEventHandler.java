package com.itm.space.notificationservice.kafka.handler.impl;


import com.itm.space.itmplatformcommonmodels.kafka.TaskCommentEvent;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.kafka.handler.TaskCommentEventHandler;
import com.itm.space.notificationservice.mapper.NotificationMapper;
import com.itm.space.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.itm.space.itmplatformcommonmodels.kafka.enums.TaskCommentType.DELETED;


@Component
@RequiredArgsConstructor
public class TaskCommentDeleteEventHandler implements TaskCommentEventHandler<TaskCommentEvent> {

    private final NotificationService notificationService;
    private final NotificationMapper mapper;

    @Override
    public boolean isHandle(TaskCommentEvent taskCommentEvent) {
        return taskCommentEvent.type() == DELETED &&
                taskCommentEvent.interactorId() != taskCommentEvent.authorId();
    }

    @Override
    public void handle(TaskCommentEvent taskCommentEvent) {
        Notification notification = mapper.taskCommentDeleteEventToNotification(taskCommentEvent);
        notificationService.saveNotification(notification);
    }
}




