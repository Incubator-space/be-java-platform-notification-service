package com.itm.space.notificationservice.kafka.consumer;

import com.github.database.rider.core.api.dataset.DataSet;
import com.itm.space.itmplatformcommonmodels.kafka.ReviewEvent;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.repository.NotificationRepository;
import com.itm.space.notificationservice.service.impl.TestProducerService;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


import static com.itm.space.notificationservice.domain.enums.NotificationType.PUSH;
import static com.itm.space.notificationservice.enums.NotificationStatus.WAIT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ReviewEventConsumerTest extends BaseIntegrationTest {
    @Autowired
    private TestProducerService testProducerService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Value(value = "${spring.kafka.topic.review-events}")
    private String topic;
    private boolean semaphoreAcquired = false;

    @BeforeEach
    void setUp() {
        semaphoreAcquired = false;
    }

    @Test
    @SneakyThrows
    @DataSet(value = "datasets/kafka/consumer/ReviewEventConsumerTest.yml",cleanAfter = true, cleanBefore = true)
    public void shouldHandleReviewEvent() {
        ReviewEvent testEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_REVIEW + "ReviewEventInProgress.json",
                ReviewEvent.class
        );
        testProducerService.send(topic, testEvent);
        while (!semaphoreAcquired) {
            semaphoreAcquired = testProducerService.getSemaphore().tryAcquire();
        }
        Notification notification = notificationRepository.findAll().stream()
                .filter(notification1 -> notification1.getTitle()
                        .equals("Напоминание о ревью")).findAny().get();
        assertThat(notification.getTarget()).isNotNull();
        assertThat(notification.getType()).isEqualTo(PUSH);
        assertThat(notification.getAction()).isEqualTo("placeholder_action");
        assertThat(notification.getStatus()).isEqualTo(WAIT);
        assertThat(notification.getTitle()).isEqualTo("Напоминание о ревью");
        assertThat(notification.getMessage()).contains("Запланировано ревью");
        assertThat(notification.getId()).isNotNull();
        testProducerService.getSemaphore().release();
    }

    @Test
    @SneakyThrows
    @DataSet(value = "datasets/kafka/consumer/ReviewEventConsumerTest.yml",cleanAfter = true, cleanBefore = true)
    @DisplayName("Тест на проверку работы консьюмера когда у события Status.START_WAITING")
    public void shouldHandleReviewEventWithStatusStartWaiting() {
        ReviewEvent testEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_REVIEW + "ReviewEventStartWaiting.json",
                ReviewEvent.class
        );
        testProducerService.send(topic, testEvent);
        while (!semaphoreAcquired) {
            semaphoreAcquired = testProducerService.getSemaphore().tryAcquire();
        }
        Notification notification = notificationRepository.findAll().stream()
                .filter(notification1 -> notification1.getTitle()
                        .equals("Запись на ревью")).findAny().get();
        assertThat(notification.getTarget()).isEqualTo(testEvent.mentorId());
        assertThat(notification.getType()).isEqualTo(PUSH);
        assertThat(notification.getAction()).isEqualTo("placeholder_action");
        assertThat(notification.getStatus()).isEqualTo(WAIT);
        assertThat(notification.getTitle()).isEqualTo("Запись на ревью");
        assertThat(notification.getMessage()).contains("Студент записался на ревью. Время проведения");
        assertThat(notification.getId()).isNotNull();
        testProducerService.getSemaphore().release();
    }

    @Test
    @SneakyThrows
    @DataSet(value = "datasets/kafka/consumer/ReviewEventConsumerTest.yml",cleanAfter = true, cleanBefore = true)
    @DisplayName("Тест на проверку работы консьюмера когда у события Status.CANCELED")
    public void shouldHandleReviewEventWithStatusCanceled() {
        ReviewEvent testEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_REVIEW + "ReviewEventCanceled.json",
                ReviewEvent.class
        );
        testProducerService.send(topic, testEvent);
        while (!semaphoreAcquired) {
            semaphoreAcquired = testProducerService.getSemaphore().tryAcquire();
        }
        Notification notification = notificationRepository.findAll().stream()
                .filter(notification1 -> notification1.getTitle()
                        .equals("Отмена ревью")).findAny().get();
        assertThat(notification.getTarget()).isEqualTo(testEvent.studentId());
        assertThat(notification.getType()).isEqualTo(PUSH);
        assertThat(notification.getAction()).isEqualTo("placeholder_action");
        assertThat(notification.getStatus()).isEqualTo(WAIT);
        assertThat(notification.getTitle()).isEqualTo("Отмена ревью");
        assertThat(notification.getMessage()).contains("Отменено ревью, запланированное на время");
        assertThat(notification.getId()).isNotNull();
        testProducerService.getSemaphore().release();
    }
}