package com.acme.middleware.infrastructure.persistence.mapper;

import com.acme.middleware.domain.model.Task;
import com.acme.middleware.domain.model.TaskId;
import com.acme.middleware.infrastructure.persistence.entity.TaskEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskPersistenceMapper {

    public TaskEntity toEntity(Task task) {
        if (task == null) {
            return null;
        }
        TaskEntity entity = new TaskEntity(
                task.getId().getValue(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getBusinessKey()
        );
        entity.setDueDate(task.getDueDate());
        entity.setPriority(task.getPriority());
        return entity;
    }

    public Task toDomain(TaskEntity entity) {
        if (entity == null) {
            return null;
        }
        return Task.restore(
                TaskId.of(entity.getId()),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDueDate(),
                entity.getPriority(),
                entity.getBusinessKey()
        );
    }

    public List<Task> toDomain(List<TaskEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}