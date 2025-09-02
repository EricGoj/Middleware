package com.acme.middleware.application.usecase;

import com.acme.middleware.domain.model.Event;

public interface ProcessJiraSyncEventUseCase {
    void execute(Event event);
}
