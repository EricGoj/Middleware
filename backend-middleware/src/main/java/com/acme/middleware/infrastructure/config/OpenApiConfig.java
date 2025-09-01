package com.acme.middleware.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Middleware Task Management API")
                        .version("1.0.0")
                        .description("Hexagonal architecture task management system with bidirectional Jira sync")
                        .contact(new Contact()
                                .name("Eric Quevedo")
                                .email("eric@example.com")));
    }
}
