package com.itm.space.notificationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.spring.api.DBRider;
import com.itm.space.itmplatformcommonmodels.util.AuthUtil;
import com.itm.space.itmplatformcommonmodels.util.JsonParserUtil;
import com.itm.space.notificationservice.config.UtilConfig;
import com.itm.space.notificationservice.initializers.KafkaInitializer;
import com.itm.space.notificationservice.initializers.KeycloakInitializer;
import com.itm.space.notificationservice.initializers.PostgresInitializer;
import com.itm.space.notificationservice.mapper.NotificationMapper;
import com.itm.space.notificationservice.service.NewsService;
import com.itm.space.notificationservice.service.NotificationService;
import com.itm.space.notificationservice.service.UserService;
import com.itm.space.notificationservice.service.impl.TestConsumerService;
import com.itm.space.notificationservice.service.impl.TestProducerService;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.NOT_SUPPORTED)
@ContextConfiguration(initializers = {PostgresInitializer.class, KafkaInitializer.class, KeycloakInitializer.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(UtilConfig.class)
@RunWith(SpringRunner.class)
@DBRider
@DBUnit(caseSensitiveTableNames = true, schema = "public")
@AutoConfigureWebTestClient(timeout = "50000")
public abstract class BaseIntegrationTest {
    @Autowired
    protected MockMvc mvc;
    @SpyBean
    protected NotificationService notificationService;
    @SpyBean
    protected UserService userService;
    @SpyBean
    protected NotificationMapper notificationMapper;
    @Autowired
    protected ObjectMapper objectMapper;
    @SpyBean
    protected NewsService newsService;
    @Spy
    protected JsonParserUtil jsonParserUtil;
    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    protected AuthUtil authUtil;
    @Autowired
    protected TestProducerService testProducerService;

    @Autowired
    protected TestConsumerService testConsumerService;
}
