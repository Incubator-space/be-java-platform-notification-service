package com.itm.space.notificationservice.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.Objects;

@Table(name = "users")
@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User extends AuditingBaseEntity {
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "is_archived", nullable = false)
    private boolean isArchived;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User that = (User) o;
        return id != null && Objects.equals(id, that.id);
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + "" + ")" + "(" +
                "email = " + email + ")" + "(" +
                "first_name = " + firstName + ")" + "(" +
                "last_name = " + lastName + ")" + "(" +
                "is_archived = " + isArchived +
                "created = " + created + ", " +
                "updated = " + updated + ")";
    }
}
