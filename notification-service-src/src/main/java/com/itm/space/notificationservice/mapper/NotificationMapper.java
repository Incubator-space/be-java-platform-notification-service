package com.itm.space.notificationservice.mapper;

import com.itm.space.itmplatformcommonmodels.kafka.AchievementEvent;
import com.itm.space.itmplatformcommonmodels.kafka.BankCardEvent;
import com.itm.space.itmplatformcommonmodels.kafka.DirectionEvent;
import com.itm.space.itmplatformcommonmodels.kafka.ReviewEvent;
import com.itm.space.itmplatformcommonmodels.kafka.TaskCommentEvent;
import com.itm.space.itmplatformcommonmodels.kafka.TransactionEvent;
import com.itm.space.notificationservice.api.request.NotificationRequest;
import com.itm.space.notificationservice.api.response.NotificationResponse;
import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.repository.UserProjection;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

import static com.itm.space.notificationservice.api.enums.NotificationMessage.*;
import static com.itm.space.notificationservice.api.enums.NotificationMessage.ACHIEVEMENT_RECEIVED_NOTIFICATION;
import static com.itm.space.notificationservice.api.enums.NotificationMessage.BANK_ADD_CARD;
import static com.itm.space.notificationservice.api.enums.NotificationMessage.BANK_REMOVE_CARD;
import static com.itm.space.notificationservice.api.enums.NotificationMessage.DIRECTION_NEW_NOTIFICATION;
import static com.itm.space.notificationservice.api.enums.NotificationMessage.DIRECTION_UPDATE_NOTIFICATION;
import static com.itm.space.notificationservice.api.enums.NotificationMessage.NEW_RESPONSE_COMMENT_NOTIFICATION;
import static com.itm.space.notificationservice.api.enums.NotificationMessage.PAYMENT_FAILED_NOTIFICATION;
import static com.itm.space.notificationservice.api.enums.NotificationMessage.PAYMENT_PROCESSING_NOTIFICATION;
import static com.itm.space.notificationservice.api.enums.NotificationMessage.REVIEW_CREATE_NOTIFICATION;
import static com.itm.space.notificationservice.api.enums.NotificationMessage.REVIEW_DELETE_NOTIFICATION;
import static com.itm.space.notificationservice.api.enums.NotificationMessage.REVIEW_REMIND_NOTIFICATION;
import static com.itm.space.notificationservice.api.enums.NotificationMessage.TRANSACTION_EVENT_NOTIFICATION_MESSAGE;
import static com.itm.space.notificationservice.api.enums.NotificationMessage.TRANSACTION_INCOME_COMPLETE_NOTIFICATION_MESSAGE;
import static com.itm.space.notificationservice.api.enums.NotificationMessage.TRANSACTION_OUTCOME_COMPLETE_NOTIFICATION_MESSAGE;

