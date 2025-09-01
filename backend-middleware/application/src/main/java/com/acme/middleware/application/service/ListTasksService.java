package com.acme.middleware.application.service;

import com.acme.middleware.application.dto.TaskDto;
import com.acme.middleware.application.mapper.TaskApplicationMapper;
import com.acme.middleware.application.usecase.ListTasksUseCase;
import com.acme.middleware.domain.model.Task;
import com.acme.middleware.domain.port.TaskRepository;

import java.util.List;

public class ListTasksService implements ListTasksUseCase {

    private final TaskRepository taskRepository;
    private final TaskApplicationMapper mapper;

    public ListTasksService(TaskRepository taskRepository, TaskApplicationMapper mapper) {
        this.taskRepository = taskRepository;
        this.mapper = mapper;
    }

    @Override
    public List<TaskDto> execute() {
        List<Task> tasks = taskRepository.findAll();
        return mapper.toDto(tasks);
    }
}