package com.itm.space.notificationservice.handler;

import com.itm.space.itmplatformcommonmodels.kafka.DirectionEvent;
import com.itm.space.notificationservice.BaseUnitTest;
import com.itm.space.notificationservice.api.enums.NewsTargetRole;
import com.itm.space.notificationservice.constant.JsonPathConstans;
import com.itm.space.notificationservice.domain.entity.News;
import com.itm.space.notificationservice.kafka.handler.impl.DirectionNewEventHandler;
import com.itm.space.notificationservice.mapper.NewsMapper;
import com.itm.space.notificationservice.service.impl.NewsServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class DirectionNewEventHandlerTest extends BaseUnitTest {

    @InjectMocks
    private DirectionNewEventHandler newsHandler;
    @Mock
    private NewsServiceImpl newsService;
    @Mock
    private NewsMapper mapper;

    @Test
    @SneakyThrows
    void testIsHandleWhenStatusIsNew() {
        DirectionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_DIRECTION_NEW + "DirectionEventNew.json",
                DirectionEvent.class
        );
        boolean result = newsHandler.isHandle(event);
        Assertions.assertTrue(result);
    }

    @Test
    @SneakyThrows
    void testIsHandleWhenStatusIsNotNew() {
        DirectionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_DIRECTION_NEW + "DirectionEventNotNew.json",
                DirectionEvent.class
        );
        boolean result = newsHandler.isHandle(event);
        Assertions.assertFalse(result);
    }

    @Test
    @SneakyThrows
    void testHandle() {
        DirectionEvent event = jsonParserUtil.getObjectFromJson(
                JsonPathConstans.HANDLER_DIRECTION_NEW + "DirectionEventNotNew.json",
                DirectionEvent.class
        );
        News news = new News();
        when(mapper.newDirectionEventToNews(event)).thenReturn(news);
        newsHandler.handle(event);
        verify(newsService, times(1)).saveNews(news);
    }
}
