package com.itm.space.notificationservice.kafka.producer;

import com.itm.space.itmplatformcommonmodels.kafka.NotificationEvent;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;

import java.time.Duration;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationEventProducerTest extends BaseIntegrationTest {
    @Autowired
    private NotificationEventProducer notificationEventProducer;
    @Value(value = "${spring.kafka.topic.notification-events}")
    private String topic;

    @Test
    @SneakyThrows
    void shouldSendMessage() {
        NotificationEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_PRODUCER_NOTIFICATION + "NotificationEvent.json",
                NotificationEvent.class
        );
        notificationEventProducer.handle(event);

        testConsumerService.consumeAndValidate(topic, event);
    }
}