package com.itm.space.notificationservice.mapper;

import com.itm.space.itmplatformcommonmodels.kafka.DirectionEvent;
import com.itm.space.notificationservice.api.enums.NewsTargetRole;
import com.itm.space.notificationservice.api.request.NewsRequest;
import com.itm.space.notificationservice.api.response.NewsResponse;
import com.itm.space.notificationservice.domain.entity.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static com.itm.space.notificationservice.api.enums.NotificationMessage.DIRECTION_NEW_NOTIFICATION;

@Mapper
public interface NewsMapper {

    @Mapping(target = "status", constant = "NEW")
    @Mapping(target = "targetRole", constant = "ALL")
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage.DIRECTION_NEW_NOTIFICATION.getTitle())")
    @Mapping(target = "message", source = "directionEvent", qualifiedByName = "newNotificationMessage")
    News newDirectionEventToNews(DirectionEvent directionEvent);

    @Named("newNotificationMessage")
    default String newNotificationMessage(DirectionEvent directionEvent) {
        return String.format(
                DIRECTION_NEW_NOTIFICATION.getMessage(),
                directionEvent.directionName()
        );
    }

    @Mapping(target = "image_id", constant = "NULL", ignore = true)
    @Mapping(target = "message", source = "text")
    @Mapping(target = "status", constant = "NEW")
    @Mapping(source = "targetRole", target = "targetRole", qualifiedByName = "stringToNewsTargetRole")
    News toNews(NewsRequest newsRequest);


    @Mapping(target = "imageId", source = "image_id")
    @Mapping(target = "text", source = "message")
    NewsResponse toNewsResponse(News news);

    @Named("stringToNewsTargetRole")
    default NewsTargetRole stringToNewsTargetRole(String role) {
        try {
            return NewsTargetRole.valueOf(role);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid target role: " + role);
        }
    }
}
