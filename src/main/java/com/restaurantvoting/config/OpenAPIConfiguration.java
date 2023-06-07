package com.restaurantvoting.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
@OpenAPIDefinition(
        info = @Info(title = "Restaurant-voting API",
                description = """
                        A voting system for deciding where to have lunch.
                        
                        Tests credentials:
                        - user1@gmail.com / 12345
                        - user2@gmail.com / abcd
                        - admin1@gmail.com / gqfqf123
                        """,
                version = "v1.0"),
        security = @SecurityRequirement(name = "basicAuth")
)
public class OpenAPIConfiguration {
    @Bean
    public GroupedOpenApi userApis() {
        return GroupedOpenApi.builder().group("user").displayName("User API`s").pathsToMatch("/user/**").build();
    }

    @Bean
    public GroupedOpenApi adminApis() {
        return GroupedOpenApi.builder().group("admin").displayName("Admin API`s").pathsToMatch("/admin/**").build();
    }

    @Bean
    public GroupedOpenApi allApis() {
        return GroupedOpenApi.builder().group("all").displayName("All API`s").pathsToMatch("/**").build();
    }
}
