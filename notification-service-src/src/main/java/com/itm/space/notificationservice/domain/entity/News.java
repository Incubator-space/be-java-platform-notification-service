package com.itm.space.notificationservice.domain.entity;

import com.itm.space.notificationservice.domain.enums.NewsStatus;
import com.itm.space.notificationservice.api.enums.NewsTargetRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "news")
public class News extends AuditingBaseEntity {

    @Column(name = "image_id")
    private UUID image_id;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private NewsStatus status;

    @Column(name = "target_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private NewsTargetRole targetRole;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false)
    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        News news = (News) o;
        return id != null && Objects.equals(id, news.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "image_id=" + image_id +
                ", status=" + status +
                ", targetRole=" + targetRole +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", id=" + id +
                '}';
    }
}
