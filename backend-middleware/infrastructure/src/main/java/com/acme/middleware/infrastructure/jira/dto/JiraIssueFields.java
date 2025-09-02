package com.acme.middleware.infrastructure.jira.dto;

import com.acme.middleware.infrastructure.jira.dto.adf.AdfDocument;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Fields for Jira issue creation request
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record JiraIssueFields(
        @JsonProperty("project") JiraProject project,
        @JsonProperty("summary") String summary,
        @JsonProperty("description") AdfDocument description,
        @JsonProperty("issuetype") JiraIssueType issuetype,
        @JsonProperty("duedate") String duedate,
        @JsonProperty("priority") JiraPriority priority
) {
    public static JiraIssueFields of(String projectKey, String summary, String descriptionText, String issueTypeName) {
        return new JiraIssueFields(
                new JiraProject(projectKey),
                summary,
                AdfDocument.of(descriptionText),
                new JiraIssueType(issueTypeName),
                null,
                null
        );
    }
    
    public static JiraIssueFields of(String projectKey, String summary, String descriptionText, String issueTypeName, String duedate, String priorityName) {
        return new JiraIssueFields(
                new JiraProject(projectKey),
                summary,
                AdfDocument.of(descriptionText),
                new JiraIssueType(issueTypeName),
                duedate,
                priorityName != null ? JiraPriority.of(priorityName) : null
        );
    }
}

record JiraProject(@JsonProperty("key") String key) {}
record JiraIssueType(@JsonProperty("name") String name) {}