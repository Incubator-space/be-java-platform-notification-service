package com.itm.space.notificationservice.repository;

import com.itm.space.notificationservice.api.enums.NewsTargetRole;
import com.itm.space.notificationservice.domain.entity.News;
import com.itm.space.notificationservice.domain.enums.NewsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface NewsRepository extends JpaRepository<News, UUID> {

    boolean existsByCreatedBeforeAndStatus(LocalDateTime created, NewsStatus status);

    @Modifying
    @Query("UPDATE News n SET n.status = :newStatus WHERE n.created < :oneDayAgo AND n.status = :currentStatus")
    void updateStatusForOldNews(@Param("newStatus") NewsStatus newStatus,
                                @Param("oneDayAgo") LocalDateTime oneDayAgo,
                                @Param("currentStatus") NewsStatus currentStatus);

    @Query("SELECT n " +
           "FROM News n " +
           "WHERE n.targetRole IN (:targets)" +
           "ORDER BY n.created DESC, n.targetRole")
    List<News> getNewsByTargetRoles(List<NewsTargetRole> targets);

    @Query("SELECT count (n) " +
           "FROM News n " +
           "WHERE n.status = 'NEW'")
    Long getUnreadNewsCount();

    @Query("SELECT count (n) " +
           "FROM News n " +
           "WHERE n.status = 'NEW' " +
           "AND n.targetRole IN (:targets)")
    Long getUnreadNewsCountByTargetRole(@Param("targets") Set<NewsTargetRole> targets);
}
