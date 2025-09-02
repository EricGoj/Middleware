package com.acme.middleware.infrastructure.jira.client;

import com.acme.middleware.infrastructure.jira.config.JiraProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class JiraRestClientAdapterTest {

    private RestTemplate restTemplate;
    private JiraProperties props;
    private JiraRestClientAdapter adapter;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        props = new JiraProperties("https://example.atlassian.net", "u", "t", "DEMO", "");
        adapter = new JiraRestClientAdapter(restTemplate, props);
    }

    @Test
    void createIssue_postsCorrectPayload() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(Map.of("key", "DEMO-1")));

        String key = adapter.createIssue("Summary", "Desc", "Task", Instant.now());

        assertThat(key).isEqualTo("DEMO-1");

        @SuppressWarnings("unchecked")
        ArgumentCaptor<HttpEntity<Map<String, Object>>> captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(eq("/rest/api/3/issue"), eq(HttpMethod.POST), captor.capture(), eq(Map.class));

        HttpEntity<Map<String, Object>> sent = captor.getValue();
        assertThat(sent.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        Map<String, Object> body = sent.getBody();
        assertThat(body).isNotNull();
        Map<String, Object> fields = (Map<String, Object>) body.get("fields");
        assertThat(fields).containsEntry("summary", "Summary");
        assertThat(fields).containsEntry("description", "Desc");
        assertThat((Map<String, Object>) fields.get("project")).containsEntry("key", "DEMO");
        assertThat((Map<String, Object>) fields.get("issuetype")).containsEntry("name", "Task");
        assertThat(fields).containsKey("dueDate");
    }

    @Test
    void updateIssue_putsFields() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(ResponseEntity.noContent().build());

        adapter.updateIssue("DEMO-1", Map.of("summary", "New"));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<HttpEntity<Map<String, Object>>> captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(eq("/rest/api/3/issue/DEMO-1"), eq(HttpMethod.PUT), captor.capture(), eq(Void.class));
        Map<String, Object> body = captor.getValue().getBody();
        assertThat(body).isNotNull();
        assertThat((Map<String, Object>) body.get("fields")).containsEntry("summary", "New");
    }

    @Test
    void deleteIssue_callsDelete() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Void.class)))
                .thenReturn(ResponseEntity.noContent().build());

        adapter.deleteIssue("DEMO-2");

        verify(restTemplate).exchange(eq("/rest/api/3/issue/DEMO-2"), eq(HttpMethod.DELETE), eq(HttpEntity.EMPTY), eq(Void.class));
    }
}
