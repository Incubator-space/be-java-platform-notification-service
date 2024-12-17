package com.itm.space.notificationservice.handler;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.itm.space.itmplatformcommonmodels.kafka.BankCardEvent;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.kafka.handler.impl.BankCardRemoveHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;



    class BankCardRemoveHandlerTest extends BaseIntegrationTest {

        @Autowired
        private BankCardRemoveHandler bankCardRemovehandler;

        @Test
        @DisplayName("true если карта была удалена")
        void shouldHandleWhenEventTypeIsRemove() {
            BankCardEvent event = jsonParserUtil.getObjectFromJson
                    ("json/handler/BankCardRemoveHandlerTest/BankCardEventRemoveHandler.json", BankCardEvent.class);
            boolean result = bankCardRemovehandler.isHandle(event);

            assertTrue(result);
        }

        @Test
        @DisplayName("Создание нотификации с корректными данными")
        @DataSet(value = "datasets/handler/TestUserBankCardRemove.yml", cleanAfter = true, cleanBefore = true)
        @ExpectedDataSet(value = "datasets/handler/BankCardRemoveExpected.yml", ignoreCols = {"id", "created"})
        void shouldHandleAddNotification() {
            BankCardEvent event = jsonParserUtil.getObjectFromJson
                    ("json/handler/BankCardAddHandlerTest/BankCardEventAddHandler.json", BankCardEvent.class);

            bankCardRemovehandler.handle(event);
        }

        @Test
        @DisplayName("Когда проекция не найдена")
        @DataSet(value = "datasets/handler/UserTestExceptionBankCardRemove.yml", cleanAfter = true, cleanBefore = true)
        @ExpectedDataSet(value = "datasets/handler/UserTestExceptionBankCardRemove.yml")
        void shouldHandleAddNotProjection() {
            BankCardEvent event = jsonParserUtil.getObjectFromJson
                    ("json/handler/BankCardAddHandlerTest/BankCardEventAddHandler.json", BankCardEvent.class);

            bankCardRemovehandler.handle(event);
        }
    }


