package com.itm.space.notificationservice.handler;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.itm.space.itmplatformcommonmodels.kafka.TransactionEvent;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.kafka.handler.impl.TransactionOutcomePendingHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TransactionOutcomePendingHandlerTest extends BaseIntegrationTest {

    @Autowired
    private TransactionOutcomePendingHandler transactionOutcomePendingHandler;

    private static final String TRANSACTION_EVENT_JSON_PATH = "json/handler/TransactionOutcomePendingHandlerTest/TransactionEventTestValue.json";

    @Test
    @DisplayName("true если type == INCOME и status == PENDING")
    void shouldHandleWhenEventStatusIsPending() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson
                (TRANSACTION_EVENT_JSON_PATH, TransactionEvent.class);
        boolean result = transactionOutcomePendingHandler.isHandle(event);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Создание нотификации \"Платеж в обработке\" с корректными данными")
    @DataSet(value = {"datasets/handler/TestUserPending.yml"})
    @ExpectedDataSet(value = {"datasets/handler/TransactionEventOutcomePendingExpected.yml"}, ignoreCols = {"id", "created"})
    void shouldCreateNotificationWithCorrectData() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson
                (TRANSACTION_EVENT_JSON_PATH, TransactionEvent.class);

        transactionOutcomePendingHandler.handle(event);
    }

    @Test
    @DisplayName("Данные пользователя не найдены, невозможно создать уведомление")
    @DataSet(value = {"datasets/handler/UserTestExceptionPending.yml"})
    @ExpectedDataSet(value = {"datasets/handler/UserTestExceptionPending.yml"})
    void shouldHandleAddNotProjection() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson
                (TRANSACTION_EVENT_JSON_PATH, TransactionEvent.class);

        transactionOutcomePendingHandler.handle(event);
    }
}
