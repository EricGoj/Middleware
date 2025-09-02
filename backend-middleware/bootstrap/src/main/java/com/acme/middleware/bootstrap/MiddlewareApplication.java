package com.acme.middleware.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.acme.middleware")
@EntityScan("com.acme.middleware.infrastructure.persistence.entity")
@EnableJpaRepositories("com.acme.middleware.infrastructure.persistence.repository")
@EnableAsync
@ConfigurationPropertiesScan(basePackages = "com.acme.middleware.infrastructure.jira.config")
public class MiddlewareApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiddlewareApplication.class, args);
    }
}