package com.itm.space.notificationservice.handler;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.itm.space.itmplatformcommonmodels.kafka.TransactionEvent;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.kafka.handler.impl.TransactionIncomeWaitingHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TransactionIncomeWaitingHandlerTest extends BaseIntegrationTest {

    @Autowired
    private TransactionIncomeWaitingHandler transactionIncomeWaitingHandler;

    private static final String TRANSACTION_EVENT_JSON_PATH = "json/handler/TransactionIncomeWaitingHandlerTest/TransactionIncomeEvent.json";

    @Test
    @DisplayName("true if type == INCOME and status == WAITING_FOR_USER")
    void shouldHandleWhenEventStatusIsWaitingForUser() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson
                (TRANSACTION_EVENT_JSON_PATH, TransactionEvent.class);
        boolean result = transactionIncomeWaitingHandler.isHandle(event);

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Создание нотификации и сумма amount в копейках с корректными данными")
    @DataSet(value = {"datasets/handler/TestUserWaiting.yml"})
    @ExpectedDataSet(value = {"datasets/handler/TransactionEventWaitingExpected.yml"}, ignoreCols = {"id", "created"})
    void shouldCreateNotificationWithCorrectData() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson
                (TRANSACTION_EVENT_JSON_PATH, TransactionEvent.class);

        transactionIncomeWaitingHandler.handle(event);
    }

    @Test
    @DisplayName("Данные пользователя не найдены, невозможно создать уведомление")
    @DataSet(value = {"datasets/handler/UserTestExceptionWaiting.yml"})
    @ExpectedDataSet(value = {"datasets/handler/UserTestExceptionWaiting.yml"})
    void shouldHandleAddNotProjection() {
        TransactionEvent event = jsonParserUtil.getObjectFromJson
                (TRANSACTION_EVENT_JSON_PATH, TransactionEvent.class);

        transactionIncomeWaitingHandler.handle(event);
    }

}


