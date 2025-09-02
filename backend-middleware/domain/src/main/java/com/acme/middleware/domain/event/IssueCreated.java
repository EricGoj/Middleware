package com.acme.middleware.domain.event;

import com.acme.middleware.domain.model.IssueId;

import java.time.Instant;
import java.util.Objects;

public final class IssueCreated implements DomainEvent {

    private final IssueId issueId;
    private final String title;
    private final String description;
    private final Instant occurredAt;

    public IssueCreated(IssueId issueId, String title, String description) {
        this.issueId = Objects.requireNonNull(issueId);
        this.title = Objects.requireNonNull(title);
        this.description = description;
        this.occurredAt = Instant.now();
    }

    public IssueId getIssueId() {
        return issueId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssueCreated that = (IssueCreated) o;
        return Objects.equals(issueId, that.issueId) && Objects.equals(occurredAt, that.occurredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueId, occurredAt);
    }
}