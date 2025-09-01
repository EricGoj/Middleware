package com.acme.middleware.infrastructure.rest.dto;

import com.acme.middleware.domain.model.TaskStatus;

import java.time.Instant;
import java.util.UUID;

public record TaskResponse(
    UUID id,
    String title,
    String description,
    TaskStatus status,
    Instant createdAt,
    Instant updatedAt
) {}
