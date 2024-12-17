package com.itm.space.notificationservice.controller;

import com.itm.space.notificationservice.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.itm.space.notificationservice.api.response.EmailResponse;
import com.itm.space.notificationservice.api.request.EmailRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/emails")
public class EmailControllerImpl implements EmailController{

    private final EmailService emailService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmailResponse create(@RequestBody @Valid EmailRequest emailAddress) {
        return emailService.create(emailAddress);
    }
}
