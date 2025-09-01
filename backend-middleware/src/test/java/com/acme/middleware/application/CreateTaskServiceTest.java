package com.acme.middleware.application;

import com.acme.middleware.application.dto.CreateTaskCommand;
import com.acme.middleware.application.dto.TaskDto;
import com.acme.middleware.application.mapper.TaskApplicationMapper;
import com.acme.middleware.application.service.CreateTaskService;
import com.acme.middleware.domain.event.TaskCreated;
import com.acme.middleware.domain.model.Task;
import com.acme.middleware.domain.model.TaskId;
import com.acme.middleware.domain.model.TaskStatus;
import com.acme.middleware.domain.port.DomainEventPublisher;
import com.acme.middleware.domain.port.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private DomainEventPublisher eventPublisher;

    @Mock
    private TaskApplicationMapper mapper;

    private CreateTaskService createTaskService;

    @BeforeEach
    void setUp() {
        createTaskService = new CreateTaskService(taskRepository, eventPublisher, mapper);
    }

    @Test
    void shouldCreateTaskSuccessfully() {
        // Given
        CreateTaskCommand command = new CreateTaskCommand("Test Task", "Test Description");
        Task savedTask = Task.create(TaskId.generate(), command.title(), command.description());
        TaskDto expectedDto = new TaskDto(
                savedTask.getId().getValue(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getStatus(),
                savedTask.getCreatedAt(),
                savedTask.getUpdatedAt()
        );

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);
        when(mapper.toDto(savedTask)).thenReturn(expectedDto);

        // When
        TaskDto result = createTaskService.execute(command);

        // Then
        assertThat(result).isEqualTo(expectedDto);
        verify(taskRepository).save(any(Task.class));
        verify(eventPublisher).publish(argThat(event -> 
                event instanceof TaskCreated && 
                ((TaskCreated) event).getTaskId().equals(savedTask.getId())
        ));
        verify(mapper).toDto(savedTask);
    }
}
