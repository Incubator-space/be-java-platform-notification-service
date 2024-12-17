package com.itm.space.notificationservice.api.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

@Getter
@Setter
@Component
@RequestScope
public class UserRequestInfo {

    private String userRole;

    private UUID userId;
}