package com.itm.space.notificationservice.kafka.consumer;

import com.itm.space.itmplatformcommonmodels.kafka.DirectionEvent;
import com.itm.space.notificationservice.kafka.handler.DirectionEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Log4j2
@RequiredArgsConstructor
public class DirectionEventConsumer implements EventConsumer<DirectionEvent> {
    private final Set<DirectionEventHandler<DirectionEvent>> eventHandlers;

    @Override
    @KafkaListener(topics = "#{'${spring.kafka.topic.direction-events}'}")
    public void handle(DirectionEvent directionEvent) {
        eventHandlers.stream()
                .filter(handler -> handler.isHandle(directionEvent))
                .forEach(handler -> handler.handle(directionEvent));
    }
}
