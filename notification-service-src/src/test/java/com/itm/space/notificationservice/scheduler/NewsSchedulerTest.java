package com.itm.space.notificationservice.scheduler;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.itm.space.notificationservice.BaseIntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class NewsSchedulerTest extends BaseIntegrationTest {

    @Autowired
    private NewsScheduler newsScheduler;

    @Test
    @DisplayName("Проверка на изменение статуса, если новости больше 1 дня")
    @DataSet(value = "datasets/config/schedulingConfig/newsOlderOneDay.yml",
            cleanAfter = true, cleanBefore = true)
    @ExpectedDataSet(value = "datasets/config/schedulingConfig/newsOlderOneDayExpected.yml",
            ignoreCols = {"updated"})
    void shouldUpdateWhenNewsCreatedBeforeOneDay() {
        newsScheduler.updateNewsStatus();
    }

    @Test
    @Disabled("Отключен, потому что каждый раз надо менять Created в дадасете")
    @DisplayName("Проверка на изменение статуса, если новости меньше одного дня")
    @DataSet(value = "datasets/config/schedulingConfig/newsYoungerOneDay.yml")
    @ExpectedDataSet(value = "datasets/config/schedulingConfig/newsYoungerOneDayExpected.yml",
            ignoreCols = {"updated"}) //чтобы тест проходил, created должно быть не старше одного дня от текущей даты
    void shouldNotUpdateWhenNewsCreatedAfterOneDay() {
        newsScheduler.updateNewsStatus();
    }
}
