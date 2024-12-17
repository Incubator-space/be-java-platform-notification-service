package com.itm.space.notificationservice.mapper;

import com.itm.space.itmplatformcommonmodels.kafka.UserEvent;
import com.itm.space.notificationservice.BaseUnitTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.domain.entity.User;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class UserMapperTest extends BaseUnitTest {

    @Test
    @SneakyThrows
    void shouldMapToUser() {
        UserEvent userEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.MAPPER_USER + "UserEvent.json",
                UserEvent.class
        );
        UserMapper underTest = Mappers.getMapper(UserMapper.class);
        User user = underTest.toUser(userEvent);

        Assertions.assertEquals("Joe", user.getFirstName());
        Assertions.assertEquals("Dassin", user.getLastName());
        Assertions.assertEquals("test@example.ru", user.getEmail());
        Assertions.assertFalse(user.isArchived());
    }
}