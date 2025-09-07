package com.acme.middleware.application.service;

import com.acme.middleware.application.dto.IssueDto;
import com.acme.middleware.application.dto.UpdateIssueCommand;
import com.acme.middleware.application.exceptions.IssueNotFoundException;
import com.acme.middleware.application.mapper.IssueApplicationMapper;
import com.acme.middleware.application.usecase.UpdateIssueUseCase;
import com.acme.middleware.domain.event.IssueUpdated;
import com.acme.middleware.domain.model.Issue;
import com.acme.middleware.domain.port.DomainEventPublisher;
import com.acme.middleware.domain.port.IssueRepository;

import java.time.Instant;
import java.util.UUID;

public class UpdateIssueService implements UpdateIssueUseCase {

    private final IssueRepository issueRepository;
    private final DomainEventPublisher eventPublisher;
    private final IssueApplicationMapper mapper;

    public UpdateIssueService(IssueRepository issueRepository,
                           DomainEventPublisher eventPublisher,
                           IssueApplicationMapper mapper) {
        this.issueRepository = issueRepository;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public IssueDto execute(UUID issueId, UpdateIssueCommand command) {
        Issue issue = issueRepository.findById(issueId)
            .orElseThrow(() -> new IssueNotFoundException("Issue not found with id: " + issueId));

        if (command.title() != null) {
            issue.updateTitle(command.title());
        }
        if (command.description() != null) {
            issue.updateDescription(command.description());
        }
        if (command.status() != null) {
            issue.updateStatus(command.status());
        }
        if (command.dueDate() != null) {
            issue.updateDueDate(command.dueDate());
        }
        if (command.priority() != null) {
            issue.updatePriority(command.priority());
        }

        Issue updatedIssue = issueRepository.save(issue);

        IssueUpdated event = new IssueUpdated(
            updatedIssue.getId(),
            updatedIssue.getTitle(),
            updatedIssue.getDescription(),
            updatedIssue.getStatus(),
            Instant.now()
        );
       // eventPublisher.publish(event);

        return mapper.toDto(updatedIssue);
    }
}