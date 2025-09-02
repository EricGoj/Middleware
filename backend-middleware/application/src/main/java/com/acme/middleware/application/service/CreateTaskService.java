package com.acme.middleware.application.service;

import com.acme.middleware.application.dto.CreateTaskCommand;
import com.acme.middleware.application.dto.TaskDto;
import com.acme.middleware.application.jira.JiraService;
import com.acme.middleware.application.mapper.TaskApplicationMapper;
import com.acme.middleware.application.usecase.CreateTaskUseCase;
import com.acme.middleware.domain.event.TaskCreated;
import com.acme.middleware.domain.model.Task;
import com.acme.middleware.domain.model.TaskId;
import com.acme.middleware.domain.port.DomainEventPublisher;
import com.acme.middleware.domain.port.TaskRepository;
import java.util.logging.Logger;
import java.time.Instant;
import org.springframework.transaction.annotation.Transactional;

public class CreateTaskService implements CreateTaskUseCase {

    private final TaskRepository taskRepository;
    private final DomainEventPublisher eventPublisher;
    private final TaskApplicationMapper mapper;
    private final JiraService jiraService;

    private final Logger logger = Logger.getLogger(CreateTaskService.class.getName());

    public CreateTaskService(TaskRepository taskRepository, 
                           DomainEventPublisher eventPublisher,
                           TaskApplicationMapper mapper,
                           JiraService jiraService) {
        this.taskRepository = taskRepository;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
        this.jiraService = jiraService;
    }

    @Override
    @Transactional
    public TaskDto execute(CreateTaskCommand command) {
        TaskId taskId = TaskId.generate();
        Task task = Task.create(taskId, command.title(), command.description(), command.dueDate(), command.priority());
        
        Task savedTask = taskRepository.save(task);
        
        TaskCreated event = new TaskCreated(
            savedTask.getId(),
            savedTask.getTitle(),
            savedTask.getDescription(),
            Instant.now()
        );

        try {
            String jiraKey = jiraService.createIssue(savedTask.getTitle(), savedTask.getDescription(), "Task", savedTask.getDueDate());
            savedTask.linkToBusinessKey(jiraKey);
            savedTask = taskRepository.save(savedTask);
        } catch (Exception e) {
            logger.warning("Failed to create business key for task " + savedTask.getBusinessKey() + ": " + e.getMessage());
        }
        eventPublisher.publish(event);
        
        return mapper.toDto(savedTask);
    }


}