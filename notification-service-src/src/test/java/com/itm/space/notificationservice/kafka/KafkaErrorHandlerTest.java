package com.itm.space.notificationservice.kafka;


import com.itm.space.itmplatformcommonmodels.kafka.NotificationEvent;
import com.itm.space.itmplatformcommonmodels.kafka.ReviewEvent;
import com.itm.space.itmplatformcommonmodels.kafka.TransactionEvent;
import com.itm.space.itmplatformcommonmodels.kafka.enums.NotificationEventStatus;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.kafka.producer.NotificationEventProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@Slf4j
class KafkaErrorHandlerTest extends BaseIntegrationTest {

    @Value(value = "${spring.kafka.topic.transaction-events}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    @DisplayName("Должен пропустить notificationEvent")
    void shouldSkipMessage() {
        NotificationEvent notificationEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_PRODUCER_NOTIFICATION + "NotificationEvent.json",
                NotificationEvent.class
        );
        TransactionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_TRANSACTION +
                        "TransactionConsumerIncomeCompleted.json", TransactionEvent.class
        );

        kafkaTemplate.send(topic, notificationEvent);
        kafkaTemplate.send(topic, event);

        assertDoesNotThrow(() -> testConsumerService.consumeAndValidate(topic, event));
    }




}
