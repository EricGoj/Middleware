package com.acme.middleware.application.dto;

public record CreateTaskCommand(
    String title,
    String description,
    java.time.Instant dueDate,
    String priority
) {}