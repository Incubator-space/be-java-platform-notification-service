package com.itm.space.notificationservice.controller;

import com.itm.space.notificationservice.api.response.NewsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;


import java.util.List;


@Tag(name = "News Controller", description = "CRUD операции с новостями")
public interface NewsController {

    @Operation(summary = "Get news", description = "Получение новостей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение новостей"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")

    })
    List<NewsResponse> getNews(Authentication authentication);
}
