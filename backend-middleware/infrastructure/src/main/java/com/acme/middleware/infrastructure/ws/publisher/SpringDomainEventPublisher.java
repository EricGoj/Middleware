package com.acme.middleware.infrastructure.ws.publisher;

import com.acme.middleware.domain.event.DomainEvent;
import com.acme.middleware.domain.event.TaskCreated;
import com.acme.middleware.domain.event.TaskDeleted;
import com.acme.middleware.domain.event.TaskUpdated;
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
        if (event instanceof TaskCreated taskCreated) {
            messagingTemplate.convertAndSend("/topic/tasks.created", taskCreated);
        } else if (event instanceof TaskUpdated taskUpdated) {
            messagingTemplate.convertAndSend("/topic/tasks.updated", taskUpdated);
        } else if (event instanceof TaskDeleted taskDeleted) {
            messagingTemplate.convertAndSend("/topic/tasks.deleted", taskDeleted);
        }
        // Log unknown event type if needed
    }
}