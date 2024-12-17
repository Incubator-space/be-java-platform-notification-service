package com.itm.space.notificationservice.api.request;

import com.itm.space.notificationservice.api.enums.NewsTargetRole;
import com.itm.space.notificationservice.api.validation.ValueOfEnum;
import com.itm.space.notificationservice.api.validation.groups.BlankChecks;
import com.itm.space.notificationservice.api.validation.groups.EnumChecks;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@GroupSequence({NewsRequest.class, BlankChecks.class, EnumChecks.class})
public class NewsRequest {

    @NotBlank(message = "Title should not be blank", groups = BlankChecks.class)
    private String title;

    @NotBlank(message = "Text should not be blank", groups = BlankChecks.class)
    private String text;

    @NotBlank(message = "TargetRole should not be blank", groups = BlankChecks.class)
    @ValueOfEnum(enumClass = NewsTargetRole.class, message = "TargetRole must be ALL | STUDENT | MENTOR", groups = EnumChecks.class)
    private String targetRole;
}
