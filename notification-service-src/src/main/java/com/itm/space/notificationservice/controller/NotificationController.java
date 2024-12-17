package com.itm.space.notificationservice.controller;

import com.itm.space.notificationservice.api.request.ViewNotificationsRequest;
import com.itm.space.notificationservice.api.response.NotificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Tag(name = "Notification Controller", description = "CRUD операции с уведомлениями")
public interface NotificationController {
    @Operation(summary = "Отсортированные по признаку прочтения и по дате создания", description = "Все нотификации пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение непрочитанных уведомлений"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "404", description = "Пользователя нет в базе данных уведомлений"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")})
    @GetMapping
    List<NotificationResponse> getUnreads(Authentication authentication);

    @Operation(summary = "Getting read notifications", description = "Получение прочитанных уведомлений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение прочитанных уведомлений"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера"),
    })
    @GetMapping("/history")
    ResponseEntity<List<NotificationResponse>> getViewedNotifications(Authentication authentication);

    @Operation(summary = "Обновление статуса уведомления", description = "Обновление статуса уведомления по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное обновление статуса уведомления"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "404", description = "Уведомление не найдено"),
            @ApiResponse(responseCode = "409", description = "Просмотреть можно только свою нотификацию"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PatchMapping("/{id}")
    ResponseEntity<NotificationResponse> viewNotification(@PathVariable UUID id, Authentication authentication);

    @Operation(summary = "Обновление статуса уведомления на VIEWED", description = "Обновление статуса уведомления на VIEWED по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное обновление статуса уведомления на VIEWED"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "404", description = "Нотификация/нотификации не найдены"),
            @ApiResponse(responseCode = "409", description = "Нельзя просмотреть чужую нотификацию/нотификации"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера"),
    })
    @PatchMapping
    void viewNotifications(@RequestBody ViewNotificationsRequest request);

    @GetMapping
    Long getUnreadNotificationsCount(Authentication authentication);
}