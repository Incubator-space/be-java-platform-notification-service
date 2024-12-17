package com.itm.space.notificationservice.service;

import com.itm.space.notificationservice.api.request.EmailRequest;
import com.itm.space.notificationservice.api.response.EmailResponse;

public interface EmailService {
     EmailResponse create(EmailRequest emailAddress);
}
