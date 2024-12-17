package com.itm.space.notificationservice.service;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String message) throws Exception;
}
