package com.acme.middleware.infrastructure.sync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acme.middleware.infrastructure.sync.entity.EventEntity;

@Repository
public interface SpringDataEventJpaRepository extends JpaRepository<EventEntity, String> {
        
}
