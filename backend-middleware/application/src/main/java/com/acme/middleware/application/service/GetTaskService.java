package com.acme.middleware.application.service;

import com.acme.middleware.application.dto.TaskDto;
import com.acme.middleware.application.exceptions.TaskNotFoundException;
import com.acme.middleware.application.mapper.TaskApplicationMapper;
import com.acme.middleware.application.usecase.GetTaskUseCase;
import com.acme.middleware.domain.model.Task;
import com.acme.middleware.domain.port.TaskRepository;

import java.util.UUID;

public class GetTaskService implements GetTaskUseCase {

    private final TaskRepository taskRepository;
    private final TaskApplicationMapper mapper;

    public GetTaskService(TaskRepository taskRepository, TaskApplicationMapper mapper) {
        this.taskRepository = taskRepository;
        this.mapper = mapper;
    }

    @Override
    public TaskDto execute(UUID taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
        
        return mapper.toDto(task);
    }
}