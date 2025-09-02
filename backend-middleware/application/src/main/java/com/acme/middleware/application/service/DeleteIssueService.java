package com.acme.middleware.application.service;

import com.acme.middleware.application.usecase.DeleteIssueUseCase;
import com.acme.middleware.domain.event.IssueDeleted;
import com.acme.middleware.domain.model.Issue;
import com.acme.middleware.domain.port.DomainEventPublisher;
import com.acme.middleware.domain.port.IssueRepository;
import com.acme.middleware.application.exceptions.IssueNotFoundException;
import java.time.Instant;
import java.util.UUID;

public class DeleteIssueService implements DeleteIssueUseCase {

    private final IssueRepository issueRepository;
    private final DomainEventPublisher eventPublisher;

    public DeleteIssueService(IssueRepository issueRepository, DomainEventPublisher eventPublisher) {
        this.issueRepository = issueRepository;
        this.eventPublisher = eventPublisher;
    }   

    @Override
    public void execute(UUID issue) {

        Issue issueDel = issueRepository.findById(issue)
            .orElseThrow(() -> new IssueNotFoundException("Issue not found with id: " + issue));
        
        issueRepository.deleteById(issueDel.getId().getValue());

        // Note: Jira integration moved to dedicated JiraIssueService

        IssueDeleted event = new IssueDeleted(issueDel.getId(), Instant.now());
        //eventPublisher.publish(event);
    }
}