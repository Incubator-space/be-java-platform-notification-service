package com.itm.space.notificationservice.kafka.consumer;


import com.itm.space.itmplatformcommonmodels.kafka.UserEvent;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

public class UserEventConsumerTest extends BaseIntegrationTest {
    @Autowired
    private UserEventConsumer userEventConsumer;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Value(value = "${spring.kafka.topic.user-events}")
    private String topic;

    @Test
    @SneakyThrows
    public void shouldHandleUserEvent() {
        UserEvent testEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_USER + "UserEventCreated.json",
                UserEvent.class
        );
        kafkaTemplate.send(topic, testEvent).get();
        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(5, SECONDS)
                .until(() -> userEventConsumer.getEvent() != null);
        assertThat(testEvent).isEqualTo(userEventConsumer.getEvent());
    }


    @Test
    @SneakyThrows
    public void shouldHandleCreateUserEventHandlers() {
        UserEvent createEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_USER + "UserEventCreated.json",
                UserEvent.class
        );
        kafkaTemplate.send(topic, createEvent).get();

        await()
                .pollInterval(Duration.ofSeconds(15))
                .atMost(20, SECONDS)
                .until(() -> userRepository.findAll().size() >= 1);
        Assertions.assertTrue(userRepository.findAll().stream().anyMatch(x -> x.getEmail().equals(createEvent.email())));
    }

    @Test
    @SneakyThrows
    public void shouldHandleUpdateUserEventHandlers() {
        UserEvent createEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_USER + "UserEventCreated.json",
                UserEvent.class
        );
        UserEvent updateEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_USER + "UserEventUpdated.json",
                UserEvent.class
        );
        kafkaTemplate.send(topic, createEvent).get();
        kafkaTemplate.send(topic, updateEvent).get();

        await()
                .pollInterval(Duration.ofSeconds(15))
                .atMost(20, SECONDS)
                .until(() -> userRepository.findAll().size() >= 1);
        Assertions.assertTrue(userRepository.findAll().stream().anyMatch(x -> x.getEmail().equals(updateEvent.email())));
    }

    @Test
    @SneakyThrows
    public void shouldHandleDeleteUserEventHandlers() {
        UserEvent createEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_USER + "UserEventCreated.json",
                UserEvent.class
        );
        UserEvent deleteEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_USER + "UserEventDeleted.json",
                UserEvent.class
        );
        kafkaTemplate.send(topic, createEvent).get();
        kafkaTemplate.send(topic, deleteEvent).get();

        await()
                .pollInterval(Duration.ofSeconds(15))
                .atMost(20, SECONDS);
        Assertions.assertFalse(userRepository.findAll().stream().anyMatch(x -> x.getEmail().equals(deleteEvent.email())));
    }
}