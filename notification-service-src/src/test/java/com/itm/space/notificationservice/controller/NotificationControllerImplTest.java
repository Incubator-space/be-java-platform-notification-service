package com.itm.space.notificationservice.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.itm.space.itmplatformcommonmodels.response.HttpErrorResponse;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.api.request.ViewNotificationsRequest;
import com.itm.space.notificationservice.api.response.NotificationResponse;
import com.itm.space.notificationservice.exception.ConflictException;
import com.itm.space.notificationservice.exception.InternalServerErrorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.itm.space.notificationservice.controller.ApiConstants.NOTIFICATIONS_API_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificationControllerImplTest extends BaseIntegrationTest {

    @Test
    @DataSet(value = "datasets/controller/NotificationControllerImpl/shouldGetUnreadNotifications/notifications.yml")
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7", authorities = {"ROLE_ADMIN"})
    @DisplayName("Должен возвращать пустой список для администратора")
    void shouldReturnEmptyListForAdmin() {
        webTestClient.get()
                .uri("/api/user/notifications")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(NotificationResponse.class)
                .value(notificationResponses -> {
                    assertThat(notificationResponses).isEmpty();
                });
    }

    @Test
    @DataSet(value = "datasets/controller/NotificationControllerImpl/shouldGetUnreadNotifications/notifications.yml")
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7", authorities = {"ROLE_MODERATOR"})
    @DisplayName("Должен возвращать пустой список для модератора")
    void shouldReturnEmptyListForModerator() {
        webTestClient.get()
                .uri("/api/user/notifications")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(NotificationResponse.class)
                .value(notificationResponses -> {
                    assertThat(notificationResponses).isEmpty();
                });
    }

    @Test
    @DataSet(value = "datasets/controller/NotificationControllerImpl/shouldGetUnreadNotifications/notifications.yml")
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7", authorities = {"ROLE_USER"})
    @DisplayName("Должен возвращать список для пользователя")
    void shouldReturnListForUser() {
        webTestClient.get()
                .uri("/api/user/notifications")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(10)
                .jsonPath("$[0].id").isEqualTo("b51a0837-8f4f-4644-8946-5c72d845430c")
                .jsonPath("$[1].title").isEqualTo("Notification 2")
                .jsonPath("$[2].imageId").isEqualTo("9f05f320-e08d-4182-bb99-aecbd9b9a954")
                .jsonPath("$[2].text").isEqualTo("Message 3")
                .jsonPath("$[0].created").isEqualTo("2023-07-11T11:42:25.085893");
    }

    @Test
    @WithAnonymousUser
    @DisplayName("Должен возвращать статус 401 при отсутствии аутентификации")
    void shouldReturnStatus401WhenNotAuthenticated() {
        webTestClient
                .get()
                .uri("/api/user/notifications")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(HttpErrorResponse.class)
                .isEqualTo(new HttpErrorResponse(401, "AuthenticationException", "Full authentication is required to access this resource"));
    }

    @Test
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da")
    @DisplayName("Должен возвращать статус 404, когда user не найден")
    void shouldReturnStatus404WhenUserNotFound() {
        webTestClient
                .get()
                .uri("/api/user/notifications")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(HttpErrorResponse.class)
                .isEqualTo(new HttpErrorResponse(404, "Not Found", "User с UUID d55d32bf-7a0f-4b29-8abd-0195f3b358da не найден"));
    }

    @Test
    @DataSet(value = "datasets/controller/NotificationControllerImpl/shouldGetUnreadNotifications/notifications.yml")
    @WithMockUser("d55d32bf-7a0f-4b29-8abd-195f3b358da7")
    void shouldGetReadNotifications() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/user/notifications/history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("4c584e32-40d7-470c-87e5-5f45fe9c6124"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Notification 8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].imageId").value("4f7c2f2d-1727-4de4-8953-021fb1f11544"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].text").value("Message 10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].created").value("2023-07-11T11:42:30.085893"));
    }

    @Test
    void shouldReturnStatus401WhenNotAuthenticatedHistoryNotifications() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/user/notifications/history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7")
    void shouldReturnStatus500HistoryNotifications() throws Exception {
        when(notificationService.getViewedNotifications(Mockito.any())).thenThrow(new InternalServerErrorException("Test exception"));
        mvc.perform(get("/api/user/notifications/history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DataSet(value = "datasets/controller/NotificationControllerImpl/shouldGetUnreadNotifications/notificationsVIEWD.yml",
            cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(value = "datasets/controller/NotificationControllerImpl/shouldGetUnreadNotifications/expectedNotificationsVIEWD.yml")
    @DisplayName("Успешный ответ, установлен нотификации статус VIEWED.")
    void shouldPatchNotification() {
        webTestClient.patch()
                .uri(NOTIFICATIONS_API_PATH + "/413beec7-17aa-45d3-8a40-e747a98d0777")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", authUtil.getAuthorization("student@student.ru"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @WithAnonymousUser
    @DisplayName("Должен вернуть 401, если пользователь не аутентифицирован")
    void shouldReturn401WhenNotAuthorized() {
        webTestClient.patch()
                .uri(NOTIFICATIONS_API_PATH + "/b51a0837-8f4f-4644-8946-5c72d845430c")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.UNAUTHORIZED.value())
                .jsonPath("$.type").isEqualTo("AuthenticationException")
                .jsonPath("$.message").isEqualTo("Full authentication is required to access this resource");
    }
    @Test
    @DataSet(value = "datasets/controller/NotificationControllerImpl/shouldGetUnreadNotifications/notifications409.yml")
    @DisplayName("Должен вернуть 409, Просмотреть можно только свою нотификацию")
    void shouldReturn409WhenConflict() {
        webTestClient.patch()
                .uri(NOTIFICATIONS_API_PATH + "/b51a0837-8f4f-4644-8946-5c72d845430c")
                .header("Authorization", authUtil.getAuthorization("student@student.ru"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.CONFLICT.value())
                .jsonPath("$.type").isEqualTo(HttpStatus.CONFLICT.getReasonPhrase())
                .jsonPath("$.message").isEqualTo("Просмотреть можно только свою нотификацию");
    }
    @Test
    @DisplayName("Должен вернуть 404, Просмотреть можно только свою нотификацию")
    void shouldReturn404WhenNotEnoughRights() {
        webTestClient.patch()
                .uri(NOTIFICATIONS_API_PATH + "/99147f75-f80b-4b52-aee6-0520c7698880")
                .header("Authorization", authUtil.getAuthorization("student@student.ru"))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.code").isEqualTo(HttpStatus.NOT_FOUND.value())
                .jsonPath("$.type").isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase())
                .jsonPath("$.message").isEqualTo("Нотификация не найдена");
    }

    @Test
    @DataSet(value = "datasets/controller/NotificationControllerImpl/shouldGetUnreadNotifications/notificationsVIEWD.yml")
    @ExpectedDataSet(value = "datasets/controller/NotificationControllerImpl/shouldGetUnreadNotifications/expectedNotificationsVIEWD.yml")
    @DisplayName("Проверка на изменение статуса у новости на VIEWED")
    void GetAllNotifications() {
        ViewNotificationsRequest json = jsonParserUtil.getObjectFromJson("json/controller/NotificationControllerImplTest/notificationsIds.json", ViewNotificationsRequest.class);
        webTestClient.patch()
                .uri("/api/user/notifications")
                .header("Authorization", authUtil.getAuthorization("student@student.ru"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("Должен вернуть статус 401 если не аутентифицирован")
    void shouldReturnStatus401WhenNotAuthenticatedNotifications() throws Exception {
        webTestClient.patch()
                .uri("/api/user/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(HttpErrorResponse.class)
                .isEqualTo(new HttpErrorResponse(401, "AuthenticationException", "Full authentication is required to access this resource"));
    }

    @Test
    @DisplayName("Должен вернуть статус 404 если не найдено уведомлений для пользователя")
    void shouldReturnStatus404WhenNoNotificationsExistForUser() {
        ViewNotificationsRequest json = jsonParserUtil.getObjectFromJson("json/controller/NotificationControllerImplTest/notificationsIds404.json", ViewNotificationsRequest.class);
        webTestClient.patch()
                .uri("/api/user/notifications")
                .header("Authorization", authUtil.getAuthorization("student@student.ru"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(HttpErrorResponse.class)
                .isEqualTo(new HttpErrorResponse(404, "Not Found", "Notification user targets for checks were not found here"));
    }

    @Test
    @DataSet(value = "datasets/controller/NotificationControllerImpl/shouldGetUnreadNotifications/notifications409.yml")
    @DisplayName("Должен вернуть статус 409 при попытке посмотореть чужую нотификацию/нотификации")
    void shouldReturnStatus409WhenEnabledToViewSomeOneElsesNotification() throws ConflictException {
        ViewNotificationsRequest json = jsonParserUtil.getObjectFromJson("json/controller/NotificationControllerImplTest/notificationsIds409.json", ViewNotificationsRequest.class);
        webTestClient.patch()
                .uri("/api/user/notifications")
                .header("Authorization", authUtil.getAuthorization("student@student.ru"))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(json)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectBody(HttpErrorResponse.class)
                .isEqualTo(new HttpErrorResponse(409, "Conflict", "You can't view notifications of other users"));
    }

    @Test
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da")
    @DisplayName("Должен возвращать статус 404, когда пользователь не найден")
    void shouldReturnStatus404WhenUserNotFoundForCounterUri() {
        webTestClient
                .get()
                .uri("/api/user/notifications/counter")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(HttpErrorResponse.class)
                .isEqualTo(new HttpErrorResponse(404, "Not Found", "User с UUID d55d32bf-7a0f-4b29-8abd-0195f3b358da не найден"));
    }

    @Test
    @DataSet(value = "datasets/controller/NotificationControllerImpl/shouldGetUnreadNotifications/news.yml", cleanAfter = true, cleanBefore = true)
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7", authorities = {"ROLE_ADMIN"})
    @DisplayName("Должен возвращать количество непрочитанных новостей для администратора")
    void shouldReturnCountOfNewsForAdmin() {
        webTestClient.get()
                .uri("/api/user/notifications/counter")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .value(count -> {
                    assertThat(count).isEqualTo(3L);
                });
    }

    @Test
    @DataSet(value = "datasets/controller/NotificationControllerImpl/shouldGetUnreadNotifications/news.yml", cleanAfter = true, cleanBefore = true)
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7", authorities = {"STUDENT"})
    @DisplayName("Должен возвращать количество непрочитанных новостей для пользователя")
    void shouldReturnCountOfNewsForStudent() {
        webTestClient.get()
                .uri("/api/user/notifications/counter")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .value(count -> {
                    assertThat(count).isEqualTo(2L);
                });
    }

    @Test
    @DataSet(value = "datasets/controller/NotificationControllerImpl/shouldGetUnreadNotifications/notifications.yml")
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7", authorities = {"STUDENT"})
    @DisplayName("Должен возвращать количество уведомлений для пользователя")
    void shouldReturnCountOfNotificationsForStudent() {
        webTestClient.get()
                .uri("/api/user/notifications/counter")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Long.class)
                .value(count -> {
                    assertThat(count).isEqualTo(7L);
                });
    }
}
