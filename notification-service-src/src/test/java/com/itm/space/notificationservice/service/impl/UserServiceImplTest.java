package com.itm.space.notificationservice.service.impl;

import com.itm.space.itmplatformcommonmodels.kafka.UserEvent;
import com.itm.space.notificationservice.BaseUnitTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.domain.entity.User;
import com.itm.space.notificationservice.exception.NotFoundException;
import com.itm.space.notificationservice.mapper.UserMapper;
import com.itm.space.notificationservice.repository.UserProjection;
import com.itm.space.notificationservice.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest extends BaseUnitTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl underTest;

    @SneakyThrows
    @Test
    void shouldUpdateUser() {
        UserEvent updatedUser = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.SERVICE_USER + "UserEvent.json",
                UserEvent.class
        );

        Mockito.when(userMapper.toUser(Mockito.any(UserEvent.class))).thenReturn(User.builder()
                .firstName("Joe")
                .lastName("Dassin")
                .email("test@example.ru")
                .isArchived(false)
                .build());

        underTest.updateUser(updatedUser);
        verify(userRepository, times(1))
                .save(Mockito.any(User.class));
    }

    @Test
    @DisplayName("Тест на корректное извлечение имени и фамилии")
    void shouldFindProjectionById() {
        UUID id = UUID.randomUUID();
        UserProjection userProjection = new UserProjection() {
            public String getFirstName() {
                return "Петр";
            }

            public String getLastName() {
                return "Петров";
            }
        };

        when(userRepository.findProjectionById(id)).thenReturn(Optional.of(userProjection));

        UserProjection result = underTest.findProjectionById(id);

        assertNotNull(result);
        assertEquals("Петр", result.getFirstName());
        assertEquals("Петров", result.getLastName());
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
