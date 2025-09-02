package com.acme.middleware.domain.port;

import java.util.List;

import com.acme.middleware.domain.model.Event;


public interface EventRepository {

    List<Event> findPendingEvents();

    Event save(Event event);
    
}
