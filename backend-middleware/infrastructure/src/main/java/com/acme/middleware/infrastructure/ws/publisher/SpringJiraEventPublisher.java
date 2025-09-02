package com.acme.middleware.infrastructure.ws.publisher;

import com.acme.middleware.application.port.JiraEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SpringJiraEventPublisher implements JiraEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public SpringJiraEventPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void publish(Object event) {
        // Frontend subscribes to /topic/jira-events
        messagingTemplate.convertAndSend("/topic/jira-events", event);
    }
}
