package com.itm.space.notificationservice.repository;

import com.itm.space.notificationservice.domain.entity.Notification;
import com.itm.space.notificationservice.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    @Query("SELECT n " +
           "FROM Notification n " +
           "WHERE n.target = :target " +
           "ORDER BY n.status, n.created")
    List<Notification> getAllNotifications(UUID target);

    @Query("SELECT n " +
           "FROM Notification n " +
           "WHERE n.target = :userId AND n.status = 'VIEWED'")
    List<Notification> getViewedNotifications(@Param("userId") UUID userId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Notification n SET n.status = :status WHERE n.id = :id")
    void updateNotificationStatus(@Param("id") UUID id, @Param("status") NotificationStatus status);

    @Query("SELECT n FROM Notification n WHERE n.status = 'WAIT'")
    List<Notification> findWaitingNotifications();

    @Query("SELECT n.target FROM Notification n WHERE n.id in (:ids) group by n.target")
    List<UUID> findUserIdsByNotificationTargetIds(@Param("ids") Set<UUID> ids);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Notification n SET n.status = :status WHERE n.id IN (:ids)")
    int updateNotificationsStatus(@Param("ids") Set<UUID> ids, @Param("status") NotificationStatus status);

    @Query("SELECT count (n) " +
           "FROM Notification n " +
           "WHERE n.status != 'VIEWED' AND n.target = :userId")
    Long getCountOfUnviewedNotifications(@Param("userId") UUID userId);

}

