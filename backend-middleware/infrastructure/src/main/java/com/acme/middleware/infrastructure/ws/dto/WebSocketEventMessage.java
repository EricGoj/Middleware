package com.acme.middleware.infrastructure.ws.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Unified WebSocket event message format for real-time communication
 * Matches the frontend expected format for /topic/jira-events
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebSocketEventMessage {
    
    private String type;
    private Object task;
    private String id;
    
    public WebSocketEventMessage() {}
    
    public WebSocketEventMessage(String type, Object task) {
        this.type = type;
        this.task = task;
    }
    
    public WebSocketEventMessage(String type, String id) {
        this.type = type;
        this.id = id;
    }
    
    // Getters and setters
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Object getTask() {
        return task;
    }
    
    public void setTask(Object task) {
        this.task = task;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
}