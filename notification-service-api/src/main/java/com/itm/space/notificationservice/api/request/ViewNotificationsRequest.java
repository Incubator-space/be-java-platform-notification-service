package com.itm.space.notificationservice.api.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViewNotificationsRequest {

    @NotEmpty(message = "ids list must not be empty")
    private Set<UUID> ids;
}
