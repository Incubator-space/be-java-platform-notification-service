package com.itm.space.notificationservice.kafka.consumer;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.itm.space.itmplatformcommonmodels.kafka.DirectionEvent;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.repository.NewsRepository;
import com.itm.space.notificationservice.service.impl.TestProducerService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

class DirectionEventConsumerTest extends BaseIntegrationTest {
    @Autowired
    private TestProducerService testProducerService;
    @Autowired
    private NewsRepository newsRepository;
    @Value(value = "${spring.kafka.topic.direction-events}")
    private String topic;
    private boolean semaphoreAcquired = false;

    @BeforeEach
    void setUp() {
        semaphoreAcquired = false;
    }

    @Test
    @SneakyThrows
    @DataSet(value = "datasets/kafka/consumer/DatasetDirectionEventConsumerTestNew.yml")
    @ExpectedDataSet(value = "datasets/kafka/consumer/DirectionEventConsumerTestNew.yml",ignoreCols = {"id", "created"})
    @DisplayName("Тест на проверку работы консьюмера когда у события Status.NEW")
    void shouldHandleDirectionEventWithStatusNew() {
        DirectionEvent testEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_DIRECTION + "DirectionEventNew.json",
                DirectionEvent.class
        );
        testProducerService.send(topic, testEvent);

    }

    @Test
    @SneakyThrows
    @DataSet(value = "datasets/kafka/consumer/DatasetDirectionEventConsumerTestUpdate.yml", cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/DirectionEventConsumerTestUpdate.yml",ignoreCols = {"id", "created"})
    @DisplayName("Тест на проверку работы консьюмера когда у события Status.UPDATE")
    void shouldHandleDirectionEventWithStatusUpdate() {

        DirectionEvent testEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_DIRECTION + "DirectionEventUpdate.json",
                DirectionEvent.class
        );
        testProducerService.send(topic, testEvent);

    }
}

