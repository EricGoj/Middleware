package com.acme.middleware.application.usecase;

import com.acme.middleware.application.dto.TaskDto;
import com.acme.middleware.application.dto.UpdateTaskCommand;

import java.util.UUID;

public interface UpdateTaskUseCase {
    TaskDto execute(UUID taskId, UpdateTaskCommand command);
}
