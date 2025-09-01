package com.acme.middleware.domain.model;

import java.util.Objects;
import java.util.UUID;

public final class TaskId {
    private final UUID value;

    private TaskId(UUID value) {
        this.value = Objects.requireNonNull(value, "TaskId value cannot be null");
    }

    public static TaskId of(UUID value) {
        return new TaskId(value);
    }

    public static TaskId of(String value) {
        return new TaskId(UUID.fromString(value));
    }

    public static TaskId generate() {
        return new TaskId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskId taskId = (TaskId) o;
        return Objects.equals(value, taskId.value);
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