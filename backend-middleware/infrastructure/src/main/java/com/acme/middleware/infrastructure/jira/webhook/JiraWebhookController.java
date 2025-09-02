package com.acme.middleware.infrastructure.jira.webhook;

import com.acme.middleware.application.jira.JiraService;
import com.acme.middleware.infrastructure.jira.config.JiraProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/jira/webhooks")
public class JiraWebhookController {

    private static final Logger log = LoggerFactory.getLogger(JiraWebhookController.class);

    private final JiraService jiraService;
    private final JiraProperties jiraProperties;

    public JiraWebhookController(JiraService jiraService, JiraProperties jiraProperties) {
        this.jiraService = jiraService;
        this.jiraProperties = jiraProperties;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> onWebhook(
            @RequestHeader Map<String, String> headers,
            @RequestBody Map<String, Object> payload
    ) {
        // Optional shared-secret validation (simple header equality). Adjust as needed.
        if (StringUtils.hasText(jiraProperties.webhookSecret())) {
            String provided = headers.getOrDefault("x-webhook-secret", "");
            if (!jiraProperties.webhookSecret().equals(provided)) {
                log.warn("Rejected Jira webhook due to invalid secret");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", "unauthorized"));
            }
        }

        // Normalize headers to lowercase to avoid case sensitivity
        Map<String, String> lowerHeaders = new HashMap<>();
        headers.forEach((k, v) -> lowerHeaders.put(k.toLowerCase(Locale.ROOT), v));

        jiraService.processWebhook(payload, lowerHeaders);
        return ResponseEntity.ok(Map.of("status", "received"));
    }
}
