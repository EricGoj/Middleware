package com.acme.middleware.infrastructure.jira.dto;

import com.acme.middleware.infrastructure.jira.dto.adf.AdfDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JiraCreateIssueRequestTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSerializeToExpectedJiraFormat() throws Exception {
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
        String expectedJson = """
            {
              "fields": {
                "project": {
                  "key": "MELICHALL"
                },
                "summary": "Mi Task Title",
                "description": {
                  "type": "doc",
                  "version": 1,
                  "content": [
                    {
                      "type": "paragraph",
                      "content": [
                        {
                          "type": "text",
                          "text": "Descripción detallada de la tarea"
                        }
                      ]
                    }
                  ]
                },
                "issuetype": {
                  "name": "Task"
                },
                "duedate": "2025-09-05",
                "priority": {
                  "name": "High"
                }
              }
            }""";

        assertThat(json).isEqualToIgnoringWhitespace(expectedJson);
    }

    @Test
    void shouldCreateAdfDocumentFromSimpleText() throws Exception {
        // Given
        AdfDocument adfDoc = AdfDocument.of("Simple text description");

        // When
        String json = objectMapper.writeValueAsString(adfDoc);

        // Then
        String expectedJson = """
            {
              "type": "doc",
              "version": 1,
              "content": [
                {
                  "type": "paragraph",
                  "content": [
                    {
                      "type": "text",
                      "text": "Simple text description"
                    }
                  ]
                }
              ]
            }""";

        assertThat(json).isEqualToIgnoringWhitespace(expectedJson);
    }
}