package com.itm.space.notificationservice.kafka.consumer;

import com.itm.space.itmplatformcommonmodels.kafka.AchievementEvent;
import com.itm.space.notificationservice.kafka.handler.AchievementEventHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Log4j2
@Getter
@RequiredArgsConstructor
public class AchievementEventConsumer implements EventConsumer<AchievementEvent> {

    private AchievementEvent event;
    private final Set<AchievementEventHandler<AchievementEvent>> eventHandlers;

    @Override
    @KafkaListener(topics = "#{'${spring.kafka.topic.achievement-events}'}")
    public void handle(AchievementEvent achievementEvent) {
        this.event = achievementEvent;
        eventHandlers.stream()
                .filter(handler -> handler.isHandle(achievementEvent))
                .forEach(handler -> {
                    handler.handle(achievementEvent);
                    log.info("Handled event: {}", achievementEvent);
                });
    }
}