package com.acme.middleware.application.dto;

import com.acme.middleware.domain.model.TaskStatus;

import java.time.Instant;
import java.util.UUID;

public record TaskDto(
    UUID id,
    String title,
    String description,
    TaskStatus status,
    Instant createdAt,
    Instant updatedAt
) {}