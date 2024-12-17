package com.itm.space.notificationservice.service.impl;

import com.itm.space.notificationservice.api.request.EmailRequest;
import com.itm.space.notificationservice.api.response.EmailResponse;
import com.itm.space.notificationservice.domain.entity.Email;
import com.itm.space.notificationservice.mapper.EmailMapper;
import com.itm.space.notificationservice.repository.EmailRepository;
import com.itm.space.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final EmailRepository emailRepository;
    private final EmailMapper emailMapper;

    @Override
    public EmailResponse create(EmailRequest emailAddress) {
        Email email = emailMapper.toEmail(emailAddress);
        email = emailRepository.save(email);
        return emailMapper.toEmailResponse(email);
    }
}
