package com.acme.middleware.application.port;

/**
 * Port to publish Jira-related events to the outside world (e.g., WebSocket/STOMP).
 * Implementations should serialize and dispatch the provided event payload as-is
 * to the configured transport (e.g., convertAndSend to a STOMP topic).
 */
public interface JiraEventPublisher {
    /**
     * Publish a generic Jira event payload. Implementations decide the destination.
     * @param event event payload (typically a Map or a DTO) that will be serialized
     */
    void publish(Object event);
}
