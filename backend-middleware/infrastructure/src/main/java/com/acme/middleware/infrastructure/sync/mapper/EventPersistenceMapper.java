package com.acme.middleware.infrastructure.sync.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.acme.middleware.infrastructure.sync.entity.EventEntity;
import com.acme.middleware.domain.model.Event;
import com.acme.middleware.domain.model.IssueId;

@Component
public class EventPersistenceMapper {

      public EventEntity toEntity(Event event) {
        if (event == null) {
            return null;
        }
        EventEntity entity = new EventEntity(
                event.getId(),
                event.getEventType(),
                event.getEntityId().getValue(),
                event.getPayload(),
                event.getStatus(),
                event.getRetryCount(),
                event.getCreatedAt(),
                event.getProcessedAt(),
                event.getError()
        );
        return entity;
    }

    public Event toDomain(EventEntity entity) {
        if (entity == null) {
            return null;
        }
        return Event.restore(
                entity.getId(),
                entity.getEventType(),
                IssueId.of(entity.getEntityId()),
                entity.getPayload(),
                entity.getStatus(),
                entity.getRetryCount(),
                entity.getCreatedAt(),
                entity.getProcessedAt(),
                entity.getError()
        );
    }

    public List<Event> toDomain(List<EventEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public EventEntity toPersistence(Event event) {
        return toEntity(event);
    }
    
}
