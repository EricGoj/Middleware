package com.acme.middleware.infrastructure.rest.controller;

import com.acme.middleware.application.dto.CreateIssueCommand;
import com.acme.middleware.application.dto.IssueDto;
import com.acme.middleware.application.dto.UpdateIssueCommand;
import com.acme.middleware.application.usecase.*;
import com.acme.middleware.infrastructure.rest.dto.IssueRequest;
import com.acme.middleware.infrastructure.rest.dto.IssueResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/issues")
@Tag(name = "Issues", description = "Issue management operations")
public class IssueController {

    private final CreateIssueUseCase createIssueUseCase;
    private final GetIssueUseCase getIssueUseCase;
    private final ListIssuesUseCase listIssuesUseCase;
    private final UpdateIssueUseCase updateIssueUseCase;
    private final DeleteIssueUseCase deleteIssueUseCase;

    public IssueController(CreateIssueUseCase createIssueUseCase,
                         GetIssueUseCase getIssueUseCase,
                         ListIssuesUseCase listIssuesUseCase,
                         UpdateIssueUseCase updateIssueUseCase,
                         DeleteIssueUseCase deleteIssueUseCase) {
        this.createIssueUseCase = createIssueUseCase;
        this.getIssueUseCase = getIssueUseCase;
        this.listIssuesUseCase = listIssuesUseCase;
        this.updateIssueUseCase = updateIssueUseCase;
        this.deleteIssueUseCase = deleteIssueUseCase;
    }

    @PostMapping
    @Operation(summary = "Create a new issue")
    public ResponseEntity<IssueResponse> createIssue(@Valid @RequestBody IssueRequest request) {
        CreateIssueCommand command = new CreateIssueCommand(request.title(), request.description(), request.dueDate(), request.priority());
        IssueDto issueDto = createIssueUseCase.execute(command);
        IssueResponse response = mapToResponse(issueDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get issue by ID")
    public ResponseEntity<IssueResponse> getIssue(@PathVariable UUID id) {
        IssueDto issueDto = getIssueUseCase.execute(id);
        IssueResponse response = mapToResponse(issueDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List all issues")
    public ResponseEntity<List<IssueResponse>> listIssues() {
        List<IssueDto> issueDtos = listIssuesUseCase.execute();
        List<IssueResponse> responses = issueDtos.stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update issue")
    public ResponseEntity<IssueResponse> updateIssue(@PathVariable UUID id, 
                                                  @Valid @RequestBody IssueRequest request) {
        UpdateIssueCommand command = new UpdateIssueCommand(
                request.title(), 
                request.description(), 
                request.status(),
                request.dueDate(),
                request.priority()
        );
        IssueDto issueDto = updateIssueUseCase.execute(id, command);
        IssueResponse response = mapToResponse(issueDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete issue")
    public ResponseEntity<Void> deleteIssue(@PathVariable UUID id) {
        deleteIssueUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    private IssueResponse mapToResponse(IssueDto issueDto) {
        return new IssueResponse(
                issueDto.id(),
                issueDto.title(),
                issueDto.description(),
                issueDto.status(),
                issueDto.createdAt(),
                issueDto.updatedAt(),
                issueDto.dueDate(),
                issueDto.priority()
        );
    }
}