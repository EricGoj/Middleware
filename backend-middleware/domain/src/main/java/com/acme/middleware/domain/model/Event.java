package com.acme.middleware.domain.model;

import java.time.Instant;
import java.util.Objects;

public class Event {

    private String id;
    private String eventType;
    private IssueId entityId;
    private String payload;
    private String status;
    private Integer retryCount;
    private Instant createdAt;
    private Instant processedAt;
    private String error;
    
    public Event(String issueId, String eventType, IssueId entityId, String title, String description, String priority) {
        this.id = issueId;
        this.eventType = eventType;
        this.entityId = Objects.requireNonNull(entityId, "Issue id cannot be null");
        this.payload = String.format("{\"title\":\"%s\",\"description\":\"%s\",\"priority\":\"%s\"}", title, description, priority);
        this.status = "PENDING";
        this.retryCount = 0;
        this.createdAt = Instant.now();
        this.processedAt = null;
        this.error = null;
    }

    public Event(String id, String eventType, IssueId entityId, String payload, String status, Integer retryCount, Instant createdAt, Instant processedAt, String error) {
        this.id = id;
        this.eventType = eventType;
        this.entityId = entityId;
        this.payload = payload;
        this.status = status;
        this.retryCount = retryCount;
        this.createdAt = createdAt;
        this.processedAt = processedAt;
        this.error = error;
    }

    public static Event restore(String id, String eventType, IssueId entityId, String payload, String status, Integer retryCount, Instant createdAt, Instant processedAt, String error) {
        return new Event(id, eventType, entityId, payload, status, retryCount, createdAt, processedAt, error);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public IssueId getEntityId() {
        return entityId;
    }

    public void setEntityId(IssueId entityId) {
        this.entityId = entityId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    public String getError() {
        return error;
    }


    public void setError(String error) {
        this.error = error;
    }
}
