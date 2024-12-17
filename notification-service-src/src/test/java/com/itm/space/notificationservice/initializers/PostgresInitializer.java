package com.itm.space.notificationservice.initializers;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PostgresInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Container
    public static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15.1")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test")
            .withReuse(false);
    public static final String DATASOURCE_URL = "spring.datasource.url=";
    public static final String DATASOURCE_USERNAME = "spring.datasource.username=";
    public static final String DATASOURCE_PASSWORD = "spring.datasource.password=";

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        container.start();
        TestPropertyValues.of(
                DATASOURCE_URL + container.getJdbcUrl(),
                DATASOURCE_USERNAME + container.getUsername(),
                DATASOURCE_PASSWORD + container.getPassword()
        ).applyTo(configurableApplicationContext.getEnvironment());
    }
}