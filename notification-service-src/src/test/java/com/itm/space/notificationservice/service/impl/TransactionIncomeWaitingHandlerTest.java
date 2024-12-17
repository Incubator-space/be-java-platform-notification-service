package com.itm.space.notificationservice.service.impl;

import com.itm.space.itmplatformcommonmodels.kafka.UserEvent;
import com.itm.space.notificationservice.BaseUnitTest;
import com.itm.space.notificationservice.domain.entity.User;
import com.itm.space.notificationservice.exception.NotFoundException;
import com.itm.space.notificationservice.mapper.UserMapper;
import com.itm.space.notificationservice.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionIncomeWaitingHandlerTest extends BaseUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl underTest;

    @SneakyThrows
    @Test
    @DisplayName("Должен доставать юзера")
    void shouldUpdateUser() {
        UserEvent updatedUser = jsonParserUtil.getObjectFromJson
                ("json/service/UserServiceImplTest/UserEvent.json",
                        UserEvent.class
                );

        User preparedUser = User.builder()
                .firstName("Joe")
                .lastName("Dassin")
                .email("test@example.ru")
                .isArchived(false)
                .build();
        Mockito.when(userMapper.toUser(Mockito.any(UserEvent.class))).thenReturn(preparedUser);

        underTest.updateUser(updatedUser);
        verify(userRepository, times(1))
                .save(Mockito.any(User.class));
    }

    @Test

    @DisplayName("Должен бросить NotFoundException при отсутствии проекции")
    void shouldNotFoundException() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findProjectionById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            underTest.findProjectionById(userId);
        });
    }
}
