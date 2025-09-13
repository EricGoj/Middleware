package com.acme.middleware.application.usecase;

import com.acme.middleware.application.dto.IssueDto;

import java.util.List;

import org.springframework.stereotype.Component;

// Removed @Component annotation - interfaces should not be annotated as components
public interface ListIssuesUseCase {
    List<IssueDto> execute();
}