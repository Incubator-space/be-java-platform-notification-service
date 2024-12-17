package com.itm.space.notificationservice.kafka.consumer;

import com.itm.space.itmplatformcommonmodels.kafka.UserEvent;
import com.itm.space.notificationservice.kafka.handler.UserEventHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Log4j2
@Getter
@Setter
@RequiredArgsConstructor
public class UserEventConsumer implements EventConsumer<UserEvent> {

    private UserEvent event;
    private final Set<UserEventHandler<UserEvent>> eventHandlers;

    @Override
    @KafkaListener(topics = "#{'${spring.kafka.topic.user-events}'}")
    public void handle(UserEvent event) {
        this.event = event;
        log.info(this + " Received: " + event);
        eventHandlers.stream().filter(i -> i.isHandle(event)).forEach(i -> i.handle(event));
    }
}