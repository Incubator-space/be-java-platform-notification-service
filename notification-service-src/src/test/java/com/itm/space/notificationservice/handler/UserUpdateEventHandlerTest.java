package com.itm.space.notificationservice.handler;

import com.itm.space.itmplatformcommonmodels.kafka.UserEvent;
import com.itm.space.notificationservice.BaseUnitTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.kafka.handler.impl.UserUpdateEventHandler;
import com.itm.space.notificationservice.service.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

class UserUpdateEventHandlerTest extends BaseUnitTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserUpdateEventHandler underTest;

    @Test
    @SneakyThrows
    void shouldUpdateUser() {
        UserEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_USER_UPDATE + "UserEventUpdated.json",
                UserEvent.class
        );
        underTest.handle(event);
        Mockito.verify(userService, times(1)).updateUser(event);
    }

    @Test
    @SneakyThrows
    void shouldReturnTrueIfUserEventTypeIsUpdate() {
        UserEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_USER_UPDATE + "UserEventUpdated.json",
                UserEvent.class
        );
        assertThat(underTest.isHandle(event)).isTrue();
    }

    @Test
    @SneakyThrows
    void shouldReturnFalseIfUserEventTypeIsNotUpdate() {
        UserEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_USER_UPDATE + "UserEventNotUpdated.json",
                UserEvent.class
        );
        assertThat(underTest.isHandle(event)).isFalse();
    }
}