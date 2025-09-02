package com.acme.middleware.application.usecase;

import java.util.Map;
import java.time.Instant;

public interface IssueUseCase {
    String createIssue(String summary, String description, String issueType, Instant dueDate, String priority);
    void updateIssue(String issueKey, Map<String, Object> fields);
    void deleteIssue(String issueKey);
}
