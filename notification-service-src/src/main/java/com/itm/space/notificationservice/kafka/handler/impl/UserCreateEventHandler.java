package com.itm.space.notificationservice.kafka.handler.impl;

import com.itm.space.itmplatformcommonmodels.kafka.UserEvent;
import com.itm.space.notificationservice.kafka.handler.UserEventHandler;
import com.itm.space.notificationservice.mapper.UserMapper;
import com.itm.space.notificationservice.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.itm.space.itmplatformcommonmodels.kafka.enumeration.UserEventType.CREATED;


@Component
@RequiredArgsConstructor
public class UserCreateEventHandler implements UserEventHandler<UserEvent> {

    private final UserService userService;

    private final UserMapper userMapper;

    @Override
    public boolean isHandle(UserEvent event) {
        return event.type() == CREATED;
    }

    @Override
    public void handle(UserEvent event) {
        userService.createUser(userMapper.toUser(event));
    }
}

