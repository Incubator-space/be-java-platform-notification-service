package com.itm.space.notificationservice.handler;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.itm.space.itmplatformcommonmodels.kafka.TransactionEvent;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.kafka.handler.impl.TransactionOutcomeCompletedHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TransactionOutcomeCompletedHandlerTest extends BaseIntegrationTest {

    @Autowired
    private TransactionOutcomeCompletedHandler transactionOutcomeCompletedHandler;

    private static final String TRANSACTION_EVENT_JSON_PATH = "json/handler/TransactionOutcomeCompletedHandlerTest/TransactionOutcomeEvent.json";

    @Test
    @DisplayName("true if type == OUTCOME and status == COMPLETED")
    void shouldHandleWhenEventStatusIsCompleted() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson
                (TRANSACTION_EVENT_JSON_PATH, TransactionEvent.class);
        boolean result = transactionOutcomeCompletedHandler.isHandle(event);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Создание нотификации \"Выплата проведена\" и сумма amount в копейках с корректными данными")
    @DataSet(value = {"datasets/handler/TestUserCompleted.yml"})
    @ExpectedDataSet(value = {"datasets/handler/TransactionEventCompletedExpected.yml"}, ignoreCols = {"id", "created"})
    void shouldCreateNotificationWithCorrectData() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson
                (TRANSACTION_EVENT_JSON_PATH, TransactionEvent.class);

        transactionOutcomeCompletedHandler.handle(event);
    }

    @Test
    @DisplayName("Данные пользователя не найдены, невозможно создать уведомление")
    @DataSet(value = {"datasets/handler/UserTestExceptionWaiting.yml"})
    @ExpectedDataSet(value = {"datasets/handler/UserTestExceptionWaiting.yml"})
    void shouldHandleAddNotProjection() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson
                (TRANSACTION_EVENT_JSON_PATH, TransactionEvent.class);

        transactionOutcomeCompletedHandler.handle(event);
    }
}
