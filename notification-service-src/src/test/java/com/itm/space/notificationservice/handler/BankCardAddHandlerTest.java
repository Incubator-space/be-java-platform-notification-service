package com.itm.space.notificationservice.handler;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.itm.space.itmplatformcommonmodels.kafka.BankCardEvent;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.kafka.handler.impl.BankCardAddHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BankCardAddHandlerTest extends BaseIntegrationTest {

    @Autowired
    private BankCardAddHandler bankCardAddHandler;

    private static final String BANK_CARD_EVENT_JSON_PATH = "json/handler/BankCardAddHandlerTest/BankCardEventAddHandler.json";

    @Test
    @DisplayName("true если карта была добавлена")
    void shouldHandleWhenEventTypeIsAdd() {
        BankCardEvent event = jsonParserUtil.getObjectFromJson
                (BANK_CARD_EVENT_JSON_PATH, BankCardEvent.class);
        boolean result = bankCardAddHandler.isHandle(event);

        assertTrue(result);
    }

    @Test
    @DisplayName("Создание нотификации с корректными данными")
    @DataSet(value = {"datasets/handler/TestUserBankCardAdd.yml"})
    @ExpectedDataSet(value = {"datasets/handler/BankCardAddExpected.yml"}, ignoreCols = {"id", "created"})
    void shouldHandleAddNotification() {
        BankCardEvent event = jsonParserUtil.getObjectFromJson
                (BANK_CARD_EVENT_JSON_PATH, BankCardEvent.class);

        bankCardAddHandler.handle(event);
    }

    @Test
    @DisplayName("Данные пользователя не найдены, невозможно создать уведомление")
    @DataSet(value = {"datasets/handler/UserTestExceptionBankCardAdd.yml"})
    @ExpectedDataSet(value = {"datasets/handler/UserTestExceptionBankCardAdd.yml"})
    void shouldHandleAddNotProjection() {
        BankCardEvent event = jsonParserUtil.getObjectFromJson
                (BANK_CARD_EVENT_JSON_PATH, BankCardEvent.class);

        bankCardAddHandler.handle(event);
    }
}
