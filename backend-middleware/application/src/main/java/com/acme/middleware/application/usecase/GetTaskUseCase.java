package com.acme.middleware.application.usecase;

import org.springframework.stereotype.Component;

import com.acme.middleware.application.dto.TaskDto;

import java.util.UUID;

@Component
public interface GetTaskUseCase {
    TaskDto execute(UUID taskId);
}