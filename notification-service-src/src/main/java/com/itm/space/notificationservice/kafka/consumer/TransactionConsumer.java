package com.itm.space.notificationservice.kafka.consumer;

import com.itm.space.itmplatformcommonmodels.kafka.TransactionEvent;
import com.itm.space.notificationservice.kafka.handler.TransactionEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Log4j2
@RequiredArgsConstructor
public class TransactionConsumer implements EventConsumer<ConsumerRecord<String, TransactionEvent>> {

    private final Set<TransactionEventHandler<TransactionEvent>> transactionHandlers;

    @Override
    @KafkaListener(topics = "#{'${spring.kafka.topic.transaction-events}'}")
    public void handle(ConsumerRecord<String, TransactionEvent> transactionEvent) {

        log.info("Обрабатывается сообщение из топика: {}, оффсет: {}", transactionEvent.topic(), transactionEvent.offset());

        TransactionEvent event = transactionEvent.value();

        transactionHandlers.stream()
                .filter(handler->handler.isHandle(event))
                .forEach(handler->handler.handle(event));
    }
}
