package com.acme.middleware.domain;

import com.acme.middleware.domain.model.Task;
import com.acme.middleware.domain.model.TaskId;
import com.acme.middleware.domain.model.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TaskTest {

    @Test
    void shouldCreateTaskWithValidData() {
        // Given
        TaskId taskId = TaskId.generate();
        String title = "Test Task";
        String description = "Test Description";

        // When
        Task task = Task.create(taskId, title, description);

        // Then
        assertThat(task.getId()).isEqualTo(taskId);
        assertThat(task.getTitle()).isEqualTo(title);
        assertThat(task.getDescription()).isEqualTo(description);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.PENDING);
        assertThat(task.getCreatedAt()).isNotNull();
        assertThat(task.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenTitleIsNull() {
        // Given
        TaskId taskId = TaskId.generate();

        // When & Then
        assertThatThrownBy(() -> Task.create(taskId, null, "description"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task title cannot be null or empty");
    }

    @Test
    void shouldThrowExceptionWhenTitleIsEmpty() {
        // Given
        TaskId taskId = TaskId.generate();

        // When & Then
        assertThatThrownBy(() -> Task.create(taskId, "  ", "description"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task title cannot be null or empty");
    }

    @Test
    void shouldUpdateTitle() {
        // Given
        Task task = Task.create(TaskId.generate(), "Original Title", "Description");
        String newTitle = "Updated Title";

        // When
        task.updateTitle(newTitle);

        // Then
        assertThat(task.getTitle()).isEqualTo(newTitle);
        assertThat(task.getUpdatedAt()).isAfter(task.getCreatedAt());
    }

    @Test
    void shouldUpdateStatus() {
        // Given
        Task task = Task.create(TaskId.generate(), "Title", "Description");

        // When
        task.updateStatus(TaskStatus.IN_PROGRESS);

        // Then
        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(task.getUpdatedAt()).isAfter(task.getCreatedAt());
    }
}
