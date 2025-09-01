package com.acme.middleware.application.mapper;

import com.acme.middleware.application.dto.TaskDto;
import com.acme.middleware.domain.model.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskApplicationMapper {

    public TaskDto toDto(Task task) {
        if (task == null) {
            return null;
        }
        return new TaskDto(
                task.getId().getValue(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    public List<TaskDto> toDto(List<Task> tasks) {
        if (tasks == null) {
            return null;
        }
        return tasks.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
