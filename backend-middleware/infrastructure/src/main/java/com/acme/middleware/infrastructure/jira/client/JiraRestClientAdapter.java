package com.acme.middleware.infrastructure.jira.client;

import com.acme.middleware.application.port.JiraIssuePort;
import com.acme.middleware.application.usecase.IssueUseCase;
import com.acme.middleware.infrastructure.jira.config.JiraProperties;
import com.acme.middleware.infrastructure.jira.dto.JiraCreateIssueRequest;
import com.acme.middleware.infrastructure.jira.dto.JiraCreateIssueResponse;
import com.acme.middleware.infrastructure.jira.dto.JiraIssueFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@Component
public class JiraRestClientAdapter implements IssueUseCase, JiraIssuePort {

    private static final Logger log = LoggerFactory.getLogger(JiraRestClientAdapter.class);

    private final RestTemplate jiraRestTemplate;
    private final JiraProperties props;

    public JiraRestClientAdapter(RestTemplate jiraRestTemplate, JiraProperties props) {
        this.jiraRestTemplate = jiraRestTemplate;
        this.props = props;
    }

    @Override
    public String createIssue(String summary, String description, String issueType, Instant dueDate) {
        return createIssueWithAdf(summary, description, issueType, dueDate.toString(), issueType);
    }

    /**
     * Creates a Jira issue using ADF with duedate and priority support
     */
    public String createIssueWithAdf(String summary, String description, String issueType, String duedate, String priority) {
        String url = "/rest/api/3/issue";
        
        JiraIssueFields fields = JiraIssueFields.of(
            props.projectKey(),
            summary,
            description != null ? description : "",
            issueType,
            duedate,
            priority
        );
        
        JiraCreateIssueRequest request = JiraCreateIssueRequest.of(fields);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(MediaType.parseMediaTypes("application/json"));

        try {
            ResponseEntity<JiraCreateIssueResponse> response = jiraRestTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    JiraCreateIssueResponse.class
            );
            
            JiraCreateIssueResponse responseBody = response.getBody();
            if (responseBody != null && responseBody.key() != null) {
                log.info("Successfully created Jira issue: {} in project {}", responseBody.key(), props.projectKey());
                return responseBody.key();
            }
            throw new IllegalStateException("Jira create issue response missing key");
        } catch (RestClientException e) {
            log.error("Error creating Jira issue in project {}: {}", props.projectKey(), e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateIssue(String issueKey, Map<String, Object> fields) {
        String url = "/rest/api/3/issue/" + issueKey;
        Map<String, Object> body = Map.of("fields", fields);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            jiraRestTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(body, headers), Void.class);
        } catch (RestClientException e) {
            log.error("Error updating Jira issue {}: {}", issueKey, e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteIssue(String issueKey) {
        String url = "/rest/api/3/issue/" + issueKey;
        try {
            jiraRestTemplate.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);
        } catch (RestClientException e) {
            log.error("Error deleting Jira issue {}: {}", issueKey, e.getMessage());
            throw e;
        }
    }
}
