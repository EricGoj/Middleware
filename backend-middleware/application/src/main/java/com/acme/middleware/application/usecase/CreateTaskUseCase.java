package com.acme.middleware.application.usecase;

import org.springframework.stereotype.Component;

import com.acme.middleware.application.dto.CreateTaskCommand;
import com.acme.middleware.application.dto.TaskDto;

@Component
public interface CreateTaskUseCase {
    TaskDto execute(CreateTaskCommand command);
}