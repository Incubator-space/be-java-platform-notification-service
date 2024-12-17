package com.itm.space.notificationservice.controller;
import com.itm.space.notificationservice.api.response.EmailResponse;
import com.itm.space.notificationservice.api.request.EmailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Email Controller", description = "CRUD операции с email-адресами")
public interface EmailController {

    @Operation(summary = "Create email", description = "Создание нового email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Email создан"),
            @ApiResponse(responseCode = "400", description = "Недопустимый запрос"),
            @ApiResponse(responseCode = "403", description = "В доступе отказано"),
            @ApiResponse(responseCode = "500", description = "Сервер не может создать email")
    })
    @PostMapping
    @Secured("ROLE_MODERATOR")
    EmailResponse create(EmailRequest emailAddress);
}
