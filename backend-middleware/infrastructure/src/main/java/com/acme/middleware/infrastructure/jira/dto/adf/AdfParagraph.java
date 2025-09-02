package com.acme.middleware.infrastructure.jira.dto.adf;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a paragraph node in Atlassian Document Format (ADF)
 */
public record AdfParagraph(
        @JsonProperty("type") String type,
        @JsonProperty("content") List<AdfText> content
) {
    public static AdfParagraph of(List<AdfText> content) {
        return new AdfParagraph("paragraph", content);
    }
    
    public static AdfParagraph of(String text) {
        return new AdfParagraph("paragraph", List.of(AdfText.of(text)));
    }
}