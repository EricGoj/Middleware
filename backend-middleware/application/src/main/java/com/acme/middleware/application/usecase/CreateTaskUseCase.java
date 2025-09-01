package com.acme.middleware.application.usecase;

import com.acme.middleware.application.dto.CreateTaskCommand;
import com.acme.middleware.application.dto.TaskDto;

public interface CreateTaskUseCase {
    TaskDto execute(CreateTaskCommand command);
}