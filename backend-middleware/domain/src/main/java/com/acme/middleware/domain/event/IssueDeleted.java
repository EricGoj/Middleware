package com.acme.middleware.domain.event;

import com.acme.middleware.domain.model.IssueId;

import java.time.Instant;
import java.util.Objects;

public final class IssueDeleted implements DomainEvent {
    private final IssueId issueId;
    private final Instant occurredAt;

    public IssueDeleted(IssueId issueId, Instant occurredAt) {
        this.issueId = Objects.requireNonNull(issueId);
        this.occurredAt = Objects.requireNonNull(occurredAt);
    }

    public IssueId getIssueId() {
        return issueId;
    }

    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssueDeleted that = (IssueDeleted) o;
        return Objects.equals(issueId, that.issueId) && Objects.equals(occurredAt, that.occurredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueId, occurredAt);
    }
}