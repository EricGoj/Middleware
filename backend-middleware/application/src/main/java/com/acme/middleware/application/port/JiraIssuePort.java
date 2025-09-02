package com.acme.middleware.application.port;

/**
 * Port for Jira issue operations with enhanced ADF support
 */
public interface JiraIssuePort {
    
    /**
     * Creates a Jira issue with ADF format description
     * 
     * @param summary The issue summary/title
     * @param description The issue description (will be converted to ADF)
     * @param issueType The type of issue
     * @param duedate The due date in YYYY-MM-DD format (optional)
     * @param priority The priority name (optional)
     * @return The created issue key
     */
    String createIssueWithAdf(String summary, String description, String issueType, String duedate, String priority);
}