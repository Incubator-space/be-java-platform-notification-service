package com.itm.space.notificationservice.api.constants;

import java.util.Set;

public class RoleConstains {
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String MODERATOR = "ROLE_MODERATOR";
    public static final String MENTOR = "MENTOR";

    public static final String STUDENT = "STUDENT";
    public static final Set<String> ADMIN_AND_MODERATOR_ROLES = Set.of(ADMIN, MODERATOR);

}

