package com.acme.middleware.infrastructure.persistence.repository;

import com.acme.middleware.infrastructure.persistence.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataTaskJpaRepository extends JpaRepository<TaskEntity, UUID> {
}