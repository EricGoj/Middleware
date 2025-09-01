package com.acme.middleware.application.service;

import com.acme.middleware.application.dto.CreateTaskCommand;
import com.acme.middleware.application.dto.TaskDto;
import com.acme.middleware.application.mapper.TaskApplicationMapper;
import com.acme.middleware.application.usecase.CreateTaskUseCase;
import com.acme.middleware.domain.event.TaskCreated;
import com.acme.middleware.domain.model.Task;
import com.acme.middleware.domain.model.TaskId;
import com.acme.middleware.domain.port.DomainEventPublisher;
import com.acme.middleware.domain.port.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CreateTaskService implements CreateTaskUseCase {

    private final TaskRepository taskRepository;
    private final DomainEventPublisher eventPublisher;
    private final TaskApplicationMapper mapper;

    public CreateTaskService(TaskRepository taskRepository, 
                           DomainEventPublisher eventPublisher,
                           TaskApplicationMapper mapper) {
        this.taskRepository = taskRepository;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
    }

    @Override
    public TaskDto execute(CreateTaskCommand command) {
        TaskId taskId = TaskId.generate();
        Task task = Task.create(taskId, command.title(), command.description());
        
        Task savedTask = taskRepository.save(task);
        
        TaskCreated event = new TaskCreated(
            savedTask.getId(),
            savedTask.getTitle(),
            savedTask.getDescription(),
            Instant.now()
        );
        eventPublisher.publish(event);
        
        return mapper.toDto(savedTask);
    }
}
