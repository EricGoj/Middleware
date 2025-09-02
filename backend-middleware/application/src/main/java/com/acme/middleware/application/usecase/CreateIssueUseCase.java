package com.acme.middleware.application.usecase;

import org.springframework.stereotype.Component;

import com.acme.middleware.application.dto.CreateIssueCommand;
import com.acme.middleware.application.dto.IssueDto;

@Component
public interface CreateIssueUseCase {
    IssueDto execute(CreateIssueCommand command);
}