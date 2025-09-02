package com.acme.middleware.infrastructure.jira.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class JiraConfig {

    @Bean
    public RestTemplate jiraRestTemplate(RestTemplateBuilder builder, JiraProperties props) {
        return builder
                .rootUri(props.baseUrl())
                .basicAuthentication(props.email(), props.apiToken())
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(20))
                .build();
    }
}
