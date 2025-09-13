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
        this.description = validateDescription(description);
        this.status = Objects.requireNonNull(status, "Issue status cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created at cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated at cannot be null");
        this.dueDate = dueDate;
        this.priority = validatePriority(priority);
        this.businessKey = businessKey;
        this.syncStatus = IssueConstants.SYNC_PENDING;
    }

    public static Issue create(IssueId id, String title, String description) {
        return create(id, title, description, null, null);
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
        this.description = validateDescription(description);
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

    public void updateBusinessKey(String businessKey) {
        this.businessKey = businessKey;
        this.updatedAt = Instant.now();
    }

    public void updateDueDate(Instant dueDate) {
        this.dueDate = dueDate;
        this.updatedAt = Instant.now();
    }

    public void updatePriority(String priority) {
        this.priority = validatePriority(priority);
        this.updatedAt = Instant.now();
    }

    // Fixed: linkToBusinessKey now consistently updates updatedAt
    public void linkToBusinessKey(String businessKey) {
        this.businessKey = businessKey;
        this.updatedAt = Instant.now();
    }

    private String validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Issue title cannot be null or empty");
        }
        String trimmedTitle = title.trim();
        if (trimmedTitle.length() > IssueConstants.MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException("Issue title cannot exceed " + IssueConstants.MAX_TITLE_LENGTH + " characters");
        }
        return trimmedTitle;
    }

    private String validateDescription(String description) {
        if (description != null && description.length() > IssueConstants.MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("Issue description cannot exceed " + IssueConstants.MAX_DESCRIPTION_LENGTH + " characters");
        }
        return description;
    }

    private String validatePriority(String priority) {
        if (priority == null) {
            return IssueConstants.DEFAULT_PRIORITY;
        }
        String upperPriority = priority.toUpperCase();
        if (!isValidPriority(upperPriority)) {
            return IssueConstants.DEFAULT_PRIORITY;
        }
        return upperPriority;
    }

    private boolean isValidPriority(String priority) {
        return IssueConstants.HIGH_PRIORITY.equals(priority) || 
               IssueConstants.DEFAULT_PRIORITY.equals(priority) || 
               IssueConstants.LOW_PRIORITY.equals(priority);
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
                ", priority='" + priority + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}