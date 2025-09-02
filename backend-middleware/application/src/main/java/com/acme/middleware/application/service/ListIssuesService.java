package com.acme.middleware.application.service;

import com.acme.middleware.application.dto.IssueDto;
import com.acme.middleware.application.mapper.IssueApplicationMapper;
import com.acme.middleware.application.usecase.ListIssuesUseCase;
import com.acme.middleware.domain.model.Issue;
import com.acme.middleware.domain.port.IssueRepository;
import java.util.List;
public class ListIssuesService implements ListIssuesUseCase {

    private final IssueRepository issueRepository;
    private final IssueApplicationMapper mapper;

    public ListIssuesService(IssueRepository issueRepository, IssueApplicationMapper mapper) {
        this.issueRepository = issueRepository;
        this.mapper = mapper;
    }

    @Override
    public List<IssueDto> execute() {
        List<Issue> issues = issueRepository.findAll();

        // Note: Jira sync functionality moved to dedicated JiraIssueService
        return mapper.toDto(issues);
    }
}