package com.acme.middleware.application.service;

import com.acme.middleware.application.dto.TaskDto;
import com.acme.middleware.application.mapper.TaskApplicationMapper;
import com.acme.middleware.application.usecase.ListTasksUseCase;
import com.acme.middleware.domain.model.Task;
import com.acme.middleware.domain.port.TaskRepository;
import com.acme.middleware.application.jira.JiraService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListTasksService implements ListTasksUseCase {

    private final TaskRepository taskRepository;
    private final TaskApplicationMapper mapper;
    private final JiraService jiraService;
    private static final Logger log = LoggerFactory.getLogger(ListTasksService.class);

    public ListTasksService(TaskRepository taskRepository, TaskApplicationMapper mapper, JiraService jiraService) {
        this.taskRepository = taskRepository;
        this.mapper = mapper;
        this.jiraService = jiraService;
    }

    @Override
    public List<TaskDto> execute() {
        List<Task> tasks = taskRepository.findAll();

        try {
            jiraService.syncTasks(tasks);
        } catch (Exception e) {
            log.error("Failed to sync tasks: {}", e.getMessage(), e);
        }
        return mapper.toDto(tasks);
    }
}