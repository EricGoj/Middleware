package com.acme.middleware.application.usecase;

import org.springframework.stereotype.Component;

import com.acme.middleware.application.dto.IssueDto;
import com.acme.middleware.application.dto.UpdateIssueCommand;

import java.util.UUID;

@Component
public interface UpdateIssueUseCase {
    IssueDto execute(UUID issueId, UpdateIssueCommand command);
}