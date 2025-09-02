package com.acme.middleware.infrastructure.config;

import com.acme.middleware.application.jira.JiraService;
import com.acme.middleware.application.mapper.TaskApplicationMapper;
import com.acme.middleware.application.service.*;
import com.acme.middleware.application.usecase.*;
import com.acme.middleware.domain.port.DomainEventPublisher;
import com.acme.middleware.domain.port.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public TaskApplicationMapper taskApplicationMapper() {
        return new TaskApplicationMapper();
    }

    @Bean
    public CreateTaskUseCase createTaskUseCase(TaskRepository taskRepository, 
                                             DomainEventPublisher eventPublisher,
                                             TaskApplicationMapper mapper,
                                             JiraService jiraService) {
        return new CreateTaskService(taskRepository, eventPublisher, mapper, jiraService);
    }

    @Bean
    public GetTaskUseCase getTaskUseCase(TaskRepository taskRepository, 
                                       TaskApplicationMapper mapper) {
        return new GetTaskService(taskRepository, mapper);
    }

    @Bean
    public ListTasksUseCase listTasksUseCase(TaskRepository taskRepository, 
                                           TaskApplicationMapper mapper,
                                           JiraService jiraService) {
        return new ListTasksService(taskRepository, mapper, jiraService);
    }

    @Bean
    public UpdateTaskUseCase updateTaskUseCase(TaskRepository taskRepository,
                                             DomainEventPublisher eventPublisher,
                                             TaskApplicationMapper mapper) {
        return new UpdateTaskService(taskRepository, eventPublisher, mapper);
    }

    @Bean
    public DeleteTaskUseCase deleteTaskUseCase(TaskRepository taskRepository,
                                             DomainEventPublisher eventPublisher,
                                             JiraService jiraService) {
        return new DeleteTaskService(taskRepository, eventPublisher, jiraService);
    }
}
