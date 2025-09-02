package com.acme.middleware.application.service.jira;

import com.acme.middleware.application.jira.JiraService;
import com.acme.middleware.application.usecase.ProcessJiraSyncEventUseCase;
import com.acme.middleware.domain.model.Event;
import com.acme.middleware.domain.model.Issue;
import com.acme.middleware.domain.port.IssueRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessJiraSyncEventService implements ProcessJiraSyncEventUseCase {

    private final IssueRepository issueRepository;
    private final Logger log = LoggerFactory.getLogger(ProcessJiraSyncEventService.class);
    private final JiraService jiraService;  
    
    public ProcessJiraSyncEventService(IssueRepository issueRepository, JiraService jiraService) {
        this.issueRepository = issueRepository;
        this.jiraService = jiraService;
    }
    
    @Override
    public void execute(Event event) {

        Issue issue = issueRepository.findById(event.getEntityId()).get();
        
        if (event.getStatus().equals("PENDING")) {
            try {
                String jiraKey = jiraService.createIssue(issue.getTitle(), issue.getDescription(), issue.getPriority(), issue.getDueDate());
                issue.updateBusinessKey(jiraKey);
                issue.updateSyncStatus("DONE");
                issueRepository.save(issue);
            } catch (Exception e) {
                log.error("Error creating Jira issue: {}", e.getMessage(), e);
            }
        }
        
    }
}
