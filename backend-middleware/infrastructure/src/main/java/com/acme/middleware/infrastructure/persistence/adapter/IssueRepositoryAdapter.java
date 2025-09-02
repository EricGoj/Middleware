package com.acme.middleware.infrastructure.persistence.adapter;

import com.acme.middleware.domain.model.Issue;
import com.acme.middleware.domain.port.IssueRepository;
import com.acme.middleware.infrastructure.persistence.entity.IssueEntity;
import com.acme.middleware.infrastructure.persistence.mapper.IssuePersistenceMapper;
import com.acme.middleware.infrastructure.persistence.repository.SpringDataIssueJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class IssueRepositoryAdapter implements IssueRepository {

    private final SpringDataIssueJpaRepository jpaRepository;
    private final IssuePersistenceMapper mapper;

    public IssueRepositoryAdapter(SpringDataIssueJpaRepository jpaRepository, IssuePersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Issue save(Issue issue) {
        IssueEntity entity = mapper.toEntity(issue);
        IssueEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Issue> findById(UUID issueId) {
        return jpaRepository.findById(issueId)
                .map(mapper::toDomain);
    }

    @Override
    public List<Issue> findAll() {
        List<IssueEntity> entities = jpaRepository.findAll();
        return mapper.toDomain(entities);
    }

    @Override
    public void deleteById(UUID issueId) {
        jpaRepository.deleteById(issueId);
    }

    @Override
    public boolean existsById(UUID issueId) {
        return jpaRepository.existsById(issueId);
    }
}