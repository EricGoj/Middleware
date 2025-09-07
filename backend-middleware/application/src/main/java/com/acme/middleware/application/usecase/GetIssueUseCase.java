package com.acme.middleware.application.usecase;

import org.springframework.stereotype.Component;

import com.acme.middleware.application.dto.IssueDto;

import java.util.UUID;

// Removed @Component annotation - interfaces should not be annotated as components
public interface GetIssueUseCase {
    IssueDto execute(UUID issueId);
}