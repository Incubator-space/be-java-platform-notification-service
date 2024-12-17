package com.itm.space.notificationservice.controller;

import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.api.request.NotificationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificationAdminControllerImplTest extends BaseIntegrationTest {
    @Test
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7", authorities = {"ROLE_ADMIN"})
    @DisplayName("Должен вернуть данные уведомления, когда создается уведомление")
    void shouldReturnNotificationWhenCreated() throws Exception {
        NotificationRequest notificationRequest = jsonParserUtil.getObjectFromJson(
                "json/controller/NotificationControllerImplTest/NotificationRequest.json",
                NotificationRequest.class
        );
        String json = jsonParserUtil.getStringFromJson(
                "json/controller/NotificationControllerImplTest/NotificationRequest.json");
        mvc.perform(post("/api/admin/notifications")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(notificationRequest.getMessage()))
                .andExpect(jsonPath("$.title").value(notificationRequest.getTitle()));
    }

    @Test
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7", authorities = {"ROLE_ADMIN"})
    @DisplayName("Должен вернуть статус 400, когда заголовок null")
    void shouldReturnBadRequestWhenNullTitle() throws Exception {
        String json = jsonParserUtil.getStringFromJson(
                "json/controller/NotificationControllerImplTest/NotificationRequestNullTitle.json");
        mvc.perform(post("/api/admin/notifications")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title should not be blank"));
    }

    @Test
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7", authorities = {"ROLE_ADMIN"})
    @DisplayName("Должен вернуть статус 400, когда сообщение null")
    void shouldReturnBadRequestWhenNullMessage() throws Exception {
        String json = jsonParserUtil.getStringFromJson(
                "json/controller/NotificationControllerImplTest/NotificationRequestNullMessage.json");
        mvc.perform(post("/api/admin/notifications")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Message should not be blank"));
    }

    @Test
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7", authorities = {"ROLE_ADMIN"})
    @DisplayName("Должен вернуть статус 400, когда сообщение или заголовок пустое")
    void shouldReturnBadRequestWhenEmptyTitleOrMessage() throws Exception {
        String json = jsonParserUtil.getStringFromJson(
                "json/controller/NotificationControllerImplTest/NotificationRequestEmpty.json");
        MvcResult result = mvc.perform(post("/api/admin/notifications")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains("Title should not be blank") || responseBody.contains("Message should not be blank"));
    }

    @Test
    @DisplayName("Должен вернуть статус 401, когда пользователь не авторизован")
    void shouldReturnUnauthorizedWhenUserIsUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/admin/notifications")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7", authorities = {"ROLE_STUDENT"})
    @DisplayName("Должен вернуть статус 403, когда у пользователя неверные авторизационные данные")
    void shouldReturnForbiddenWhenUserHasIncorrectAuthorizationData() throws Exception {
        String json = jsonParserUtil.getStringFromJson(
                "json/controller/NotificationControllerImplTest/NotificationRequest.json");
        mvc.perform(MockMvcRequestBuilders.post("/api/admin/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }
}
