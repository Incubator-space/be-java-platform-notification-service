package com.itm.space.notificationservice.handler;


import com.itm.space.itmplatformcommonmodels.kafka.UserEvent;
import com.itm.space.notificationservice.BaseUnitTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.kafka.handler.impl.UserDeleteEventHandler;
import com.itm.space.notificationservice.service.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


class UserDeleteEventHandlerTest extends BaseUnitTest {
    @Mock
    UserService userService;
    @InjectMocks
    UserDeleteEventHandler userDeleteEventHandler;

    @Test
    @SneakyThrows
    void isHandleShouldReturnTrue() {
        UserEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_USER_DELETE + "UserEventDeleted.json",
                UserEvent.class
        );
        var isHandled = userDeleteEventHandler.isHandle(event);
        Assertions.assertTrue(isHandled);
    }

    @Test
    @SneakyThrows
    void isHandleShouldReturnFalse() {
        UserEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_USER_DELETE + "UserEventNotDeleted.json",
                UserEvent.class
        );
        var isHandled = userDeleteEventHandler.isHandle(event);
        Assertions.assertFalse(isHandled);
    }

    @Test
    @SneakyThrows
    void handleShouldDelete() {
        UserEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_USER_DELETE + "UserEventDeleted.json",
                UserEvent.class
        );
        userDeleteEventHandler.handle(event);
        verify(userService, times(1)).deleteUser(event);
    }
}
