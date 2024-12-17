package com.itm.space.notificationservice.controller;

import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.api.request.NewsRequest;
import com.itm.space.notificationservice.exception.InternalServerErrorException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
class NewsAdminControllerImplTest extends BaseIntegrationTest {
    NewsRequest mockNewsRequest = NewsRequest.builder()
            .text("Java Message")
            .title("Java Title")
            .targetRole("ALL")
            .build();

    @Test
    @DisplayName("Должен успешно вернуть данные новости, когда она создается")
    void shouldReturnNewsWhenCreatedV2(){
        webTestClient.post()
                .uri("/api/admin/news")
                .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("admin@admin.ru"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(mockNewsRequest), NewsRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.text").isEqualTo(mockNewsRequest.getText())
                .jsonPath("$.title").isEqualTo(mockNewsRequest.getTitle())
                .jsonPath("$.targetRole").isEqualTo(mockNewsRequest.getTargetRole());
    }

    @Test
    @DisplayName("Должен вернуть статус 401, когда пользователь не авторизован")
    void shouldReturnUnauthorizedWhenUserIsNotAuthenticatedV2() {

        webTestClient.post()
                .uri("/api/admin/news")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(mockNewsRequest), NewsRequest.class)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.code").isEqualTo(401)
                .jsonPath("$.type").isEqualTo("AuthenticationException")
                .jsonPath("$.message").isEqualTo("Full authentication is required to access this resource");
    }

    @Test
    @DisplayName("Должен вернуть статус 403, когда у пользователя неверные авторизационные данные")
    void shouldReturnForbiddenWhenUserHasIncorrectAuthorizationDataV2() {
        webTestClient.post()
                .uri("/api/admin/news")
                .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("student@student.ru"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(mockNewsRequest), NewsRequest.class)
                .exchange()
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.code").isEqualTo(403)
                .jsonPath("$.type").isEqualTo("AccessDeniedException")
                .jsonPath("$.message").isEqualTo("Access Denied");
    }

    @Test
    @DisplayName("Должен вернуть статус 400, когда запрос передан  с недействительным значением targetRole")
    void shouldThrowValidationExceptionWhenTargetRoleIsInvalidV2() {
        NewsRequest mockNewsRequest = NewsRequest.builder()
                .text("Java Message")
                .title("Java Title")
                .targetRole("INVALID_ROLE")
                .build();

        webTestClient.post()
                .uri("/api/admin/news")
                .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("admin@admin.ru"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(mockNewsRequest), NewsRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.targetRole").isEqualTo("TargetRole must be ALL | STUDENT | MENTOR");
    }

    @Test
    @DisplayName("Должен вернуть статус 400, когда все поля в запросе пустые")
    void shouldThrowValidationExceptionWhenEmptyTextOrTitleOrMessageV2() throws Exception {
        NewsRequest mockNewsRequest = NewsRequest.builder()
                .text("")
                .title("")
                .targetRole("")
                .build();

        webTestClient.post()
                .uri("/api/admin/news")
                .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("admin@admin.ru"))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(mockNewsRequest), NewsRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.targetRole").isEqualTo("TargetRole should not be blank")
                .jsonPath("$.title").isEqualTo("Title should not be blank")
                .jsonPath("$.text").isEqualTo("Text should not be blank");
    }

    @Test
    @DisplayName("Должен вернуть статус 500, когда в сервисе произошла ошибка сервера")
    void shouldReturnInternalServerErrorWhenExceptionErrorV2(){
        Mockito.doThrow(new InternalServerErrorException("Simulating a server error"))
                .when(newsService)
                .saveNewsAndReturnResponse(any());


        webTestClient.post()
                .uri("/api/admin/news")
                .header(HttpHeaders.AUTHORIZATION, authUtil.getAuthorization("admin@admin.ru"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(mockNewsRequest), NewsRequest.class)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.code").isEqualTo(500)
                .jsonPath("$.type").isEqualTo("Internal Server Error")
                .jsonPath("$.message").isEqualTo("Simulating a server error");
    }
}
