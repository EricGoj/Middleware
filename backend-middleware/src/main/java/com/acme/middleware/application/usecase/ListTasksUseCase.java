package com.acme.middleware.application.usecase;

import com.acme.middleware.application.dto.TaskDto;

import java.util.List;

public interface ListTasksUseCase {
    List<TaskDto> execute();
}
