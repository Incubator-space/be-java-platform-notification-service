package com.itm.space.notificationservice.service.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.Semaphore;

/**
 * A service class to produce and control messages for Kafka.
 * The class uses Aspect Oriented Programming (AOP) to manage the sending of messages.
 * It also uses a Semaphore to control access to the KafkaTemplate for sending messages.
 */
@Aspect
@Service
@Getter
@RequiredArgsConstructor
public class TestProducerService {
    /**
     * KafkaTemplate for sending messages to a Kafka topic.
     */
    private final KafkaTemplate<String, Object> kafkaTemplate;
    /**
     * Uses for waiting event execution on listener side
     */
    private final Semaphore semaphore = new Semaphore(1);

    /**
     * Sends a given event to the provided topic and acquires a permit from the semaphore, blocking until listener ending execution.
     *
     * @param topic the Kafka topic to send the event to.
     * @param event the event to be sent.
     */
    @SneakyThrows
    public void send(String topic, Object event) {
        kafkaTemplate.send(topic, event);
        semaphore.acquire();
    }

    /**
     * Releases a permit, returning it to the semaphore, upon successful execution of the method
     * com.itm.space.notificationservice.kafka.consumer.EventConsumer.handle().
     */
    @SneakyThrows
    @After(
            value = "execution(public * com.itm.space.notificationservice.kafka.consumer.EventConsumer.handle(..))")
    public void beforeReceiveMessage() {
        semaphore.release(1);
    }
}
