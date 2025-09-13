package com.acme.middleware.domain.model;

import java.time.Instant;
import java.util.Objects;

public class Issue {

    private IssueId id;
    private String title;
    private String description;
    private IssueStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant dueDate;
    private String priority;
    private String businessKey;
    private String syncStatus;

    private Issue(IssueId id, String title, String description, IssueStatus status, Instant createdAt, Instant updatedAt, Instant dueDate, String priority, String businessKey) {
        this.id = Objects.requireNonNull(id, "Issue id cannot be null");
        this.title = validateTitle(title);
        this.description = description;
        this.status = Objects.requireNonNull(status, "Issue status cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
        this.dueDate = dueDate;
        this.priority = priority != null ? priority : "Medium";
        this.businessKey = businessKey;
        this.syncStatus = "Pending";
    }

    public static Issue create(IssueId id, String title, String description) {
        Instant now = Instant.now();
        return new Issue(id, title, description, IssueStatus.PENDING, now, now, null, "Medium", null);
    }

    public static Issue create(IssueId id, String title, String description, Instant dueDate, String priority) {
        Instant now = Instant.now();
        return new Issue(id, title, description, IssueStatus.PENDING, now, now, dueDate, priority, null);
    }

    public static Issue restore(IssueId id, String title, String description, IssueStatus status, Instant createdAt, Instant updatedAt, Instant dueDate, String priority, String businessKey) {
        return new Issue(id, title, description, status, createdAt, updatedAt, dueDate, priority, businessKey);
    }

    public void updateTitle(String title) {
        this.title = validateTitle(title);
        this.updatedAt = Instant.now();
    }

    public void updateDescription(String description) {
        this.description = description;
        this.updatedAt = Instant.now();
    }

    public void updateStatus(IssueStatus status) {
        this.status = Objects.requireNonNull(status, "Issue status cannot be null");
        this.updatedAt = Instant.now();
    }

    public void updateSyncStatus(String syncStatus) {
        this.syncStatus = Objects.requireNonNull(syncStatus, "Sync status cannot be null");
        this.updatedAt = Instant.now();
    }

    private String validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Issue title cannot be null or empty");
        }
        return title.trim();
    }

    public void updateBusinessKey(String businessKey) {
        this.businessKey = businessKey;
        this.updatedAt = Instant.now();
    }

    // Getters
    public IssueId getId() {
        return id;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void updateDueDate(Instant dueDate) {
        this.dueDate = dueDate;
        this.updatedAt = Instant.now();
    }

    public void updatePriority(String priority) {
        this.priority = priority != null ? priority : "MEDIUM";
        this.updatedAt = Instant.now();
    }

    public void linkToBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Issue issue = (Issue) o;
        return Objects.equals(id, issue.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}