package com.acme.middleware.infrastructure.jira.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response DTO for Jira issue creation via REST API v3
 */
public record JiraCreateIssueResponse(
        @JsonProperty("id") String id,
        @JsonProperty("key") String key,
        @JsonProperty("self") String self
) {}