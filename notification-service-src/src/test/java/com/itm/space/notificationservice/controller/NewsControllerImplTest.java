package com.itm.space.notificationservice.controller;

import com.github.database.rider.core.api.dataset.DataSet;
import com.itm.space.itmplatformcommonmodels.response.HttpErrorResponse;
import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.api.response.NewsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.assertThat;

class NewsControllerImplTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Должен вернуть статус 401, когда пользователь не аутентифицирован")
    void shouldReturnStatus401WhenNotAuthenticatedNews() {
        webTestClient.get()
                .uri("/api/user/news")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(HttpErrorResponse.class)
                .isEqualTo(new HttpErrorResponse(401, "AuthenticationException", "Full authentication is required to access this resource"));
    }

    @Test
    @DataSet(value = "datasets/controller/NewsControllerImpl/news.yml")
    @WithMockUser(value = "b91a0837-8f4f-4644-8946-5c72d845430c", authorities = {"MENTOR"})
    @DisplayName("Должен возвращать список новостей для роли MENTOR и общие")
    void shouldReturnNewsForMentorRole() {
        webTestClient.get()
                .uri("/api/user/news")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].id").isEqualTo("b71a0833-8f4f-4644-8946-5c72d845430c")
                .jsonPath("$[0].imageId").isEqualTo("bea0fa9f-76a5-47d7-8375-e35f68658e72")
                .jsonPath("$[0].targetRole").isEqualTo("ALL")
                .jsonPath("$[0].title").isEqualTo("News 1")
                .jsonPath("$[0].text").isEqualTo("Message 1")
                .jsonPath("$[0].created").isEqualTo("2023-07-18T11:42:25.085893")
                .jsonPath("$[1].id").isEqualTo("b91a0937-8f4f-4644-8946-5c72d845430c")
                .jsonPath("$[0].imageId").isEqualTo("bea0fa9f-76a5-47d7-8375-e35f68658e72")
                .jsonPath("$[1].targetRole").isEqualTo("MENTOR")
                .jsonPath("$[1].title").isEqualTo("News 3")
                .jsonPath("$[1].text").isEqualTo("Message 3")
                .jsonPath("$[1].created").isEqualTo("2023-04-11T11:42:25.085893");
    }

    @Test
    @DataSet(value = "datasets/controller/NewsControllerImpl/news.yml")
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7", authorities = {"STUDENT"})
    @DisplayName("Должен возвращать список новостей для роли STUDENT и общие")
    void shouldReturnNewsForStudentRole() {
        webTestClient.get()
                .uri("/api/user/news")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(3)
                .jsonPath("$[0].id").isEqualTo("b71a0833-8f4f-4644-8946-5c72d845430c")
                .jsonPath("$[0].imageId").isEqualTo("bea0fa9f-76a5-47d7-8375-e35f68658e72")
                .jsonPath("$[0].targetRole").isEqualTo("ALL")
                .jsonPath("$[0].title").isEqualTo("News 1")
                .jsonPath("$[0].text").isEqualTo("Message 1")
                .jsonPath("$[0].created").isEqualTo("2023-07-18T11:42:25.085893")
                .jsonPath("$[1].id").isEqualTo("b71a0837-8f4f-4644-8946-5c72d845430c")
                .jsonPath("$[1].imageId").isEqualTo("b71a0837-8f4f-4644-8946-5c72d845430c")
                .jsonPath("$[2].targetRole").isEqualTo("STUDENT")
                .jsonPath("$[1].title").isEqualTo("News 2")
                .jsonPath("$[1].text").isEqualTo("Message 2")
                .jsonPath("$[1].created").isEqualTo("2023-04-18T11:42:25.085893")
                .jsonPath("$[2].id").isEqualTo("d35d32bf-7a0f-4b29-8abd-195f3b358da7")
                .jsonPath("$[2].imageId").isEqualTo("b81a0837-8f4f-4644-8946-5c72d845430c")
                .jsonPath("$[2].targetRole").isEqualTo("STUDENT")
                .jsonPath("$[2].title").isEqualTo("News 4")
                .jsonPath("$[2].text").isEqualTo("Message 4")
                .jsonPath("$[2].created").isEqualTo("2023-03-11T11:42:25.085893");
    }

    @Test
    @DataSet(value = "datasets/controller/NewsControllerImpl/news.yml")
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7", authorities = {"ROLE_ADMIN"})
    @DisplayName("Должен возвращать пустой список новостей для роли ADMIN")
    void shouldReturnNewsForAdminRole() {
        webTestClient.get()
                .uri("/api/user/news")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(NewsResponse.class)
                .value(newsResponses -> {
                    assertThat(newsResponses).isEmpty();
                });
    }

    @Test
    @DataSet(value = "datasets/controller/NewsControllerImpl/news.yml")
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7", authorities = {"ROLE_MODERATOR"})
    @DisplayName("Должен возвращать пустой список новостей для роли MODERATOR")
    void shouldReturnNewsForModeratorRole() {
        webTestClient.get()
                .uri("/api/user/news")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(NewsResponse.class)
                .value(newsResponses -> {
                    assertThat(newsResponses).isEmpty();
                });
    }
}



