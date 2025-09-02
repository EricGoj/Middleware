package com.acme.middleware.infrastructure.sync.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.acme.middleware.domain.model.Event;
import com.acme.middleware.domain.port.EventRepository;
import com.acme.middleware.infrastructure.sync.entity.EventEntity;
import com.acme.middleware.infrastructure.sync.mapper.EventPersistenceMapper;
import com.acme.middleware.infrastructure.sync.repository.SpringDataEventJpaRepository;

@Component
public class EventRepositoryAdapter implements EventRepository {
    
    private final SpringDataEventJpaRepository jpaRepository;
    private final EventPersistenceMapper eventPersistenceMapper;
    
    public EventRepositoryAdapter(SpringDataEventJpaRepository jpaRepository, EventPersistenceMapper eventPersistenceMapper) {
        this.jpaRepository = jpaRepository;
        this.eventPersistenceMapper = eventPersistenceMapper;
    }
    
    @Override
    public List<Event> findPendingEvents() {
        return jpaRepository.findAll().stream()
                .map(eventPersistenceMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Event save(Event event) {
        EventEntity entity = eventPersistenceMapper.toEntity(event);
        EventEntity savedEntity = jpaRepository.save(entity);
        return eventPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}
