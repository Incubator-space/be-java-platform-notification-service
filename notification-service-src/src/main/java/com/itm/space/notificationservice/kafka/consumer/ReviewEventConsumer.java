package com.itm.space.notificationservice.kafka.consumer;

import com.itm.space.itmplatformcommonmodels.kafka.ReviewEvent;
import com.itm.space.notificationservice.kafka.handler.ReviewEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Log4j2
@RequiredArgsConstructor
public class ReviewEventConsumer implements EventConsumer<ReviewEvent> {

    private final Set<ReviewEventHandler<ReviewEvent>> eventHandlers;
    @Override
    @KafkaListener(topics = "#{'${spring.kafka.topic.review-events}'}")
    public void handle(ReviewEvent event) {
        eventHandlers.stream()
                .filter(handler -> handler.isHandle(event))
                .forEach(handler -> handler.handle(event));
    }
}