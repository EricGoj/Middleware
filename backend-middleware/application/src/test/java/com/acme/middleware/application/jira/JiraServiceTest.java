package com.acme.middleware.application.jira;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.acme.middleware.application.port.JiraEventPublisher;
import com.acme.middleware.application.usecase.IssueUseCase;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JiraServiceTest {

    @Mock
    private IssueUseCase jiraGateway;
    @Mock
    private JiraEventPublisher jiraEventPublisher;

    private JiraService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new JiraService(jiraGateway, jiraEventPublisher);
    }

    @Test
    void createIssue_delegatesToGateway() {
        Instant fixedTime = Instant.parse("2025-09-01T20:33:33.321895Z");
        when(jiraGateway.createIssue("DEMO", "sum", "desc", fixedTime)).thenReturn("DEMO-1");
        service.createIssue("DEMO", "sum", "desc", fixedTime);
        verify(jiraGateway).createIssue("DEMO", "sum", "desc", fixedTime);
    }

    @Test
    void updateIssue_delegatesToGateway() {
        Map<String, Object> fields = Map.of("summary", "new");
        service.updateIssue("DEMO-1", fields);
        verify(jiraGateway).updateIssue(eq("DEMO-1"), eq(fields));
    }

    @Test
    void deleteIssue_delegatesToGateway() {
        service.deleteIssue("DEMO-1");
        verify(jiraGateway).deleteIssue("DEMO-1");
    }

    @Test
    void processWebhook_publishesMappedEvent() {
        Map<String, Object> payload = Map.of(
                "webhookEvent", "jira:issue_updated",
                "issue", Map.of("key", "DEMO-1")
        );
        Map<String, String> headers = Map.of(
                "x-atlassian-webhook-identifier", "123",
                "x-atlassian-webhook-retry", "0",
                "x-atlassian-webhook-flow", "Primary"
        );

        service.processWebhook(payload, headers);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(jiraEventPublisher).publish(captor.capture());
        Object sent = captor.getValue();
        assertTrue(sent instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, Object> msg = (Map<String, Object>) sent;
        assertEquals("JIRA_ISSUE_UPDATED", msg.get("type"));
        assertEquals(payload, msg.get("payload"));
        assertEquals("jira", msg.get("source"));
    }
}
