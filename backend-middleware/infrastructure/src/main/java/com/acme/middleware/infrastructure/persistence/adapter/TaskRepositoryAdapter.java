package com.acme.middleware.infrastructure.persistence.adapter;

import com.acme.middleware.domain.model.Task;
import com.acme.middleware.domain.port.TaskRepository;
import com.acme.middleware.infrastructure.persistence.entity.TaskEntity;
import com.acme.middleware.infrastructure.persistence.mapper.TaskPersistenceMapper;
import com.acme.middleware.infrastructure.persistence.repository.SpringDataTaskJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TaskRepositoryAdapter implements TaskRepository {

    private final SpringDataTaskJpaRepository jpaRepository;
    private final TaskPersistenceMapper mapper;

    public TaskRepositoryAdapter(SpringDataTaskJpaRepository jpaRepository, TaskPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Task save(Task task) {
        TaskEntity entity = mapper.toEntity(task);
        TaskEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Task> findById(UUID taskId) {
        return jpaRepository.findById(taskId)
                .map(mapper::toDomain);
    }

    @Override
    public List<Task> findAll() {
        List<TaskEntity> entities = jpaRepository.findAll();
        return mapper.toDomain(entities);
    }

    @Override
    public void deleteById(UUID taskId) {
        jpaRepository.deleteById(taskId);
    }

    @Override
    public boolean existsById(UUID taskId) {
        return jpaRepository.existsById(taskId);
    }
}