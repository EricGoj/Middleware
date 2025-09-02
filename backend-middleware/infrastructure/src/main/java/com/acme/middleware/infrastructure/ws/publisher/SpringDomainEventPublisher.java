package com.acme.middleware.infrastructure.ws.publisher;

import com.acme.middleware.domain.event.DomainEvent;
import com.acme.middleware.domain.event.IssueCreated;
import com.acme.middleware.domain.event.IssueUpdated;
import com.acme.middleware.domain.port.DomainEventPublisher;
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
        if (event instanceof IssueCreated issueCreated) {
            messagingTemplate.convertAndSend("/topic/issues.created", issueCreated);
        } else if (event instanceof IssueUpdated issueUpdated) {
            messagingTemplate.convertAndSend("/topic/issues.updated", issueUpdated);
        }
    }
}