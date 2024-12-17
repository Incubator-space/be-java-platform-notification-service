package com.itm.space.notificationservice.repository;

import com.itm.space.notificationservice.domain.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmailRepository extends JpaRepository<Email, UUID> {
}
