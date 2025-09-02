package com.acme.middleware.infrastructure.sync;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.acme.middleware.application.service.jira.ProcessJiraSyncEventService;
import com.acme.middleware.domain.model.Event;
import com.acme.middleware.domain.port.EventRepository;
   
@Component
public class SyncEventProcessor {

    private final EventRepository eventRepository;
    private final ProcessJiraSyncEventService processJiraSyncEventService;
    private final Logger log = LoggerFactory.getLogger(SyncEventProcessor.class);

    public SyncEventProcessor(EventRepository eventRepository, ProcessJiraSyncEventService processJiraSyncEventService) {
        this.eventRepository = eventRepository;
        this.processJiraSyncEventService = processJiraSyncEventService;
    }

    @Scheduled(fixedRate = 5000)
    public void syncEvents() {

        log.info(" --- Procesando eventos pendientes ---");

        List<Event> events = eventRepository.findPendingEvents();

        log.info(" --- Eventos pendientes: {} ---", events.size());

        if(!events.isEmpty()) {
            events.forEach(event -> {
                try {
                    processJiraSyncEventService.execute(event);
                } catch (Exception e) {
                    log.error("Error processing event: {}", e.getMessage(), e);
                }  
            });
        }
    }

    

}
