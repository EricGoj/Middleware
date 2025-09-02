package com.acme.middleware.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public interface DeleteIssueUseCase {
    void execute(UUID issueId);
}