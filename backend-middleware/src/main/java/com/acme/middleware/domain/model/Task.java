package com.acme.middleware.domain.model;

import java.time.Instant;
import java.util.Objects;

public final class Task {
    private final TaskId id;
    private String title;
    private String description;
    private TaskStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    private Task(TaskId id, String title, String description, TaskStatus status, Instant createdAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "Task id cannot be null");
        this.title = validateTitle(title);
        this.description = description;
        this.status = Objects.requireNonNull(status, "Task status cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
    }

    public static Task create(TaskId id, String title, String description) {
        Instant now = Instant.now();
        return new Task(id, title, description, TaskStatus.PENDING, now, now);
    }

    public static Task restore(TaskId id, String title, String description, TaskStatus status, Instant createdAt, Instant updatedAt) {
        return new Task(id, title, description, status, createdAt, updatedAt);
    }

    public void updateTitle(String title) {
        this.title = validateTitle(title);
        this.updatedAt = Instant.now();
    }

    public void updateDescription(String description) {
        this.description = description;
        this.updatedAt = Instant.now();
    }

    public void updateStatus(TaskStatus status) {
        this.status = Objects.requireNonNull(status, "Task status cannot be null");
        this.updatedAt = Instant.now();
    }

    private String validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Task title cannot be null or empty");
        }
        return title.trim();
    }

    // Getters
    public TaskId getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
