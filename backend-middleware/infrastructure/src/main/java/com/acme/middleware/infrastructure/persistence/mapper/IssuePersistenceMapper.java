package com.acme.middleware.infrastructure.persistence.mapper;

import com.acme.middleware.domain.model.Issue;
import com.acme.middleware.domain.model.IssueId;
import com.acme.middleware.infrastructure.persistence.entity.IssueEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class IssuePersistenceMapper {

    public IssueEntity toEntity(Issue issue) {
        if (issue == null) {
            return null;
        }
        IssueEntity entity = new IssueEntity(
                issue.getId().getValue(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getStatus(),
                issue.getCreatedAt(),
                issue.getUpdatedAt(),
                issue.getBusinessKey()
        );
        entity.setDueDate(issue.getDueDate());
        entity.setPriority(issue.getPriority());
        return entity;
    }

    public Issue toDomain(IssueEntity entity) {
        if (entity == null) {
            return null;
        }
        return Issue.restore(
                IssueId.of(entity.getId()),
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

    public List<Issue> toDomain(List<IssueEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
}