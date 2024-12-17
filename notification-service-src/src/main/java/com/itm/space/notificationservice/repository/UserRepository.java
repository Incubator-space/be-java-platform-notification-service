package com.itm.space.notificationservice.repository;

import com.itm.space.notificationservice.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<UserProjection> findProjectionById(UUID id);
}
