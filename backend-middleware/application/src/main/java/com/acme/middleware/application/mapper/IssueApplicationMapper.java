package com.acme.middleware.application.mapper;

import com.acme.middleware.application.dto.IssueDto;
import com.acme.middleware.domain.model.Issue;

import java.util.List;
import java.util.stream.Collectors;

public class IssueApplicationMapper {

    public IssueDto toDto(Issue issue) {
        if (issue == null) {
            return null;
        }
        return new IssueDto(
                issue.getId().getValue(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getStatus(),
                issue.getCreatedAt(),
                issue.getUpdatedAt(),
                issue.getDueDate(),
                issue.getPriority()
        );
    }

    public List<IssueDto> toDto(List<Issue> issues) {
        if (issues == null) {
            return null;
        }
        return issues.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}