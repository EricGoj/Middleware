package com.acme.middleware.infrastructure.ws.publisher;

import com.acme.middleware.domain.event.DomainEvent;
import com.acme.middleware.domain.event.IssueCreated;
import com.acme.middleware.domain.event.IssueUpdated;
import com.acme.middleware.domain.event.IssueDeleted;
import com.acme.middleware.domain.port.DomainEventPublisher;
import com.acme.middleware.infrastructure.ws.dto.WebSocketEventMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public SpringDomainEventPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void publish(DomainEvent event) {
        WebSocketEventMessage message;
        
        if (event instanceof IssueCreated issueCreated) {
            message = new WebSocketEventMessage("TASK_CREATED", createTaskFromIssue(issueCreated));
            messagingTemplate.convertAndSend("/topic/jira-events", message);
        } else if (event instanceof IssueUpdated issueUpdated) {
            message = new WebSocketEventMessage("TASK_UPDATED", createTaskFromIssue(issueUpdated));
            messagingTemplate.convertAndSend("/topic/jira-events", message);
        } else if (event instanceof IssueDeleted issueDeleted) {
            message = new WebSocketEventMessage("TASK_DELETED", issueDeleted.getIssue().getId().toString());
            messagingTemplate.convertAndSend("/topic/jira-events", message);
        }
    }
    
    /**
     * Transform domain issue to frontend task format
     */
    private Object createTaskFromIssue(DomainEvent event) {
        if (event instanceof IssueCreated created) {
            return createTaskObject(created.getIssue());
        } else if (event instanceof IssueUpdated updated) {
            return createTaskObject(updated.getIssue());
        }
        return null;
    }
    
    private Object createTaskObject(com.acme.middleware.domain.model.Issue issue) {
        return new Object() {
            public final String id = issue.getId().toString();
            public final String title = issue.getTitle();
            public final String description = issue.getDescription();
            public final String status = issue.getStatus().toString();
            public final java.time.LocalDateTime createdAt = issue.getCreatedAt();
            public final java.time.LocalDateTime updatedAt = issue.getUpdatedAt();
            public final java.time.LocalDate dueDate = issue.getDueDate();
            public final String priority = issue.getPriority();
        };
    }
}