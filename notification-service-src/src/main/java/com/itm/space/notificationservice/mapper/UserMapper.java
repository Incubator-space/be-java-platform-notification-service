package com.itm.space.notificationservice.mapper;

import com.itm.space.itmplatformcommonmodels.kafka.UserEvent;
import com.itm.space.notificationservice.domain.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User toUser(UserEvent userEvent);
}
