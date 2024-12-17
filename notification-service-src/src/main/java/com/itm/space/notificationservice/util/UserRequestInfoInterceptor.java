package com.itm.space.notificationservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itm.space.itmplatformcommonmodels.response.HttpErrorResponse;
import com.itm.space.notificationservice.api.request.UserRequestInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserRequestInfoInterceptor implements HandlerInterceptor {

    private final UserRequestInfo userRequestInfo;

    private final SecurityUtil securityUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (securityUtil.isAuthenticated()) {
            userRequestInfo.setUserId(securityUtil.getCurrentUserId());
            userRequestInfo.setUserRole(securityUtil.getCurrentUserRolesAsString());

            log.info("Пользователь успешно аутентифицирован. UserId: {}", securityUtil.getCurrentUserId());
            return true;
        }

        log.error("Несанкционированный доступ. Пользователь не аутентифицирован.");

        // Формируем и отправляем JSON-ответ с кодом 401 и информацией об ошибке
        HttpErrorResponse errorResponse = new HttpErrorResponse(
                401,
                "Unauthorized",
                "Unauthorized access");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));

        return false;
    }
}

