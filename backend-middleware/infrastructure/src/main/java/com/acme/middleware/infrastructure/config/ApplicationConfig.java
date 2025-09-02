package com.acme.middleware.infrastructure.config;

import com.acme.middleware.application.mapper.IssueApplicationMapper;
import com.acme.middleware.application.service.*;
import com.acme.middleware.application.usecase.*;
import com.acme.middleware.domain.port.DomainEventPublisher;
import com.acme.middleware.domain.port.EventRepository;
import com.acme.middleware.domain.port.IssueRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public IssueApplicationMapper issueApplicationMapper() {
        return new IssueApplicationMapper();
    }

    @Bean
    public CreateIssueUseCase createIssueUseCase(IssueRepository issueRepository, 
                                             EventRepository eventRepository,
                                             DomainEventPublisher eventPublisher,
                                             IssueApplicationMapper mapper) {
        return new CreateIssueService(issueRepository, eventRepository, eventPublisher, mapper);
    }

    @Bean
    public GetIssueUseCase getIssueUseCase(IssueRepository issueRepository, 
                                       IssueApplicationMapper mapper) {
        return new GetIssueService(issueRepository, mapper);
    }

    @Bean
    public ListIssuesUseCase listIssuesUseCase(IssueRepository issueRepository, 
                                           IssueApplicationMapper mapper) {
        return new ListIssuesService(issueRepository, mapper);
    }

    @Bean
    public UpdateIssueUseCase updateIssueUseCase(IssueRepository issueRepository,
                                             DomainEventPublisher eventPublisher,
                                             IssueApplicationMapper mapper) {
        return new UpdateIssueService(issueRepository, eventPublisher, mapper);
    }

    @Bean
    public DeleteIssueUseCase deleteIssueUseCase(IssueRepository issueRepository,
                                            DomainEventPublisher eventPublisher) {
        return new DeleteIssueService(issueRepository, eventPublisher);
    }
}
