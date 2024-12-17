package com.itm.space.notificationservice.kafka.handler;

import org.springframework.stereotype.Repository;

@Repository
public interface TaskCommentEventHandler<T> extends EventHandler<T> {

}
