package com.acme.middleware.infrastructure.jira.dto.adf;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a text node in Atlassian Document Format (ADF)
 */
public record AdfText(
        @JsonProperty("type") String type,
        @JsonProperty("text") String text
) {
    public static AdfText of(String text) {
        return new AdfText("text", text);
    }
}