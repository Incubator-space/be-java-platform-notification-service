package com.itm.space.notificationservice.handler;

import com.itm.space.itmplatformcommonmodels.kafka.UserEvent;
import com.itm.space.notificationservice.BaseUnitTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.domain.entity.User;
import com.itm.space.notificationservice.kafka.handler.impl.UserCreateEventHandler;
import com.itm.space.notificationservice.mapper.UserMapper;
import com.itm.space.notificationservice.service.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.itm.space.itmplatformcommonmodels.kafka.enumeration.UserEventType.CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserCreateEventHandlerTest extends BaseUnitTest {
    @Mock
    UserService userService;
    @Mock
    UserMapper userMapper;
    @InjectMocks
    UserCreateEventHandler userCreateEventHandler;

    @Test
    @SneakyThrows
    void isHandleShouldReturnTrue() {
        UserEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_USER_CREATE + "UserEventCreated.json",
                UserEvent.class
        );
        var isHandled = userCreateEventHandler.isHandle(event);
        Assertions.assertTrue(isHandled);
    }

    @Test
    @SneakyThrows
    void isHandleShouldReturnFalse() {
        UserEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_USER_CREATE + "UserEventNotCreated.json",
                UserEvent.class
        );
        var isHandled = userCreateEventHandler.isHandle(event);
        Assertions.assertFalse(isHandled);
    }

    @Test
    @SneakyThrows
    void handleShouldCreate() {
        UserEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_USER_CREATE + "UserEventCreated.json",
                UserEvent.class
        );
        User user = new User();
        when(userMapper.toUser(event)).thenReturn(user);
        userCreateEventHandler.handle(event);
        assertEquals(CREATED, event.type());
        verify(userService, times(1)).createUser(user);
    }
}
