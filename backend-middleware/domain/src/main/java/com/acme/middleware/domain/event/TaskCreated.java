package com.acme.middleware.domain.event;

import com.acme.middleware.domain.model.TaskId;

import java.time.Instant;
import java.util.Objects;

public final class TaskCreated implements DomainEvent {
    private final TaskId taskId;
    private final String title;
    private final String description;
    private final Instant occurredAt;

    public TaskCreated(TaskId taskId, String title, String description, Instant occurredAt) {
        this.taskId = Objects.requireNonNull(taskId);
        this.title = Objects.requireNonNull(title);
        this.description = description;
        this.occurredAt = Objects.requireNonNull(occurredAt);
    }

    public TaskId getTaskId() {
        return taskId;
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
        TaskCreated that = (TaskCreated) o;
        return Objects.equals(taskId, that.taskId) && Objects.equals(occurredAt, that.occurredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, occurredAt);
    }
}