@Mapper
public interface NotificationMapper {
    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", source = "achievementEvent.userId")
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage.ACHIEVEMENT_RECEIVED_NOTIFICATION.getTitle())")
    @Mapping(target = "message", source = "achievementEvent", qualifiedByName = "receivedNotificationMessage")
    Notification achievementEventToNotification(AchievementEvent achievementEvent);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", constant = "NULL", ignore = true)
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage.DIRECTION_NEW_NOTIFICATION.getTitle())")
    @Mapping(target = "message", source = "directionEvent", qualifiedByName = "newNotificationMessage")
    Notification newDirectionEventToNotification(DirectionEvent directionEvent);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", constant = "NULL", ignore = true)
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage" +
            ".DIRECTION_UPDATE_NOTIFICATION.getTitle())")
    @Mapping(target = "message", source = "directionEvent", qualifiedByName = "updateNotificationMessage")
    Notification updateDirectionEventToNotification(DirectionEvent directionEvent);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", source = "reviewEvent.mentorId")
    @Mapping(target = "created", source = "reviewEvent.startDate")
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage.REVIEW_CREATE_NOTIFICATION.getTitle())")
    @Mapping(target = "message", source = "reviewEvent", qualifiedByName = "createNotificationMessage")
    Notification reviewEventToNotificationWithStatusStartWaiting(ReviewEvent reviewEvent);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", source = "reviewEvent.studentId")
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage.REVIEW_DELETE_NOTIFICATION.getTitle())")
    @Mapping(target = "message", source = "reviewEvent", qualifiedByName = "deleteNotificationMessage")
    Notification reviewEventToNotificationWithStatusCanceledStudent(ReviewEvent reviewEvent);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", source = "reviewEvent.mentorId")
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage.REVIEW_DELETE_NOTIFICATION.getTitle())")
    @Mapping(target = "message", source = "reviewEvent", qualifiedByName = "deleteNotificationMessage")
    Notification reviewEventToNotificationWithStatusCanceledMentor(ReviewEvent reviewEvent);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", source = "reviewEvent.studentId")
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage" +
            ".REVIEW_REMIND_NOTIFICATION.getTitle())")
    @Mapping(target = "message", source = "reviewEvent", qualifiedByName = "reminderNotificationMessage")
    Notification reviewEventReminderToNotificationForStudent(ReviewEvent reviewEvent);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", source = "reviewEvent.mentorId")
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage" +
            ".REVIEW_REMIND_NOTIFICATION.getTitle())")
    @Mapping(target = "message", source = "reviewEvent", qualifiedByName = "reminderNotificationMessage")
    Notification reviewEventReminderToNotificationForMentor(ReviewEvent reviewEvent);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", source = "event.parentAuthorId")
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage.NEW_RESPONSE_COMMENT_NOTIFICATION.getTitle())")
    @Mapping(target = "message", source = "event", qualifiedByName = "newResponseToCommentMessage")
    Notification taskCommentEventToNotification(TaskCommentEvent event);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", source = "taskCommentEvent.authorId")
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage.COMMENT_DELETE_NOTIFICATION.getTitle())")
    @Mapping(target = "message", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage.COMMENT_DELETE_NOTIFICATION.getMessage())")
    Notification taskCommentDeleteEventToNotification(TaskCommentEvent taskCommentEvent);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", constant = "NULL", ignore = true)
    @Mapping(target = "imageId", constant = "NULL", ignore = true)
    Notification toNotification(NotificationRequest notificationRequest);

    @Mapping(target = "text", source = "message")
    NotificationResponse toNotificationResponse(Notification notification);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", source = "userId")
    @Mapping(target = "imageId", constant = "NULL", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage.BANK_ADD_CARD.getTitle())")
    @Mapping(target = "message", source = "event", qualifiedByName = "mapUserToString")
    Notification newMessageBankCardWhereAdd(BankCardEvent event, @Context UserProjection userProjection);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", source = "userId")
    @Mapping(target = "imageId", constant = "NULL", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage.BANK_REMOVE_CARD.getTitle())")
    @Mapping(target = "message", source = "event", qualifiedByName = "messageWhereRemoveBankCard")
    Notification newMessageBankCardWhereRemove(BankCardEvent event, @Context UserProjection userProjection);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", source = "userId")
    @Mapping(target = "imageId", constant = "NULL", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage.PAYMENT_PROCESSING_NOTIFICATION.getTitle())")
    @Mapping(target = "message", source = "event", qualifiedByName = "transactionIncomePendingMessage")
    Notification transactionIncomePendingToNotification(TransactionEvent event, @Context UserProjection userProjection);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", source = "userId")
    @Mapping(target = "imageId", constant = "NULL", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage.TRANSACTION_EVENT_NOTIFICATION_MESSAGE.getTitle())")
    @Mapping(target = "message", source = "event", qualifiedByName = "transactionIncomeWaitingMassage")
    Notification toEntity(TransactionEvent event, @Context UserProjection userProjection);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "CANCELLED")
    @Mapping(target = "target", source = "userId")
    @Mapping(target = "imageId", constant = "NULL", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage.PAYMENT_FAILED_NOTIFICATION.getTitle())")
    @Mapping(target = "message", source = "event", qualifiedByName = "transactionIncomeCancelledMessage")
    Notification transactionIncomeCancelledToNotification(TransactionEvent event, @Context UserProjection userProjection);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", source = "userId")
    @Mapping(target = "imageId", constant = "NULL", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage.TRANSACTION_OUTCOME_COMPLETE_NOTIFICATION_MESSAGE.getTitle())")
    @Mapping(target = "message", source = "event", qualifiedByName = "transactionOutcomeCompletedMessage")
    Notification transactionOutcomeCompletedToNotification(TransactionEvent event, @Context UserProjection userProjection);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", source = "userId")
    @Mapping(target = "imageId", constant = "NULL", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage.TRANSACTION_INCOME_COMPLETE_NOTIFICATION_MESSAGE.getTitle())")
    @Mapping(target = "message", source = "event", qualifiedByName = "transactionIncomeCompletedMessage")
    Notification transactionIncomeCompletedToNotification(TransactionEvent event, @Context UserProjection userProjection);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "WAIT")
    @Mapping(target = "target", source = "userId")
    @Mapping(target = "imageId", constant = "NULL", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage. NOTICE_OF_PAYMENT_PROCESSING.getTitle())")
    @Mapping(target = "message", source = "event", qualifiedByName = "transactionOutcomePendingMessage")
    Notification transactionOutcomePendingToNotification(TransactionEvent event, @Context UserProjection userProjection);

    @Mapping(target = "type", constant = "PUSH")
    @Mapping(target = "action", constant = "placeholder_action")
    @Mapping(target = "status", constant = "CANCELLED")
    @Mapping(target = "target", source = "userId")
    @Mapping(target = "imageId", constant = "NULL", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "title", expression = "java(com.itm.space.notificationservice.api.enums.NotificationMessage.TRANSACTION_OUTCOME_CANCELLED_NOTIFICATION_MESSAGE.getTitle())")
    @Mapping(target = "message", source = "event", qualifiedByName = "transactionOutcomeCancelledMessage")
    Notification transactionOutcomeCancelledToNotification(TransactionEvent event, @Context UserProjection userProjection);


    @Named("receivedNotificationMessage")
    default String receivedNotificationMessage(AchievementEvent achievementEvent) {
        return String.format(
                ACHIEVEMENT_RECEIVED_NOTIFICATION.getMessage(),
                achievementEvent.title());
    }

    @Named("newNotificationMessage")
    default String newNotificationMessage(DirectionEvent directionEvent) {
        return String.format(
                DIRECTION_NEW_NOTIFICATION.getMessage(),
                directionEvent.directionName()
        );
    }

    @Named("updateNotificationMessage")
    default String updateNotificationMessage(DirectionEvent directionEvent) {
        return String.format(
                DIRECTION_UPDATE_NOTIFICATION.getMessage(),
                directionEvent.directionName());
    }

    @Named("createNotificationMessage")
    default String createNotificationMessage(ReviewEvent reviewEvent) {
        return String.format(
                REVIEW_CREATE_NOTIFICATION.getMessage(),
                reviewEvent.startDate(),
                reviewEvent.endDate());
    }

    @Named("deleteNotificationMessage")
    default String deleteNotificationMessage(ReviewEvent reviewEvent) {
        return String.format(
                REVIEW_DELETE_NOTIFICATION.getMessage(),
                reviewEvent.startDate(),
                reviewEvent.endDate());
    }

    @Named("reminderNotificationMessage")
    default String reminderNotificationMessage(ReviewEvent reviewEvent) {
        return String.format(
                REVIEW_REMIND_NOTIFICATION.getMessage(),
                reviewEvent.startDate(),
                reviewEvent.endDate()
        );
    }

    @Named("newResponseToCommentMessage")
    default String newResponseToCommentMessage(TaskCommentEvent event) {
        return String.format(
                NEW_RESPONSE_COMMENT_NOTIFICATION.getMessage(),
                event.authorId()
        );
    }

    @Named("mapUserToString")
    default String mapUserToString(BankCardEvent event, @Context UserProjection userProjection) {
        return String.format(
                BANK_ADD_CARD.getMessage(),
                userProjection.getFirstName(),
                userProjection.getLastName(),
                event.name()
        );
    }

    @Named("messageWhereRemoveBankCard")
    default String messageWhereRemoveBankCard(BankCardEvent event, @Context UserProjection userProjection) {
        return String.format(
                BANK_REMOVE_CARD.getMessage(),
                userProjection.getFirstName(),
                userProjection.getLastName(),
                event.name()
        );
    }

    @Named("transactionIncomePendingMessage")
    default String transactionIncomePendingMessage(TransactionEvent event, @Context UserProjection userProjection) {
        BigDecimal amountInRubles = BigDecimal.valueOf(event.amount()).divide(BigDecimal.valueOf(100));

        return String.format(
                PAYMENT_PROCESSING_NOTIFICATION.getMessage(),
                userProjection.getFirstName(),
                userProjection.getLastName(),
                amountInRubles
        );
    }

    @Named("transactionIncomeWaitingMassage")
    default String mapUserToString(TransactionEvent event, @Context UserProjection userProjection) {
        BigDecimal amountInRubles = BigDecimal.valueOf(event.amount()).divide(BigDecimal.valueOf(100));

        return String.format(
                TRANSACTION_EVENT_NOTIFICATION_MESSAGE.getMessage(),
                userProjection.getFirstName(),
                userProjection.getLastName(),
                amountInRubles
        );
    }

    @Named("transactionIncomeCancelledMessage")
    default String transactionIncomeCancelledMessage(TransactionEvent event, @Context UserProjection userProjection) {
        BigDecimal amountInRubles = BigDecimal.valueOf(event.amount()).divide(BigDecimal.valueOf(100));

        return String.format(
                PAYMENT_FAILED_NOTIFICATION.getMessage(),
                userProjection.getFirstName(),
                userProjection.getLastName(),
                amountInRubles,
                event.cancelReason()
        );
    }

    @Named("transactionOutcomeCompletedMessage")
    default String transactionOutcomeCompletedMessage(TransactionEvent event, @Context UserProjection userProjection) {
        BigDecimal amountInRubles = BigDecimal.valueOf(event.amount()).divide(BigDecimal.valueOf(100));

        return String.format(
                TRANSACTION_OUTCOME_COMPLETE_NOTIFICATION_MESSAGE.getMessage(),
                userProjection.getFirstName(),
                userProjection.getLastName(),
                amountInRubles
        );
    }

       @Named("transactionIncomeCompletedMessage")
    default String transactionIncomeCompletedMessage(TransactionEvent event, @Context UserProjection userProjection) {
        BigDecimal amountInRubles = BigDecimal.valueOf(event.amount()).divide(BigDecimal.valueOf(100));

        return String.format(
                TRANSACTION_INCOME_COMPLETE_NOTIFICATION_MESSAGE.getMessage(),
                userProjection.getFirstName(),
                userProjection.getLastName(),
                amountInRubles
        );
    }

    @Named("transactionOutcomePendingMessage")
    default String transactionOutcomePendingMessage(TransactionEvent event, @Context UserProjection userProjection) {
        BigDecimal amountInRubles = BigDecimal.valueOf(event.amount()).divide(BigDecimal.valueOf(100));

        return String.format(
                NOTICE_OF_PAYMENT_PROCESSING.getMessage(),
                userProjection.getFirstName(),
                userProjection.getLastName(),
                amountInRubles
        );
    }

    @Named("transactionOutcomeCancelledMessage")
    default String transactionOutcomeCancelledMessage(TransactionEvent event, @Context UserProjection userProjection) {
        BigDecimal amountInRubles = BigDecimal.valueOf(event.amount()).divide(BigDecimal.valueOf(100));

        return String.format(
                TRANSACTION_OUTCOME_CANCELLED_NOTIFICATION_MESSAGE.getMessage(),
                userProjection.getFirstName(),
                userProjection.getLastName(),
                amountInRubles,
                event.cancelReason()
        );
    }
}



