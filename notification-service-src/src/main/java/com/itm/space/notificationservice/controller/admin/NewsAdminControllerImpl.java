package com.itm.space.notificationservice.controller.admin;

import com.itm.space.notificationservice.api.request.NewsRequest;
import com.itm.space.notificationservice.api.response.NewsResponse;
import com.itm.space.notificationservice.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.itm.space.notificationservice.controller.ApiConstants.NEWS_ADMIN_API_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(NEWS_ADMIN_API_PATH)
public class NewsAdminControllerImpl implements NewsAdminController {

    private final NewsService newsService;

    @Override
    @PostMapping
    @Secured("ROLE_ADMIN")
    public NewsResponse createNews(@RequestBody @Valid NewsRequest newsRequest) {
        return newsService.saveNewsAndReturnResponse(newsRequest);
    }
}
