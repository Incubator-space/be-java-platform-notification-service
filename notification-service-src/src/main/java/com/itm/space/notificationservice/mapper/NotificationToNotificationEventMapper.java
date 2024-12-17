package com.itm.space.notificationservice.mapper;

import com.itm.space.itmplatformcommonmodels.kafka.NotificationEvent;
import com.itm.space.itmplatformcommonmodels.kafka.enums.NotificationEventStatus;
import com.itm.space.notificationservice.domain.entity.Notification;

import com.itm.space.notificationservice.enums.NotificationStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface NotificationToNotificationEventMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "status", source = "status", qualifiedByName = "mapStatus")
    @Mapping(target = "target", source = "target")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "message", source = "message")
    NotificationEvent mapToEvent(Notification notification);

    @Named("mapStatus")
    default NotificationEventStatus mapStatus(NotificationStatus status) {
        return switch (status) {
            case WAIT -> NotificationEventStatus.WAIT;
            case SENT -> NotificationEventStatus.SENT;
            case ERROR -> NotificationEventStatus.ERROR;
            default -> throw new IllegalArgumentException("Unsupported status: " + status);
        };
    }
}
