package com.acme.middleware.infrastructure.jira.dto.adf;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents the root document node in Atlassian Document Format (ADF)
 */
public record AdfDocument(
        @JsonProperty("type") String type,
        @JsonProperty("version") int version,
        @JsonProperty("content") List<AdfParagraph> content
) {
    public static AdfDocument of(List<AdfParagraph> content) {
        return new AdfDocument("doc", 1, content);
    }
    
    public static AdfDocument of(String text) {
        return new AdfDocument("doc", 1, List.of(AdfParagraph.of(text)));
    }
}