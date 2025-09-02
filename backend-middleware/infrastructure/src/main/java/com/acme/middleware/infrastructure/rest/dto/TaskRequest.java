package com.acme.middleware.infrastructure.rest.dto;

import com.acme.middleware.domain.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequest(
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    String title,
    
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    String description,
    
    TaskStatus status,
    
    java.time.Instant dueDate,
    
    @Size(max = 20, message = "Priority must not exceed 20 characters")
    String priority
) {}