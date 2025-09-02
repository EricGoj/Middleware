package com.acme.middleware.domain.port;

import com.acme.middleware.domain.model.Issue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IssueRepository {
    Issue save(Issue issue);
    Optional<Issue> findById(UUID issueId);
    List<Issue> findAll();
    void deleteById(UUID issueId);
    boolean existsById(UUID issueId);
}