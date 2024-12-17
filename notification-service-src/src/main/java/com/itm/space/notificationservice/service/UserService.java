package com.itm.space.notificationservice.service;

import com.itm.space.itmplatformcommonmodels.kafka.UserEvent;
import com.itm.space.notificationservice.domain.entity.User;
import com.itm.space.notificationservice.repository.UserProjection;

import java.util.UUID;


public interface UserService {
    User createUser(User user);

    void updateUser(UserEvent userevent);

    void deleteUser(UserEvent userEvent);

    UserProjection findProjectionById(UUID id);
}
