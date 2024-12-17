package com.itm.space.notificationservice.kafka.consumer;

import com.itm.space.itmplatformcommonmodels.kafka.BankCardEvent;
import com.itm.space.notificationservice.kafka.handler.BankCardHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Log4j2
@RequiredArgsConstructor
public class BankCardConsumer implements EventConsumer<ConsumerRecord<String, BankCardEvent>> {

    private final Set<BankCardHandler<BankCardEvent>> bankCardHandlers;

    @Override
    @KafkaListener(topics = "#{'${spring.kafka.topic.bank-card-events}'}")
    public void handle(ConsumerRecord<String, BankCardEvent> bankCardEvent) {

        log.info("Обрабатывается сообщение из топика: {}, оффсет: {}", bankCardEvent.topic(), bankCardEvent.offset());

        BankCardEvent event = bankCardEvent.value();

        bankCardHandlers.stream()
                .filter(handler -> handler.isHandle(event))
                .forEach(handler -> handler.handle(event));
    }
}
