package com.acme.middleware.infrastructure.jira;

import com.acme.middleware.application.service.jira.JiraIssueService;
import com.acme.middleware.infrastructure.jira.dto.JiraCreateIssueRequest;
import com.acme.middleware.infrastructure.jira.dto.JiraIssueFields;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class JiraIntegrationTest {

    @Autowired
    private JiraIssueService jiraIssueService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateJiraIssueRequestWithAllFields() throws Exception {
        // Given
        JiraIssueFields fields = JiraIssueFields.of(
            "MELICHALL",
            "Mi Task Title",
            "Descripción detallada de la tarea",
            "Task",
            "2025-09-05",
            "High"
        );
        JiraCreateIssueRequest request = JiraCreateIssueRequest.of(fields);

        // When
        String json = objectMapper.writeValueAsString(request);

        // Then
        assertThat(json).contains("\"project\":{\"key\":\"MELICHALL\"}");
        assertThat(json).contains("\"summary\":\"Mi Task Title\"");
        assertThat(json).contains("\"duedate\":\"2025-09-05\"");
        assertThat(json).contains("\"priority\":{\"name\":\"High\"}");
        assertThat(json).contains("\"issuetype\":{\"name\":\"Task\"}");
        assertThat(json).contains("\"type\":\"doc\"");
        assertThat(json).contains("\"version\":1");
        assertThat(json).contains("\"type\":\"paragraph\"");
        assertThat(json).contains("\"type\":\"text\"");
        assertThat(json).contains("\"text\":\"Descripción detallada de la tarea\"");
    }

    @Test
    void shouldCreateJiraIssueRequestWithOptionalFields() throws Exception {
        // Given - only required fields
        JiraIssueFields fields = JiraIssueFields.of(
            "MELICHALL",
            "Simple Task",
            "Simple description",
            "Task"
        );
        JiraCreateIssueRequest request = JiraCreateIssueRequest.of(fields);

        // When
        String json = objectMapper.writeValueAsString(request);

        // Then - optional fields should not be present
        assertThat(json).doesNotContain("duedate");
        assertThat(json).doesNotContain("priority");
        assertThat(json).contains("\"project\":{\"key\":\"MELICHALL\"}");
        assertThat(json).contains("\"summary\":\"Simple Task\"");
    }

    @Test
    void jiraIssueServiceShouldBeConfigured() {
        // Verify that JiraIssueService is properly configured
        assertThat(jiraIssueService).isNotNull();
    }
}