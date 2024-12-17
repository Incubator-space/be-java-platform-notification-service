package com.itm.space.notificationservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "mail")
@Getter
@Setter
public class EmailProperties {

    private String host;

    private int port;

    private String username;

    private String password;

    private String auth;

    private String starttls;

    private Properties properties;

    public Properties getJavaMailProperties() {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", properties.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", properties.getProperty("mail.smtp.starttls.enable"));
        props.put("mail.debug", properties.getProperty("mail.debug"));
        return props;
    }
}
