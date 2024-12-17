package com.itm.space.notificationservice.kafka.consumer;

public interface EventConsumer<E> {

    void handle(E e);

}