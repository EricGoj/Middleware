package com.acme.middleware.application.dto;

import com.acme.middleware.domain.model.IssueStatus;

public record UpdateIssueCommand(
    String title,
    String description,
    IssueStatus status,
    java.time.Instant dueDate,
    String priority
) {}