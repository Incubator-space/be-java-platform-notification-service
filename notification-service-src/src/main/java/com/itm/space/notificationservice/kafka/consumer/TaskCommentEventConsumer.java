package com.itm.space.notificationservice.kafka.consumer;

import com.itm.space.itmplatformcommonmodels.kafka.TaskCommentEvent;
import com.itm.space.notificationservice.kafka.handler.TaskCommentEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Log4j2
@RequiredArgsConstructor
public class TaskCommentEventConsumer implements EventConsumer<TaskCommentEvent> {

    private final Set<TaskCommentEventHandler<TaskCommentEvent>> eventHandler;

    @Override
    @KafkaListener(topics = "#{'${spring.kafka.topic.task-comment-events}'}")
    public void handle(TaskCommentEvent event) {
        eventHandler.stream()
                .filter(s -> s.isHandle(event))
                .forEach(s -> s.handle(event));
    }
}
