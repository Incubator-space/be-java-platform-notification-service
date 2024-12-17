package com.itm.space.notificationservice.controller.admin;

import com.itm.space.notificationservice.api.request.NotificationRequest;
import com.itm.space.notificationservice.api.response.NotificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Notification Admin Controller", description = "CRUD операции с уведомлениями доступные админу")
public interface NotificationAdminController {
    @Operation(summary = "create", description = "Создание нового уведомления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное добавление уведомления"),
            @ApiResponse(responseCode = "400", description = "Недопустимый запрос"),
            @ApiResponse(responseCode = "401", description = "В доступе отказано"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера")
    })
    @PostMapping()
    @Secured("ROLE_ADMIN")
    NotificationResponse addNotification(NotificationRequest notification);
}
