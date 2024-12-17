package com.itm.space.notificationservice.mapper;

import com.itm.space.itmplatformcommonmodels.kafka.AchievementEvent;
import com.itm.space.notificationservice.BaseUnitTest;
import com.itm.space.notificationservice.api.enums.NotificationMessage;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.domain.entity.Notification;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class AchievementEventToNotificationMapperTest extends BaseUnitTest {
    @Test
    @SneakyThrows
    void mapAchievementEventToNotification() {
        AchievementEvent achievementEvent = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.MAPPER_ACHIEVEMENT + "AchievementEvent.json",
                AchievementEvent.class
        );

        NotificationMapper mapper = Mappers.getMapper(NotificationMapper.class);
        Notification notification = mapper.achievementEventToNotification(achievementEvent);

        Assertions.assertEquals("PUSH", notification.getType().toString());
        Assertions.assertEquals("placeholder_action", notification.getAction());
        Assertions.assertEquals("WAIT", notification.getStatus().toString());
        Assertions.assertEquals(achievementEvent.userId(), notification.getTarget());
        Assertions.assertEquals(NotificationMessage.ACHIEVEMENT_RECEIVED_NOTIFICATION.getTitle(), notification.getTitle());
        Assertions.assertEquals("Получено достижение Some Achievement Title. Так держать, продолжайте в том же духе!", notification.getMessage());
    }
}
