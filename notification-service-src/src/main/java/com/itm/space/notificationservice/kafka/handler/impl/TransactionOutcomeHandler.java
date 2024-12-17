package com.itm.space.notificationservice.kafka.handler.impl;

import com.itm.space.itmplatformcommonmodels.kafka.TransactionEvent;
import com.itm.space.itmplatformcommonmodels.kafka.enums.TransactionType;
import com.itm.space.notificationservice.kafka.handler.TransactionEventHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class TransactionOutcomeHandler implements TransactionEventHandler<TransactionEvent> {

    @Override
    public boolean isHandle(TransactionEvent event) {
        return event != null && event.type() == TransactionType.OUTCOME;
    }

    @Override
    public void handle(TransactionEvent event) {

    }

}
