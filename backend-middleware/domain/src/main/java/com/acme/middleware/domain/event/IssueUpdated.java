package com.acme.middleware.domain.event;

import com.acme.middleware.domain.model.IssueId;
import com.acme.middleware.domain.model.IssueStatus;

import java.time.Instant;
import java.util.Objects;

public final class IssueUpdated implements DomainEvent {
    private final IssueId issueId;
    private final String title;
    private final String description;
    private final IssueStatus status;
    private final Instant occurredAt;

    public IssueUpdated(IssueId issueId, String title, String description, IssueStatus status, Instant occurredAt) {
        this.issueId = Objects.requireNonNull(issueId);
        this.title = Objects.requireNonNull(title);
        this.description = description;
        this.status = Objects.requireNonNull(status);
        this.occurredAt = Objects.requireNonNull(occurredAt);
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

    public IssueStatus getStatus() {
        return status;
    }

    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssueUpdated that = (IssueUpdated) o;
        return Objects.equals(issueId, that.issueId) && Objects.equals(occurredAt, that.occurredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueId, occurredAt);
    }
}