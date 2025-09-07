package com.acme.middleware.application.usecase;

import org.springframework.stereotype.Component;

import com.acme.middleware.application.dto.CreateIssueCommand;
import com.acme.middleware.application.dto.IssueDto;

// Removed @Component annotation - interfaces should not be annotated as components
// The implementation will be annotated as @Service instead
public interface CreateIssueUseCase {
    IssueDto execute(CreateIssueCommand command);
}