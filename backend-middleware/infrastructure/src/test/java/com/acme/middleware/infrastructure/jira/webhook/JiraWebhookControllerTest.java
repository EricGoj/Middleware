package com.acme.middleware.infrastructure.jira.webhook;

import com.acme.middleware.application.jira.JiraService;
import com.acme.middleware.infrastructure.jira.config.JiraProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JiraWebhookControllerTest {

    private MockMvc mockMvc;
    private JiraService service;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(JiraService.class);
    }

    @Test
    void acceptsWebhookWithoutSecret() throws Exception {
        JiraProperties props = new JiraProperties("https://example.atlassian.net", "u", "t", "DEMO", "");
        JiraWebhookController controller = new JiraWebhookController(service, props);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        String body = "{\n  \"webhookEvent\": \"jira:issue_created\"\n}";
        mockMvc.perform(post("/jira/webhooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        verify(service).processWebhook(
                org.mockito.ArgumentMatchers.<String, Object>anyMap(),
                org.mockito.ArgumentMatchers.<String, String>anyMap()
        );
    }

    @Test
    void rejectsWebhookWithInvalidSecret() throws Exception {
        JiraProperties props = new JiraProperties("https://example.atlassian.net", "u", "t", "DEMO", "secret");
        JiraWebhookController controller = new JiraWebhookController(service, props);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        String body = "{\n  \"webhookEvent\": \"jira:issue_updated\"\n}";
        mockMvc.perform(post("/jira/webhooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-webhook-secret", "wrong")
                        .content(body))
                .andExpect(status().isUnauthorized());

        verify(service, never()).processWebhook(
                org.mockito.ArgumentMatchers.<String, Object>anyMap(),
                org.mockito.ArgumentMatchers.<String, String>anyMap()
        );
    }

    @Test
    void acceptsWebhookWithValidSecret() throws Exception {
        JiraProperties props = new JiraProperties("https://example.atlassian.net", "u", "t", "DEMO", "secret");
        JiraWebhookController controller = new JiraWebhookController(service, props);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        String body = "{\n  \"webhookEvent\": \"jira:issue_deleted\"\n}";
        mockMvc.perform(post("/jira/webhooks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-webhook-secret", "secret")
                        .content(body))
                .andExpect(status().isOk());

        verify(service).processWebhook(
                org.mockito.ArgumentMatchers.<String, Object>anyMap(),
                org.mockito.ArgumentMatchers.<String, String>anyMap()
        );
    }
}
