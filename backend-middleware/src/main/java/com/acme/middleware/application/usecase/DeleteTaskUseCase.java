package com.acme.middleware.application.usecase;

import java.util.UUID;

public interface DeleteTaskUseCase {
    void execute(UUID taskId);
}
