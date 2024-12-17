package com.itm.space.notificationservice.kafka.handler.impl;

import com.itm.space.itmplatformcommonmodels.kafka.UserEvent;
import com.itm.space.notificationservice.kafka.handler.UserEventHandler;
import com.itm.space.notificationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.itm.space.itmplatformcommonmodels.kafka.enumeration.UserEventType.DELETED;


@Component
@RequiredArgsConstructor
public class UserDeleteEventHandler implements UserEventHandler<UserEvent> {

    private final UserService userService;

    @Override
    public boolean isHandle(UserEvent event) {
        return event.type().equals(DELETED);
    }

    @Override
    public void handle(UserEvent event) {
        userService.deleteUser(event);
    }
}
