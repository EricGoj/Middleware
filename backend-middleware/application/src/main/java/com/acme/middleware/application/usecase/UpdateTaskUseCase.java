package com.acme.middleware.application.usecase;

import org.springframework.stereotype.Component;

import com.acme.middleware.application.dto.TaskDto;
import com.acme.middleware.application.dto.UpdateTaskCommand;

import java.util.UUID;

@Component
public interface UpdateTaskUseCase {
    TaskDto execute(UUID taskId, UpdateTaskCommand command);
}