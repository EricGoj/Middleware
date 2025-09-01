package com.acme.middleware.domain.event;

import java.time.Instant;

public interface DomainEvent {
    Instant getOccurredAt();
}
