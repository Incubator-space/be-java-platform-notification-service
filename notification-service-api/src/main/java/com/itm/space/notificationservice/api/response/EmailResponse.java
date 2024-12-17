package com.itm.space.notificationservice.api.response;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class EmailResponse {

    private UUID id;

    private String email;
}
