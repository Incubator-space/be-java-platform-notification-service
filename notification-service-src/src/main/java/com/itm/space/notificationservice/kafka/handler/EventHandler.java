package com.itm.space.notificationservice.kafka.handler;

public interface EventHandler<T> {
    boolean isHandle(T event);
    void handle(T event);
}
