package com.itm.space.notificationservice.scheduler;

import com.github.database.rider.core.api.dataset.DataSet;
import com.itm.space.itmplatformcommonmodels.kafka.NotificationEvent;
import com.itm.space.notificationservice.BaseIntegrationTest;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationServiceSchedulerTest extends BaseIntegrationTest {

    @Autowired
    private NotificationServiceScheduler notificationServiceScheduler;
    @Autowired
    private ConsumerFactory<String, NotificationEvent> kafkaConsumerFactory;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;
    @Value("${spring.kafka.topic.notification-events}")
    private String topic;

    @Test
    @DisplayName("Тест на проверку шедулера с выгрузкой нотификаций каждую минуту и сменой статуса.")
    @DataSet(value = "datasets/sheduler/NotificationSheduler/notifications.yml", cleanAfter = true, cleanBefore = true)
    void testNotificationShedulerFromWaitToSent() {
        notificationServiceScheduler.changeNotificationStatus();
        try (Consumer<String, NotificationEvent> kafkaConsumer = kafkaConsumerFactory.createConsumer(groupId, null)) {
            kafkaConsumer.subscribe(Collections.singleton(topic));
            ConsumerRecords<String, NotificationEvent> records = kafkaConsumer.poll(Duration.ofSeconds(10));
            assertThat(records)
                    .isNotEmpty()
                    .extracting(ConsumerRecord::value)
                    .extracting(NotificationEvent::id)
                    .contains(
                            UUID.fromString("08a00552-abe0-4421-a365-6a65de2554a9"),
                            UUID.fromString("55f197ba-9bd6-4042-ac64-3ca98079af3e"),
                            UUID.fromString("c8ac3e38-664a-41b7-af3c-a94020775ee1")
                    );
        }
    }
}
