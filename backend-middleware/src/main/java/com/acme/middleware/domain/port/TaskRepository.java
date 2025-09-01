package com.acme.middleware.domain.port;

import com.acme.middleware.domain.model.Task;
import com.acme.middleware.domain.model.TaskId;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(TaskId taskId);
    List<Task> findAll();
    void deleteById(TaskId taskId);
    boolean existsById(TaskId taskId);
}
