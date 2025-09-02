package com.acme.middleware.infrastructure.jira.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request DTO for creating a Jira issue via REST API v3
 */
public record JiraCreateIssueRequest(
        @JsonProperty("fields") JiraIssueFields fields
) {
    public static JiraCreateIssueRequest of(JiraIssueFields fields) {
        return new JiraCreateIssueRequest(fields);
    }
}