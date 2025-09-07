package com.acme.middleware.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Component;

// Removed @Component annotation - interfaces should not be annotated as components
public interface DeleteIssueUseCase {
    void execute(UUID issueId);
}