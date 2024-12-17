package com.itm.space.notificationservice.kafka.handler.impl;

import com.itm.space.itmplatformcommonmodels.kafka.BankCardEvent;
import com.itm.space.itmplatformcommonmodels.kafka.enums.BankCardEventType;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.exception.NotFoundException;
import com.itm.space.notificationservice.kafka.handler.BankCardHandler;
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
public class BankCardRemoveHandler implements BankCardHandler<BankCardEvent> {

    private final NotificationMapper notificationMapper;
    private final NotificationService notificationService;
    private final UserService userService;

    @Override
    public boolean isHandle(BankCardEvent event) {
        return event.type() == BankCardEventType.REMOVE;
    }

    @Transactional
    @Override
    public void handle(BankCardEvent event) {
        try {
            var user = userService.findProjectionById(event.userId());
            Notification notification = notificationMapper.newMessageBankCardWhereRemove(event, user);
            notificationService.saveNotification(notification);
        } catch (NotFoundException e) {
            log.error(String.format("Данные по пользователю с ID = %s не найдены, невозможно создать уведомление", event.userId().toString()));
        }
    }

}