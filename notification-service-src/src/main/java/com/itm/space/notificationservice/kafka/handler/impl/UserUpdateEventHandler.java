package com.itm.space.notificationservice.kafka.handler.impl;

import com.itm.space.itmplatformcommonmodels.kafka.UserEvent;
import com.itm.space.itmplatformcommonmodels.kafka.enumeration.UserEventType;
import com.itm.space.notificationservice.kafka.handler.UserEventHandler;
import com.itm.space.notificationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserUpdateEventHandler implements UserEventHandler<UserEvent> {
    private final UserService userService;
    @Override
    public boolean isHandle(UserEvent event) {
        return event.type().equals(UserEventType.UPDATED);
    }

    @Override
    public void handle(UserEvent event) {
        userService.updateUser(event);
    }
}
