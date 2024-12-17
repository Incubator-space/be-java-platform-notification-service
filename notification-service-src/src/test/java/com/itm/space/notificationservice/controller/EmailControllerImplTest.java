package com.itm.space.notificationservice.controller;

import com.itm.space.notificationservice.BaseIntegrationTest;
import com.itm.space.notificationservice.api.request.EmailRequest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EmailControllerImplTest extends BaseIntegrationTest {

    @Test
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7",authorities = {"ROLE_MODERATOR"})
    void shouldReturnEmailWhenCreated() throws Exception {
        EmailRequest emailRequest = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.CONTROLLER_EMAIL + "EmailRequest.json",
                EmailRequest.class
        );
        String json = jsonParserUtil.getStringFromJson(JsonPathConstans.CONTROLLER_EMAIL + "EmailRequest.json");
        mvc.perform(post("/api/emails")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(emailRequest.getEmail()))
                .andReturn();
    }

    @Test
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7",authorities = {"ROLE_MODERATOR"})
    void shouldReturnBadRequestWhenNullEmail() throws Exception {
        String json = jsonParserUtil.getStringFromJson(JsonPathConstans.CONTROLLER_EMAIL + "EmailRequestNull.json");
        mvc.perform(post("/api/emails")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email should not be blank"))
                .andReturn();
    }

    @Test
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7",authorities = {"ROLE_MODERATOR"})
    void shouldReturnBadRequestWhenEmptyEmail() throws Exception {
        String json = jsonParserUtil.getStringFromJson(JsonPathConstans.CONTROLLER_EMAIL + "EmailRequestEmpty.json");
        MvcResult result = mvc.perform(post("/api/emails")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains("Email should be valid") || responseBody.contains("Email should not be blank"));
    }

    @Test
    @WithMockUser(value = "d55d32bf-7a0f-4b29-8abd-195f3b358da7",authorities = {"ROLE_MODERATOR"})
    void shouldReturnBadRequestWhenGivenInvalidEmailAddress() throws Exception {
        String json = jsonParserUtil.getStringFromJson(JsonPathConstans.CONTROLLER_EMAIL + "EmailRequestNoValid.json");
        mvc.perform(post("/api/emails")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email should be valid"))
                .andReturn();
    }
}