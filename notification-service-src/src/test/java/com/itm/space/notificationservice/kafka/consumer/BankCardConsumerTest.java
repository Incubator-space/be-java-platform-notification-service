package com.itm.space.notificationservice.kafka.consumer;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.itm.space.itmplatformcommonmodels.kafka.BankCardEvent;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

class BankCardConsumerTest extends BaseIntegrationTest {

    @Value("#{'${spring.kafka.topic.bank-card-events}'}")
    private String topic;

    @Test
    @DisplayName("Должен вызвать handler для сохранения уведомления о удалении банковской карты")
    @DataSet(value = "datasets/kafka/consumer/BankCardsForRemove.yml", cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/BankCardRemove.yml", ignoreCols = {"id", "created"})
     void shouldHandleRemoveBankCard(){
        BankCardEvent bankCardEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_BANKCARD + "BankCardConsumerRemoveBankCard.json",
                BankCardEvent.class
        );

        testProducerService.send(topic, bankCardEvent);
    }

    @Test
    @DisplayName("Должен вызвать handler для создания уведомления о добавлении банковской карты")
    @DataSet(value = "datasets/kafka/consumer/BankCardsForAdd.yml", cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/BankCardConsumerAdd.yml", ignoreCols = {"id", "created"})
     void shouldHandleAddBankCard(){
        BankCardEvent bankCardEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_BANKCARD + "BankCardConsumerAddBankCard.json",
                BankCardEvent.class
        );

        testProducerService.send(topic, bankCardEvent);
    }

    @Test
    @DisplayName("Должен выбрасывать ошибку 404, когда пользователь не найден при удалении карты")
    @DataSet(value = "datasets/kafka/consumer/TestUserNotFoundBankCard.yml", cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/TestUserNotFoundBankCard.yml")
    void shouldReturn404WhereRemoveBankCard(){
        BankCardEvent bankCardEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_BANKCARD + "BankCardConsumerExceptionWhereRemove.json",
                BankCardEvent.class
        );

        testProducerService.send(topic, bankCardEvent);

    }

    @Test
    @DisplayName("Должен выбрасывать ошибку 404, когда пользователь не найден при добавлении карты")
    @DataSet(value = "datasets/kafka/consumer/TestUserNotFoundBankCard.yml", cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(value = "datasets/kafka/consumer/TestUserNotFoundBankCard.yml")
    void shouldReturn404WhereAddBankCard(){
        BankCardEvent bankCardEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.KAFKA_CONSUMER_BANKCARD + "BankCardConsumerExceptionWhereAdd.json",
                BankCardEvent.class
        );

        testProducerService.send(topic, bankCardEvent);
    }
}
