package com.itm.space.notificationservice.service.impl;

import com.itm.space.itmplatformcommonmodels.kafka.UserEvent;
import com.itm.space.notificationservice.domain.entity.User;
import com.itm.space.notificationservice.exception.NotFoundException;
import com.itm.space.notificationservice.mapper.UserMapper;
import com.itm.space.notificationservice.repository.UserProjection;
import com.itm.space.notificationservice.repository.UserRepository;
import com.itm.space.notificationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void updateUser(UserEvent event) {
        User user = userMapper.toUser(event);

        userRepository.save(user);
    }

    @Override
    public void deleteUser(UserEvent userEvent) {
        userRepository.delete(userMapper.toUser(userEvent));
    }

    @Override
    public UserProjection findProjectionById(UUID id) {
        var userProjection = userRepository.findProjectionById(id);
        if (userProjection.isEmpty()) {
            log.info("User not found by userId: {}",
                    id);
            throw new NotFoundException("User not found by userId: " + id);
        }
        return userProjection.get();
    }
}