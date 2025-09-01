package com.acme.middleware.application.dto;

public record CreateTaskCommand(
    String title,
    String description
) {}
