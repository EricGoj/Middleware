package com.acme.middleware.application.service;

import com.acme.middleware.application.dto.CreateIssueCommand;
import com.acme.middleware.application.dto.IssueDto;
import com.acme.middleware.application.mapper.IssueApplicationMapper;
import com.acme.middleware.application.usecase.CreateIssueUseCase;
import com.acme.middleware.domain.event.IssueCreated;
import com.acme.middleware.domain.model.Event;
import com.acme.middleware.domain.model.Issue;
import com.acme.middleware.domain.model.IssueId;
import com.acme.middleware.domain.port.DomainEventPublisher;
import com.acme.middleware.domain.port.EventRepository;
import com.acme.middleware.domain.port.IssueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateIssueService implements CreateIssueUseCase {

    private final IssueRepository issueRepository;
    private final EventRepository eventRepository;
    private final DomainEventPublisher eventPublisher;
    private final IssueApplicationMapper mapper;


    public CreateIssueService(IssueRepository issueRepository, 
                           EventRepository eventRepository,
                           DomainEventPublisher eventPublisher,
                           IssueApplicationMapper mapper) {
        this.issueRepository = issueRepository;
        this.eventRepository = eventRepository;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public IssueDto execute(CreateIssueCommand command) {
        IssueId issueId = IssueId.generate();
        Issue issue = Issue.create(issueId, command.title(), command.description(), command.dueDate(), command.priority());
        
        Issue savedIssue = issueRepository.save(issue);

        IssueCreated issueCreated = new IssueCreated(savedIssue.getId(), savedIssue.getTitle(), savedIssue.getDescription());

        eventPublisher.publish(issueCreated);

        Event event = new Event(java.util.UUID.randomUUID().toString(), "IssueCreated", savedIssue.getId(), savedIssue.getTitle(), savedIssue.getDescription(), savedIssue.getPriority()); 
        eventRepository.save(event);

        // Note: Jira integration moved to dedicated JiraIssueService
        // Business key can be set separately via the Jira API endpoints
        
        return mapper.toDto(savedIssue);
    }


}