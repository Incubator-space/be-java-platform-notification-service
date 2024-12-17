package com.itm.space.notificationservice.handler;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.itm.space.itmplatformcommonmodels.kafka.TransactionEvent;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.kafka.handler.impl.TransactionIncomeCancelledHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TransactionIncomeCancelledHandlerTest extends BaseIntegrationTest {

    @Autowired
    private TransactionIncomeCancelledHandler transactionIncomeCancelledHandler;

    private static final String TRANSACTION_EVENT_JSON_PATH = "json/handler/TransactionIncomeCancelledHandlerTest/TransactionIncomeEvent.json";

    @Test
    @DisplayName("true если type == INCOME и status == CANCELLED")
    void shouldHandleWhenEventStatusIsCancelled() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson
                (TRANSACTION_EVENT_JSON_PATH, TransactionEvent.class);
        boolean result = transactionIncomeCancelledHandler.isHandle(event);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Создание нотификации. Платеж не прошел с корректными данными")
    @DataSet(value = {"datasets/handler/TestUserCancelled.yml"})
    @ExpectedDataSet(value = {"datasets/handler/TransactionEventCancelledExpected.yml"}, ignoreCols = {"id", "created"})
    void shouldCreateNotificationWithCorrectData() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson
                (TRANSACTION_EVENT_JSON_PATH, TransactionEvent.class);

        transactionIncomeCancelledHandler.handle(event);
    }

    @Test
    @DisplayName("Данные пользователя не найдены, невозможно создать уведомление")
    @DataSet(value = {"datasets/handler/UserTestExceptionCancelled.yml"})
    @ExpectedDataSet(value = {"datasets/handler/UserTestExceptionCancelled.yml"})
    void shouldHandleAddNotProjection() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson
                (TRANSACTION_EVENT_JSON_PATH, TransactionEvent.class);

        transactionIncomeCancelledHandler.handle(event);
    }
}
