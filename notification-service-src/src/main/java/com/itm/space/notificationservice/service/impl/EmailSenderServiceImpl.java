package com.itm.space.notificationservice.service.impl;

import com.itm.space.notificationservice.exception.BadRequestException;
import com.itm.space.notificationservice.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender javaMailSender;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @Override
    public void sendEmail(String to, String subject, String message) {
        if (!to.matches(EMAIL_REGEX)) {
            throw new BadRequestException("Невалидный адрес электронной почты");
        }
        if (subject == null || message == null) {
            throw new BadRequestException("Заголовок и сообщение не могут быть пустыми");
        }
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("example@itm.com");
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
    }
}