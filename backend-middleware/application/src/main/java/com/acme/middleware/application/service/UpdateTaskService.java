package com.acme.middleware.application.service;

import com.acme.middleware.application.dto.TaskDto;
import com.acme.middleware.application.dto.UpdateTaskCommand;
import com.acme.middleware.application.exceptions.TaskNotFoundException;
import com.acme.middleware.application.mapper.TaskApplicationMapper;
import com.acme.middleware.application.usecase.UpdateTaskUseCase;
import com.acme.middleware.domain.event.TaskUpdated;
import com.acme.middleware.domain.model.Task;
import com.acme.middleware.domain.port.DomainEventPublisher;
import com.acme.middleware.domain.port.TaskRepository;

import java.time.Instant;
import java.util.UUID;

public class UpdateTaskService implements UpdateTaskUseCase {

    private final TaskRepository taskRepository;
    private final DomainEventPublisher eventPublisher;
    private final TaskApplicationMapper mapper;

    public UpdateTaskService(TaskRepository taskRepository,
                           DomainEventPublisher eventPublisher,
                           TaskApplicationMapper mapper) {
        this.taskRepository = taskRepository;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
    }

    @Override
    public TaskDto execute(UUID taskId, UpdateTaskCommand command) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        if (command.title() != null) {
            task.updateTitle(command.title());
        }
        if (command.description() != null) {
            task.updateDescription(command.description());
        }
        if (command.status() != null) {
            task.updateStatus(command.status());
        }
        if (command.dueDate() != null) {
            task.updateDueDate(command.dueDate());
        }
        if (command.priority() != null) {
            task.updatePriority(command.priority());
        }

        Task updatedTask = taskRepository.save(task);

        TaskUpdated event = new TaskUpdated(
            updatedTask.getId(),
            updatedTask.getTitle(),
            updatedTask.getDescription(),
            updatedTask.getStatus(),
            Instant.now()
        );
        eventPublisher.publish(event);

        return mapper.toDto(updatedTask);
    }
}