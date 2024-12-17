package com.itm.space.notificationservice.controller.admin;

import com.itm.space.notificationservice.api.request.NotificationRequest;
import com.itm.space.notificationservice.api.response.NotificationResponse;
import com.itm.space.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.itm.space.notificationservice.controller.ApiConstants.NOTIFICATIONS_ADMIN_API_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(NOTIFICATIONS_ADMIN_API_PATH)
public class NotificationAdminControllerImpl implements NotificationAdminController {

    private final NotificationService notificationService;

    @Override
    @PostMapping
    public NotificationResponse addNotification(@RequestBody @Valid NotificationRequest notification) {
        return notificationService.saveNotificationAndReturnResponse(notification);
    }
}
