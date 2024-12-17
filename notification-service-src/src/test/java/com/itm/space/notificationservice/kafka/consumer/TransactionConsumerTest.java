package com.itm.space.notificationservice.kafka.consumer;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.itm.space.itmplatformcommonmodels.kafka.TransactionEvent;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

@Log4j2
class TransactionConsumerTest extends BaseIntegrationTest {

    @Value("#{'${spring.kafka.topic.transaction-events}'}")
    private String topic;

    private static final String START_HANDLE = "Должен вызывать handle для сохранения уведомления";

    @Test
    @DisplayName(START_HANDLE + " платеж получен")
    @DataSet(value = "datasets/kafka/consumer/TransactionForIncomeCompleted.yml", cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/TransactionIncomeCompleted.yml", ignoreCols = {"id", "created"})
    void shouldHandleTransactionIncomeCompleted() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_TRANSACTION +
                        "TransactionConsumerIncomeCompleted.json", TransactionEvent.class
        );

        log.info("Старт handle: {}", event);
        testProducerService.send(topic, event);
    }

    @Test
    @DisplayName("Пользователь не найден")
    @DataSet(value = "datasets/kafka/consumer/testUserNotFoundTransaction.yml", cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/testUserNotFoundTransaction.yml", ignoreCols = {"id", "created"})
    void shouldReturn400whereTransactionIncomeCompleted() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_TRANSACTION +
                        "TransactionConsumerIncomeCompletedNotFoundException.json", TransactionEvent.class
        );

        testProducerService.send(topic, event);
    }

    @Test
    @DisplayName(START_HANDLE + " платеж не прошел")
    @DataSet(value = "datasets/kafka/consumer/TransactionForIncomeCancelled.yml", cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/TransactionIncomeCancelled.yml", ignoreCols = {"id", "created"})
    void shouldHandleTransactionIncomeCancelled() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_TRANSACTION +
                        "TransactionConsumerIncomeCancelled.json", TransactionEvent.class
        );

        log.info("Старт handle: {}", event);
        testProducerService.send(topic, event);
    }

    @Test
    @DisplayName("Пользователь не найден")
    @DataSet(value = "datasets/kafka/consumer/testUserNotFoundTransaction.yml", cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/testUserNotFoundTransaction.yml", ignoreCols = {"id", "created"})
    void shouldReturn400whereTransactionIncomeCancelled() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_TRANSACTION +
                        "TransactionConsumerIncomeCancelledNotFoundException.json", TransactionEvent.class
        );

        testProducerService.send(topic, event);
    }

    @Test
    @DisplayName(START_HANDLE + " платеж в обработке")
    @DataSet(value = "datasets/kafka/consumer/TransactionForIncomePending.yml", cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/TransactionIncomePending.yml", ignoreCols = {"id", "created"})
    void shouldHandleTransactionIncomePending() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_TRANSACTION +
                        "TransactionConsumerIncomePending.json", TransactionEvent.class
        );

        log.info("Старт handle: {}", event);
        testProducerService.send(topic, event);
    }

    @Test
    @DisplayName("Пользователь не найден")
    @DataSet(value = "datasets/kafka/consumer/testUserNotFoundTransaction.yml", cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/testUserNotFoundTransaction.yml", ignoreCols = {"id", "created"})
    void shouldReturn400whereTransactionIncomePending() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_TRANSACTION +
                        "TransactionConsumerIncomePendingNotFoundException.json", TransactionEvent.class
        );

        testProducerService.send(topic, event);
    }

    @Test
    @DisplayName(START_HANDLE + " платеж ожидает подтверждение")
    @DataSet(value = "datasets/kafka/consumer/TransactionForIncomeWaiting.yml", cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/TransactionIncomeWaiting.yml", ignoreCols = {"id", "created"})
    void shouldHandleTransactionIncomeWaiting() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_TRANSACTION +
                        "TransactionConsumerIncomeWaiting.json", TransactionEvent.class
        );

        log.info("Старт handle: {}", event);
        testProducerService.send(topic, event);
    }

    @Test
    @DisplayName("Пользователь не найден")
    @DataSet(value = "datasets/kafka/consumer/testUserNotFoundTransaction.yml", cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/testUserNotFoundTransaction.yml", ignoreCols = {"id", "created"})
    void shouldReturn400whereTransactionIncomeWaiting() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_TRANSACTION +
                        "TransactionConsumerIncomeWaitingNotFoundException.json", TransactionEvent.class
        );

        testProducerService.send(topic, event);
    }

    @Test
    @DisplayName(START_HANDLE + " выплата не прошла")
    @DataSet(value = "datasets/kafka/consumer/TransactionForOutcomeCancelled.yml", cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/TransactionOutcomeCancelled.yml", ignoreCols = {"id", "created"})
    void shouldHandleTransactionOutcomeCancelled() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_TRANSACTION +
                        "TransactionConsumerOutcomeCancelled.json", TransactionEvent.class
        );

        log.info("Старт handle: {}", event);
        testProducerService.send(topic, event);
    }

    @Test
    @DisplayName("Пользователь не найден")
    @DataSet(value = "datasets/kafka/consumer/testUserNotFoundTransaction.yml", cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/testUserNotFoundTransaction.yml", ignoreCols = {"id", "created"})
    void shouldReturn400whereTransactionOutcomeCancelled() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_TRANSACTION +
                        "TransactionConsumerOutcomeCancelledNotFoundException.json", TransactionEvent.class
        );

        testProducerService.send(topic, event);
    }

    @Test
    @DisplayName(START_HANDLE + " выплата проведена")
    @DataSet(value = "datasets/kafka/consumer/TransactionForOutcomeCompleted.yml", cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/TransactionOutcomeCompleted.yml", ignoreCols = {"id", "created"})
    void shouldHandleTransactionOutcomeCompleted() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_TRANSACTION +
                        "TransactionConsumerOutcomeCompleted.json", TransactionEvent.class
        );

        log.info("Старт handle: {}", event);
        testProducerService.send(topic, event);
    }

    @Test
    @DisplayName("Пользователь не найден")
    @DataSet(value = "datasets/kafka/consumer/testUserNotFoundTransaction.yml", cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/testUserNotFoundTransaction.yml", ignoreCols = {"id", "created"})
    void shouldReturn400whereTransactionOutcomeCompleted() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_TRANSACTION +
                        "TransactionConsumerOutcomeCompletedNotFoundException.json", TransactionEvent.class
        );

        testProducerService.send(topic, event);
    }

    @Test
    @DisplayName(START_HANDLE + " выплата в обработке")
    @DataSet(value = "datasets/kafka/consumer/TransactionForOutcomePending.yml", cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/TransactionOutcomePending.yml", ignoreCols = {"id", "created"})
    void shouldHandleTransactionOutcomePending() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_TRANSACTION +
                        "TransactionConsumerOutcomePending.json", TransactionEvent.class
        );

        log.info("Старт handle: {}", event);
        testProducerService.send(topic, event);
    }

    @Test
    @DisplayName("Пользователь не найден")
    @DataSet(value = "datasets/kafka/consumer/testUserNotFoundTransaction.yml", cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/testUserNotFoundTransaction.yml", ignoreCols = {"id", "created"})
    void shouldReturn400whereTransactionOutcomePending() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_TRANSACTION +
                        "TransactionConsumerOutcomePendingNotFoundException.json", TransactionEvent.class
        );

        testProducerService.send(topic, event);
    }
}