package com.acme.middleware.infrastructure.rest.controller;

import com.acme.middleware.application.dto.CreateTaskCommand;
import com.acme.middleware.application.dto.TaskDto;
import com.acme.middleware.application.dto.UpdateTaskCommand;
import com.acme.middleware.application.usecase.*;
import com.acme.middleware.infrastructure.rest.dto.TaskRequest;
import com.acme.middleware.infrastructure.rest.dto.TaskResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Task management operations")
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final GetTaskUseCase getTaskUseCase;
    private final ListTasksUseCase listTasksUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;

    public TaskController(CreateTaskUseCase createTaskUseCase,
                         GetTaskUseCase getTaskUseCase,
                         ListTasksUseCase listTasksUseCase,
                         UpdateTaskUseCase updateTaskUseCase,
                         DeleteTaskUseCase deleteTaskUseCase) {
        this.createTaskUseCase = createTaskUseCase;
        this.getTaskUseCase = getTaskUseCase;
        this.listTasksUseCase = listTasksUseCase;
        this.updateTaskUseCase = updateTaskUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
    }

    @PostMapping
    @Operation(summary = "Create a new task", 
               description = "Creates a new task with the provided details",
               requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                   description = "Task creation request",
                   content = @io.swagger.v3.oas.annotations.media.Content(
                       mediaType = "application/json",
                       examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                           name = "Sample Task",
                           summary = "Example task creation",
                           value = """
                           {
                             "title": "Complete project documentation",
                             "description": "Write comprehensive documentation for the middleware project",
                             "dueDate": "2024-12-31T23:59:59Z",
                             "priority": "HIGH",
                             "status": "TODO"
                           }
                           """
                       )
                   )
               ))
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        CreateTaskCommand command = new CreateTaskCommand(request.title(), request.description(), request.dueDate(), request.priority());
        TaskDto taskDto = createTaskUseCase.execute(command);
        TaskResponse response = mapToResponse(taskDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID")
    public ResponseEntity<TaskResponse> getTask(@PathVariable UUID id) {
        TaskDto taskDto = getTaskUseCase.execute(id);
        TaskResponse response = mapToResponse(taskDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List all tasks")
    public ResponseEntity<List<TaskResponse>> listTasks() {
        List<TaskDto> taskDtos = listTasksUseCase.execute();
        List<TaskResponse> responses = taskDtos.stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update task")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable UUID id, 
                                                  @Valid @RequestBody TaskRequest request) {
        UpdateTaskCommand command = new UpdateTaskCommand(
                request.title(), 
                request.description(), 
                request.status(),
                request.dueDate(),
                request.priority()
        );
        TaskDto taskDto = updateTaskUseCase.execute(id, command);
        TaskResponse response = mapToResponse(taskDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        deleteTaskUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    private TaskResponse mapToResponse(TaskDto taskDto) {
        return new TaskResponse(
                taskDto.id(),
                taskDto.title(),
                taskDto.description(),
                taskDto.status(),
                taskDto.createdAt(),
                taskDto.updatedAt(),
                taskDto.dueDate(),
                taskDto.priority()
        );
    }
}