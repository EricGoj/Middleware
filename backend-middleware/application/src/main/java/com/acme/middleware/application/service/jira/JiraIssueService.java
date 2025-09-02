package com.acme.middleware.application.service.jira;

import com.acme.middleware.application.port.JiraIssuePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Application service for managing Jira issues with ADF format support
 */
@Service
public class JiraIssueService {

    private static final Logger log = LoggerFactory.getLogger(JiraIssueService.class);
    
    private final JiraIssuePort jiraIssuePort;

    public JiraIssueService(JiraIssuePort jiraIssuePort) {
        this.jiraIssuePort = jiraIssuePort;
    }

    /**
     * Creates a Jira issue with the specified details
     * The description will be formatted as ADF (Atlassian Document Format)
     * 
     * @param summary The issue summary/title
     * @param description The issue description (will be converted to ADF)
     * @param issueType The type of issue (default: "Task")
     * @return The created issue key
     */
    public String createIssue(String summary, String description, String issueType) {
        return createIssue(summary, description, issueType, null, null);
    }

    /**
     * Creates a Jira issue with duedate and priority support
     * 
     * @param summary The issue summary/title
     * @param description The issue description (will be converted to ADF)
     * @param issueType The type of issue (default: "Task")
     * @param duedate The due date in YYYY-MM-DD format
     * @param priority The priority name (e.g., "High", "Medium", "Low")
     * @return The created issue key
     */
    public String createIssue(String summary, String description, String issueType, String duedate, String priority) {
        log.info("Creating Jira issue with summary: {}, type: {}, duedate: {}, priority: {}", 
                summary, issueType, duedate, priority);
        
        if (summary == null || summary.trim().isEmpty()) {
            throw new IllegalArgumentException("Issue summary cannot be null or empty");
        }
        
        if (issueType == null || issueType.trim().isEmpty()) {
            issueType = "Task"; // Default issue type
        }
        
        try {
            String issueKey = jiraIssuePort.createIssueWithAdf(summary, description, issueType, duedate, priority);
            log.info("Successfully created Jira issue: {}", issueKey);
            return issueKey;
        } catch (Exception e) {
            log.error("Failed to create Jira issue with summary: {}", summary, e);
            throw new RuntimeException("Failed to create Jira issue: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a Jira Task issue with the specified summary and description
     * 
     * @param summary The issue summary/title
     * @param description The issue description (will be converted to ADF)
     * @return The created issue key
     */
    public String createTask(String summary, String description) {
        return createIssue(summary, description, "Task");
    }
}