package com.itm.space.notificationservice.handler;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.itm.space.itmplatformcommonmodels.kafka.TransactionEvent;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.kafka.handler.impl.TransactionOutcomeCancelledHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TransactionOutcomeCancelledHandlerTest extends BaseIntegrationTest {

    @Autowired
    TransactionOutcomeCancelledHandler transactionOutcomeCancelledHandler;

    private static final String TRANSACTION_EVENT_JSON_PATH = "json/handler/TransactionOutcomeCancelledHandlerTest/TransactionOutcomeEvent.json";

    @Test
    @DisplayName("true if type == OUTCOME and status == CANCELLED")
    void shouldHandleWhenEventStatusIsCancelled() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson
                (TRANSACTION_EVENT_JSON_PATH, TransactionEvent.class);
        boolean result = transactionOutcomeCancelledHandler.isHandle(event);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Создание нотификации \"Выплата не прошла\" и сумма amount в копейках с корректными данными")
    @DataSet(value = {"datasets/handler/TestUserCancelled.yml"})
    @ExpectedDataSet(value = {"datasets/handler/TransactionEventOutcomeCancelledExpected.yml"}, ignoreCols = {"id", "created"})
    void shouldCreateNotificationWithCorrectData() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson
                (TRANSACTION_EVENT_JSON_PATH, TransactionEvent.class);

        transactionOutcomeCancelledHandler.handle(event);
    }

    @Test
    @DisplayName("Данные пользователя не найдены, невозможно создать уведомление")
    @DataSet(value = {"datasets/handler/UserTestExceptionCancelled.yml"})
    @ExpectedDataSet(value = {"datasets/handler/UserTestExceptionCancelled.yml"})
    void shouldHandleAddNotProjection() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson
                (TRANSACTION_EVENT_JSON_PATH, TransactionEvent.class);

        transactionOutcomeCancelledHandler.handle(event);
    }
}
