package com.itm.space.notificationservice.kafka.consumer;

import com.itm.space.itmplatformcommonmodels.kafka.AchievementEvent;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.service.impl.TestProducerService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.assertj.core.api.Assertions.assertThat;

class AchievementEventConsumerTest extends BaseIntegrationTest {
    @Autowired
    private AchievementEventConsumer achievementEventConsumer;
    @Autowired
    private TestProducerService testProducerService;
    @Value(value = "${spring.kafka.topic.achievement-events}")
    private String topic;
    private boolean semaphoreAcquired = false;

    @BeforeEach
    void setUp() {
        semaphoreAcquired = false;
    }

    @Test
    @DisplayName("Тест на проверку работы консьюмера когда у события progress == goal.")
    @SneakyThrows
    void shouldHandleAchievementEventWhenProgressIsEqualsToGoal() {
        AchievementEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_ACHIEVEMENT + "AchievementEventProgressEqualsGoal.json",
                AchievementEvent.class
        );
        testProducerService.send(topic, event);
    }

    @Test
    @DisplayName("Тест на проверку работы консьюмера когда у события progress != goal.")
    @SneakyThrows
    void shouldHandleAchievementEventWhenProgressIsNotEqualsToGoal() {
        AchievementEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_ACHIEVEMENT + "AchievementEventProgressDoesNotEqualsGoal.json",
                AchievementEvent.class
        );
        testProducerService.send(topic, event);
    }
}