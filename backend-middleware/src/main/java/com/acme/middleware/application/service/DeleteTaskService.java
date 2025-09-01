package com.acme.middleware.application.service;

import com.acme.middleware.application.usecase.DeleteTaskUseCase;
import com.acme.middleware.domain.event.TaskDeleted;
import com.acme.middleware.domain.model.TaskId;
import com.acme.middleware.domain.port.DomainEventPublisher;
import com.acme.middleware.domain.port.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class DeleteTaskService implements DeleteTaskUseCase {

    private final TaskRepository taskRepository;
    private final DomainEventPublisher eventPublisher;

    public DeleteTaskService(TaskRepository taskRepository, DomainEventPublisher eventPublisher) {
        this.taskRepository = taskRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(UUID taskId) {
        TaskId id = TaskId.of(taskId);
        
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id: " + taskId);
        }

        taskRepository.deleteById(id);

        TaskDeleted event = new TaskDeleted(id, Instant.now());
        eventPublisher.publish(event);
    }
}
