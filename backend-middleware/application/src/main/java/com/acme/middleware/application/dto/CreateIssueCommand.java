package com.acme.middleware.application.dto;

public record CreateIssueCommand(
    String title,
    String description,
    java.time.Instant dueDate,
    String priority
) {}