package com.acme.middleware.application.usecase;

import com.acme.middleware.application.dto.TaskDto;

import java.util.UUID;

public interface GetTaskUseCase {
    TaskDto execute(UUID taskId);
}