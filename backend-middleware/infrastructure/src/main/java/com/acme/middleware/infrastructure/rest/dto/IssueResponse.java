package com.acme.middleware.infrastructure.rest.dto;

import com.acme.middleware.domain.model.IssueStatus;

import java.time.Instant;
import java.util.UUID;

public record IssueResponse(
    UUID id,
    String title,
    String description,
    IssueStatus status,
    Instant createdAt,
    Instant updatedAt,
    Instant dueDate,
    String priority
) {}