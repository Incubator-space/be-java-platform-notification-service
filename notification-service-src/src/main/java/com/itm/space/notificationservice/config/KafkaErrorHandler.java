package com.itm.space.notificationservice.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

@Slf4j
class KafkaErrorHandler implements CommonErrorHandler {

    @Override
    public boolean handleOne(Exception exception, ConsumerRecord<?, ?> record, Consumer<?, ?> consumer, MessageListenerContainer container) {
        log.warn("Unable to process record on {} topic, {} partition, {} offset. Record value: {}. Error message {}",
                record.topic(), record.partition(), record.offset(), record.value(), exception.getMessage());
        consumer.seek(new TopicPartition(record.topic(), record.partition()), record.offset() + 1L);
        consumer.commitSync();
        return true;
    }

}
