package com.acme.middleware.infrastructure.rest.dto;

import java.time.Instant;

public record ErrorResponse(
    String message,
    String error,
    int status,
    Instant timestamp,
    String path
) {}