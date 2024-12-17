package com.itm.space.notificationservice.mapper;

import com.itm.space.notificationservice.api.request.EmailRequest;
import com.itm.space.notificationservice.api.response.EmailResponse;
import com.itm.space.notificationservice.domain.entity.Email;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmailMapper {

    Email toEmail(EmailRequest emailRequest);

    EmailResponse toEmailResponse(Email email);
}
