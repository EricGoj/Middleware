package com.acme.middleware.bootstrap;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.acme.middleware")
@EntityScan({"com.acme.middleware.infrastructure.persistence.entity", "com.acme.middleware.infrastructure.sync.entity"})
@EnableJpaRepositories({"com.acme.middleware.infrastructure.persistence.repository", "com.acme.middleware.infrastructure.sync.repository"})
@EnableAsync
@EnableScheduling
@ConfigurationPropertiesScan(basePackages = "com.acme.middleware.infrastructure.jira.config")
public class MiddlewareApplication {

    public static void main(String[] args) {
        // Load .env file before Spring Boot starts
        loadEnvironmentVariables();
        
        SpringApplication.run(MiddlewareApplication.class, args);
    }
    
    private static void loadEnvironmentVariables() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();

            // Set environment variables from .env file
            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                
                // Only set if not already defined in system environment
                if (System.getenv(key) == null) {
                    System.setProperty(key, value);
                }
            });
            
        } catch (Exception e) {
            // Log warning but don't fail startup if .env file is missing
            System.err.println("Warning: Could not load .env file: " + e.getMessage());
        }
    }
}