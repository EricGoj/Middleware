package com.acme.middleware.infrastructure.persistence.repository;

import com.acme.middleware.infrastructure.persistence.entity.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataIssueJpaRepository extends JpaRepository<IssueEntity, UUID> {
}