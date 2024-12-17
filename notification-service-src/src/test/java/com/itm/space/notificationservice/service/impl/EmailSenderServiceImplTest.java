package com.itm.space.notificationservice.service.impl;

import com.itm.space.notificationservice.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailSenderServiceImplTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailSenderServiceImpl emailSenderService;

    @Test
    void sendEmail_ValidEmail_Success() {
        String to = "example@example.com";
        String subject = "Test subject";
        String message = "Test message";

        emailSenderService.sendEmail(to, subject, message);

        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_InvalidEmail_ThrowsBadRequestException() {
        String to = "example.com";
        String subject = "Test subject";
        String message = "Test message";

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            emailSenderService.sendEmail(to, subject, message);
        });

        assertThat(exception.getMessage()).isEqualTo("Невалидный адрес электронной почты");
    }

    @Test
    void sendEmail_NullSubject_ThrowsBadRequestException() {
        String to = "example@example.com";
        String subject = null;
        String message = "Test message";

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            emailSenderService.sendEmail(to, subject, message);
        });

        assertThat(exception.getMessage()).isEqualTo("Заголовок и сообщение не могут быть пустыми");
    }

    @Test
    void sendEmail_NullMessage_ThrowsBadRequestException() {
        String to = "example@example.com";
        String subject = "Test subject";
        String message = null;

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            emailSenderService.sendEmail(to, subject, message);
        });

        assertThat(exception.getMessage()).isEqualTo("Заголовок и сообщение не могут быть пустыми");
    }
}
