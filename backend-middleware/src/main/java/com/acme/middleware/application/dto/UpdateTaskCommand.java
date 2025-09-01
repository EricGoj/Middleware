package com.acme.middleware.application.dto;

import com.acme.middleware.domain.model.TaskStatus;

public record UpdateTaskCommand(
    String title,
    String description,
    TaskStatus status
) {}
