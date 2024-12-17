package com.itm.space.notificationservice.controller.admin;

import com.itm.space.itmplatformcommonmodels.response.HttpErrorResponse;
import com.itm.space.notificationservice.api.request.NewsRequest;
import com.itm.space.notificationservice.api.response.NewsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "News Admin Controller", description = "CRUD операции с новостями с использованием роли ADMIN")
public interface NewsAdminController {

    @Operation(summary = "createNews", description = "Создание новости платформы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ с DTO новости"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorResponse.class)))
    })
    NewsResponse createNews(@Parameter(description = "DTO запроса на создание новости") NewsRequest newsRequest);
}
