package com.acme.middleware.application.service;

import com.acme.middleware.application.usecase.DeleteTaskUseCase;
import com.acme.middleware.domain.event.TaskDeleted;
import com.acme.middleware.domain.model.Task;
import com.acme.middleware.domain.port.DomainEventPublisher;
import com.acme.middleware.domain.port.TaskRepository;
import com.acme.middleware.application.exceptions.TaskNotFoundException;
import com.acme.middleware.application.jira.JiraService;
import java.util.logging.Logger;
import java.time.Instant;
import java.util.UUID;

public class DeleteTaskService implements DeleteTaskUseCase {

    private final TaskRepository taskRepository;
    private final DomainEventPublisher eventPublisher;
    private final JiraService jiraService;
    private final Logger logger = Logger.getLogger(CreateTaskService.class.getName());

    public DeleteTaskService(TaskRepository taskRepository, DomainEventPublisher eventPublisher, JiraService jiraService) {
        this.taskRepository = taskRepository;
        this.eventPublisher = eventPublisher;
        this.jiraService = jiraService;
    }   

    @Override
    public void execute(UUID task) {

        Task taskDel = taskRepository.findById(task).get();
        
        if (taskDel == null) {
            throw new TaskNotFoundException("Task not found with id: " + taskDel.getId());
        }
        
        taskRepository.deleteById(taskDel.getId().getValue());

        try {
            jiraService.deleteIssue(taskDel.getBusinessKey());
        } catch (Exception e) {
            logger.warning("Failed to delete business key for task " + taskDel.getBusinessKey() + ": " + e.getMessage());
        }

        TaskDeleted event = new TaskDeleted(taskDel.getId(), Instant.now());
        eventPublisher.publish(event);
    }
}