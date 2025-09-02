package com.acme.middleware.domain.port;

import com.acme.middleware.domain.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {
    Task save(Task task);
    Optional<Task> findById(UUID taskId);
    List<Task> findAll();
    void deleteById(UUID taskId);
    boolean existsById(UUID taskId);
}