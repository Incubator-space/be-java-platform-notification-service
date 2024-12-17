package com.itm.space.notificationservice.controller;

import com.itm.space.notificationservice.api.response.NewsResponse;
import com.itm.space.notificationservice.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


import static com.itm.space.notificationservice.controller.ApiConstants.NEWS_API_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(NEWS_API_PATH)
public class NewsControllerImpl implements NewsController {

    private final NewsService newsService;

    @Override
    @GetMapping()
    public List<NewsResponse> getNews(Authentication authentication) {
        return newsService.getNews(UUID.fromString(authentication.getName()));
    }
}