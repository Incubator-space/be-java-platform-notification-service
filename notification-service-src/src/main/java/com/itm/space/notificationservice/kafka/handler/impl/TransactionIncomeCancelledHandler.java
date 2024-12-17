package com.itm.space.notificationservice.kafka.handler.impl;

import com.itm.space.itmplatformcommonmodels.kafka.TransactionEvent;
import com.itm.space.itmplatformcommonmodels.kafka.enums.TransactionStatus;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.exception.NotFoundException;
import com.itm.space.notificationservice.mapper.NotificationMapper;
import com.itm.space.notificationservice.service.NotificationService;
import com.itm.space.notificationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionIncomeCancelledHandler extends TransactionIncomeHandler {

    private final NotificationMapper notificationMapper;

    private final NotificationService notificationService;

    private final UserService userService;

    @Override
    public boolean isHandle(TransactionEvent event) {
        return super.isHandle(event) && event.status() == TransactionStatus.CANCELLED;
    }

    @Override
    @Transactional
    public void handle(TransactionEvent event) {
        try {
            var user = userService.findProjectionById(event.userId());
            Notification notification = notificationMapper.transactionIncomeCancelledToNotification(event, user);

            notificationService.saveNotification(notification);
        } catch (NotFoundException e) {
            log.error(String.format("Данные по пользователю с ID = %s не найдены, невозможно создать уведомление"
                    , event.userId().toString()));
        }
    }
}