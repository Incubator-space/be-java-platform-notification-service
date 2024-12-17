package com.itm.space.notificationservice.scheduler;

import com.itm.space.notificationservice.domain.enums.NewsStatus;
import com.itm.space.notificationservice.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewsScheduler {

    private final NewsService newsService;

    @Scheduled(cron = "${scheduler.change-wait-news-status}")
    @SchedulerLock(name = "${shedlock.update-news-status.name}",
            lockAtMostFor = "${shedlock.update-news-status.lock-at-most-for}")
    public void updateNewsStatus() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        if(!newsService.isExistByCreatedBeforeAndStatus(oneDayAgo, NewsStatus.NEW)) {
            log.info("There is no news, which status needs to be updated");
        } else {
            newsService.updateStatusForOldNews(NewsStatus.OLD, oneDayAgo, NewsStatus.NEW);
        }
    }
}
