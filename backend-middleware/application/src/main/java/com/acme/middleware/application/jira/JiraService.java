package com.acme.middleware.application.jira;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.acme.middleware.application.port.JiraEventPublisher;
import com.acme.middleware.application.usecase.IssueUseCase;
import com.acme.middleware.domain.model.Issue;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JiraService {

    private static final Logger log = LoggerFactory.getLogger(JiraService.class);

    private final IssueUseCase jiraGateway;
    private final JiraEventPublisher jiraEventPublisher;

    public JiraService(IssueUseCase jiraGateway, JiraEventPublisher jiraEventPublisher) {
        this.jiraGateway = jiraGateway;
        this.jiraEventPublisher = jiraEventPublisher;
    }

    public void syncIssues(List<Issue> issues) {
        try {
            //desarrollar
        } catch (Exception e) {
            log.error("Failed to sync issues: {}", e.getMessage(), e);
        }
    }

    public String createIssue(String summary, String description, String issueType, Instant dueDate, String priority) {
        try {
            String key = jiraGateway.createIssue(summary, description, issueType, dueDate, priority);
            log.info("Created Jira issue {}", key);
            return key;
        } catch (Exception e) {
            log.error("Failed to create Jira issue: {}", e.getMessage(), e);
        }
        return null;
    }

    public void updateIssue(String issueKey, Map<String, Object> fields) {
        try {
            jiraGateway.updateIssue(issueKey, fields);
            log.info("Updated Jira issue {}", issueKey);
        } catch (Exception e) {
            log.error("Failed to update Jira issue {}: {}", issueKey, e.getMessage(), e);
        }
    }

    public void deleteIssue(String issueKey) {
        try {
            jiraGateway.deleteIssue(issueKey);
            log.info("Deleted Jira issue {}", issueKey);
        } catch (Exception e) {
            log.error("Failed to delete Jira issue {}: {}", issueKey, e.getMessage(), e);
        }
    }

    public void processWebhook(Map<String, Object> payload, Map<String, String> headers) {
        try {
            // Headers are normalized to lowercase in JiraWebhookController
            String identifier = headers.getOrDefault("x-atlassian-webhook-identifier",
                    headers.getOrDefault("X-Atlassian-Webhook-Identifier", "unknown"));
            String retry = headers.getOrDefault("x-atlassian-webhook-retry",
                    headers.getOrDefault("X-Atlassian-Webhook-Retry", "0"));
            String flow = headers.getOrDefault("x-atlassian-webhook-flow",
                    headers.getOrDefault("X-Atlassian-Webhook-Flow", "Primary"));

            String rawEvent = String.valueOf(payload.getOrDefault("webhookEvent", "jira:unknown"));
            String type;
            if (rawEvent.contains("issue_created")) {
                type = "JIRA_ISSUE_CREATED";
            } else if (rawEvent.contains("issue_updated")) {
                type = "JIRA_ISSUE_UPDATED";
            } else if (rawEvent.contains("issue_deleted")) {
                type = "JIRA_ISSUE_DELETED";
            } else {
                type = "JIRA_WEBHOOK";
            }

            Map<String, Object> message = new HashMap<>();
            message.put("type", type);
            message.put("source", "jira");
            message.put("meta", Map.of(
                    "identifier", identifier,
                    "retry", retry,
                    "flow", flow,
                    "event", rawEvent
            ));
            // Forward original payload for consumers to decide how to react
            message.put("payload", payload);

            jiraEventPublisher.publish(message);
            log.info("Processed Jira webhook id={} retry={} flow={} type={}", identifier, retry, flow, type);
        } catch (Exception e) {
            log.error("Error processing Jira webhook: {}", e.getMessage(), e);
        }
    }
}
