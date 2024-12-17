package com.itm.space.notificationservice.kafka.producer;


import com.itm.space.itmplatformcommonmodels.kafka.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationEventProducer implements EventProducer<NotificationEvent> {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value(value = "${spring.kafka.topic.notification-events}")
    private String topic;

    @Override
    public void handle(NotificationEvent event) {
        kafkaTemplate.send(topic, event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendBatch(List<NotificationEvent> events) {
        events.forEach(event -> kafkaTemplate.send(topic, event));
    }
}
