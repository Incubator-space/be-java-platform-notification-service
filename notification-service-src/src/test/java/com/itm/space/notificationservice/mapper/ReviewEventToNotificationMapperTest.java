package com.itm.space.notificationservice.mapper;

import com.itm.space.itmplatformcommonmodels.kafka.ReviewEvent;
import com.itm.space.notificationservice.BaseUnitTest;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.domain.entity.Notification;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class ReviewEventToNotificationMapperTest extends BaseUnitTest {

    @Test
    @SneakyThrows
    void mapToNotificationWithStatusStartWaiting() {
        ReviewEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.MAPPER_REVIEW + "ReviewEvent.json",
                ReviewEvent.class
        );
        NotificationMapper mapper = Mappers.getMapper(NotificationMapper.class);
        Notification notification = mapper.reviewEventToNotificationWithStatusStartWaiting(event);

        Assertions.assertEquals("PUSH", notification.getType().toString());
        Assertions.assertEquals("placeholder_action", notification.getAction());
        Assertions.assertEquals("WAIT", notification.getStatus().toString());
        Assertions.assertEquals(event.mentorId(), notification.getTarget());
        Assertions.assertEquals("Запись на ревью", notification.getTitle());
        Assertions.assertEquals("Студент записался на ревью. Время проведения: " +
                event.startDate() + " - " + event.endDate(), notification.getMessage());
    }
}
