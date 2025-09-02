package com.acme.middleware.domain.model;

import java.util.Objects;
import java.util.UUID;

public final class IssueId {
    
    private final UUID value;

    private IssueId(UUID value) {
        this.value = Objects.requireNonNull(value, "IssueId value cannot be null");
    }

    public static IssueId of(UUID value) {
        return new IssueId(value);
    }

    public static IssueId of(String value) {
        return new IssueId(UUID.fromString(value));
    }

    public static IssueId generate() {
        return new IssueId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssueId issueId = (IssueId) o;
        return Objects.equals(value, issueId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}