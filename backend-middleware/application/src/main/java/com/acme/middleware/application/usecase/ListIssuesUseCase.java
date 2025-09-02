package com.acme.middleware.application.usecase;

import com.acme.middleware.application.dto.IssueDto;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface ListIssuesUseCase {
    List<IssueDto> execute();
}