package com.itm.space.notificationservice.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationMessage {
    REVIEW_CREATE_NOTIFICATION("Запись на ревью",
            "Студент записался на ревью. Время проведения: %s - %s"),
    REVIEW_DELETE_NOTIFICATION("Отмена ревью",
            "Отменено ревью, запланированное на время: %s - %s"),
    REVIEW_REMIND_NOTIFICATION("Напоминание о ревью",
            "Запланировано ревью, время проведения: %s - %s"),
    DIRECTION_UPDATE_NOTIFICATION("Обновление направления",
            "В направлении %s появились новые темы для изучения"),
    ACHIEVEMENT_RECEIVED_NOTIFICATION("Получено новое достижение",
            "Получено достижение %s. Так держать, продолжайте в том же духе!"),
    DIRECTION_NEW_NOTIFICATION("Доступно новое направление",
            "Доступно новое направление для изучения"),
    NEW_RESPONSE_COMMENT_NOTIFICATION("Новый ответ на комментарий",
            "На ваш комментарий оставили новый ответ"),
    COMMENT_DELETE_NOTIFICATION("Ваш комментарий удален",
            "Ваш комментарий к задаче удален модератором."),
    BANK_ADD_CARD("Привязана банковская карта",
            "%s %s, к вашему аккаунту успешно привязана банковская карта %s"),
    BANK_REMOVE_CARD("Банковская карта удалена",
            "%s %s, из вашего аккаунта успешно удалена банковская карта %s"),
    PAYMENT_PROCESSING_NOTIFICATION("Платеж в обработке",
            "%s %s, платеж на сумму %.2f р обрабатывается платежным шлюзом."),
    TRANSACTION_EVENT_NOTIFICATION_MESSAGE("Платеж в обработке",
            "%s %s, платеж на сумму %.2f р ожидает вашего подтверждения."),
    TRANSACTION_OUTCOME_COMPLETE_NOTIFICATION_MESSAGE("Выплата проведена",
            "%s %s, выплата проведена, баланс вашего кошелька снижен на %.2f р."),
    PAYMENT_FAILED_NOTIFICATION("Платеж не прошел",
            "%s %s, платеж на сумму %.2f р не проведен. Причина - %s. Вы можете провести платеж еще раз -" +
            "в случае повторного возникновения ошибки обратитесь в поддержку."),
    TRANSACTION_INCOME_COMPLETE_NOTIFICATION_MESSAGE("Платеж завершен",
            "%s %s,баланс вашего кошелька пополнен на %.2f р"),
    NOTICE_OF_PAYMENT_PROCESSING("Выплата в обработке",
            "%s %s, выплата на сумму %,.2f р обрабатывается платежным шлюзом."),
    TRANSACTION_OUTCOME_CANCELLED_NOTIFICATION_MESSAGE("Выплата не прошла",
            "%s %s, выплата на сумму %.2f р не проведена. Причина -" +
            "%s. Вы можете провести выплату еще раз - в случае повторного возникновения ошибки обратитесь в поддержку.");

    private final String title;
    private final String message;
}

