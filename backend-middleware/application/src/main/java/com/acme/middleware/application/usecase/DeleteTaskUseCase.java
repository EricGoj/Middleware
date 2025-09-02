package com.acme.middleware.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public interface DeleteTaskUseCase {
    void execute(UUID taskId);
}