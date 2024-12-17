package com.itm.space.notificationservice.controller;

import com.itm.space.notificationservice.api.request.ViewNotificationsRequest;
import com.itm.space.notificationservice.api.response.NotificationResponse;
import com.itm.space.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static com.itm.space.notificationservice.controller.ApiConstants.NOTIFICATIONS_API_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(NOTIFICATIONS_API_PATH)
public class NotificationControllerImpl implements NotificationController {

    private final NotificationService notificationService;

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<NotificationResponse> getUnreads(Authentication authentication) {
        return notificationService.getUnreads(UUID.fromString(authentication.getName())).stream()
                .sorted(Comparator.comparing(NotificationResponse::getCreated))
                .toList();
    }

    @Override
    @GetMapping("/history")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<NotificationResponse>> getViewedNotifications(Authentication authentication) {
        return new ResponseEntity<>(notificationService.getViewedNotifications(UUID.fromString(authentication.getName())), HttpStatus.OK);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<NotificationResponse> viewNotification(@PathVariable("id") UUID id, Authentication authentication) {
        return ResponseEntity.ok(notificationService.viewNotification(id, UUID.fromString(authentication.getName())));
    }

    @PatchMapping
    public void viewNotifications(@RequestBody ViewNotificationsRequest request) {
        notificationService.viewNotifications(request);
    }

    @Override
    @GetMapping("/counter")
    @ResponseStatus(HttpStatus.OK)
    public Long getUnreadNotificationsCount(Authentication authentication) {
        return notificationService.getUnreadNotificationsCount(UUID.fromString(authentication.getName()));
    }
}