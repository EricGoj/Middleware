package com.acme.middleware.domain.event;

import com.acme.middleware.domain.model.TaskId;

import java.time.Instant;
import java.util.Objects;

public final class TaskDeleted implements DomainEvent {
    private final TaskId taskId;
    private final Instant occurredAt;

    public TaskDeleted(TaskId taskId, Instant occurredAt) {
        this.taskId = Objects.requireNonNull(taskId);
        this.occurredAt = Objects.requireNonNull(occurredAt);
    }

    public TaskId getTaskId() {
        return taskId;
    }

    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDeleted that = (TaskDeleted) o;
        return Objects.equals(taskId, that.taskId) && Objects.equals(occurredAt, that.occurredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, occurredAt);
    }
}