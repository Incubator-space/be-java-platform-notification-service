package com.itm.space.notificationservice.handler;

import com.itm.space.itmplatformcommonmodels.kafka.DirectionEvent;
import com.itm.space.notificationservice.BaseUnitTest;
import com.itm.space.notificationservice.api.enums.NewsTargetRole;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.domain.entity.News;
import com.itm.space.notificationservice.kafka.handler.impl.DirectionUpdateEventHandler;
import com.itm.space.notificationservice.mapper.NewsMapper;
import com.itm.space.notificationservice.service.impl.NewsServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

class DirectionUpdateEventHandlerTest extends BaseUnitTest {
    @InjectMocks
    private DirectionUpdateEventHandler notificationHandler;
    @Mock
    private NewsMapper mapper;
    @Mock
    private NewsServiceImpl newsService;

    @Test
    @SneakyThrows
    void shouldHandleWhenStatusIsUpdate() {
        DirectionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_DIRECTION_UPDATE + "DirectionEventUpdate.json",
                DirectionEvent.class
        );
        boolean result = notificationHandler.isHandle(event);
        Assertions.assertTrue(result);
    }

    @Test
    @SneakyThrows
    void shouldHandleWhenStatusIsNotUpdate() {
        DirectionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_DIRECTION_UPDATE + "DirectionEventNotUpdate.json",
                DirectionEvent.class
        );
        boolean result = notificationHandler.isHandle(event);
        Assertions.assertFalse(result);
    }

    @Test
    @SneakyThrows
    void shouldSaveNotificationWhenHandlingEvent() {
        DirectionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_DIRECTION_UPDATE + "DirectionEventNotUpdate.json",
                DirectionEvent.class
        );
        News news= new News();
        when(mapper.newDirectionEventToNews(event)).thenReturn(news);
        notificationHandler.handle(event);
        verify(newsService, times(1)).saveNews(news);
    }
}