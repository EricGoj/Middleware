package com.acme.middleware.domain.port;

import com.acme.middleware.domain.event.DomainEvent;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
