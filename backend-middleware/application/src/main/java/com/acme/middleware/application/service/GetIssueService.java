package com.acme.middleware.application.service;

import com.acme.middleware.application.dto.IssueDto;
import com.acme.middleware.application.exceptions.IssueNotFoundException;
import com.acme.middleware.application.mapper.IssueApplicationMapper;
import com.acme.middleware.application.usecase.GetIssueUseCase;
import com.acme.middleware.domain.model.Issue;
import com.acme.middleware.domain.port.IssueRepository;

import java.util.UUID;

public class GetIssueService implements GetIssueUseCase {

    private final IssueRepository issueRepository;
    private final IssueApplicationMapper mapper;

    public GetIssueService(IssueRepository issueRepository, IssueApplicationMapper mapper) {
        this.issueRepository = issueRepository;
        this.mapper = mapper;
    }

    @Override
    public IssueDto execute(UUID issueId) {
        Issue issue = issueRepository.findById(issueId)
            .orElseThrow(() -> new IssueNotFoundException("Issue not found with id: " + issueId));
        
        return mapper.toDto(issue);
    }
}