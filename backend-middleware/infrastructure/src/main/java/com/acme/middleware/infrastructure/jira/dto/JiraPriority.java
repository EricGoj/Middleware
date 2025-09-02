package com.acme.middleware.infrastructure.jira.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Jira priority field
 */
public record JiraPriority(
        @JsonProperty("name") String name
) {
    public static JiraPriority of(String name) {
        return new JiraPriority(name);
    }
}