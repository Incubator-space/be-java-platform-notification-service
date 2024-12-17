package com.itm.space.notificationservice.kafka.consumer;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.itm.space.itmplatformcommonmodels.kafka.TaskCommentEvent;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.repository.NotificationRepository;
import com.itm.space.notificationservice.service.impl.TestProducerService;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

import static org.aspectj.bridge.MessageUtil.fail;

class TaskCommentEventConsumerTest extends BaseIntegrationTest {
    @Autowired
    private TestProducerService testProducerService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Value(value = "${spring.kafka.topic.task-comment-events}")
    private String topic;
    private boolean semaphoreAcquired = false;

    @BeforeEach
    void setUp() {
        semaphoreAcquired = false;
    }

    @Test
    @SneakyThrows
    @DataSet(value = "datasets/kafka/consumer/DatasetNotificationTypeDeleted.yml")
    @ExpectedDataSet(value = "datasets/kafka/consumer/NotificationTypeDeleted.yml", ignoreCols = {"id", "created"})
    @DisplayName("Тест на проверку работы консьюмера когда у события Status.DELETED и interactorId() != authorId()")
    void shouldHandleTaskCommentEventWithStatusDeleted() {
        TaskCommentEvent taskCommentEvent = jsonParserUtil.getObjectFromJson(
                "json/kafka/consumer/TaskCommentEventConsumerTest/TaskCommentEventDeleted.json", TaskCommentEvent.class);

        testProducerService.send(topic, taskCommentEvent);
    }


    @Test
    @SneakyThrows
    @DataSet(value = "datasets/kafka/consumer/DatasetNotificationTypeCreated.yml")
    @ExpectedDataSet(value = "datasets/kafka/consumer/NotificationTypeCreated.yml", ignoreCols = {"id", "created"})
    @DisplayName("Тест на проверку работы консьюмера когда у события Status.CREATED и parentAuthorId != null")
    void shouldHandleTaskCommentEventWithStatusCreated() {
        TaskCommentEvent taskCommentEvent = jsonParserUtil.getObjectFromJson(
                "json/kafka/consumer/TaskCommentEventConsumerTest/TaskCommentEventCreated.json", TaskCommentEvent.class);

        testProducerService.send(topic, taskCommentEvent);
    }

}
