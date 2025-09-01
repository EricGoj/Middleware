package com.acme.middleware.infrastructure.persistence.adapter;

import com.acme.middleware.domain.model.Task;
import com.acme.middleware.domain.model.TaskId;
import com.acme.middleware.domain.port.TaskRepository;
import com.acme.middleware.infrastructure.persistence.entity.TaskEntity;
import com.acme.middleware.infrastructure.persistence.mapper.TaskPersistenceMapper;
import com.acme.middleware.infrastructure.persistence.repository.SpringDataTaskJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
    public Optional<Task> findById(TaskId taskId) {
        return jpaRepository.findById(taskId.getValue())
                .map(mapper::toDomain);
    }

    @Override
    public List<Task> findAll() {
        List<TaskEntity> entities = jpaRepository.findAll();
        return mapper.toDomain(entities);
    }

    @Override
    public void deleteById(TaskId taskId) {
        jpaRepository.deleteById(taskId.getValue());
    }

    @Override
    public boolean existsById(TaskId taskId) {
        return jpaRepository.existsById(taskId.getValue());
    }
}
