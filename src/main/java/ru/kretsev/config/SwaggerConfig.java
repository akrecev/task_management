package ru.kretsev.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Swagger (OpenAPI) documentation.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configures the OpenAPI documentation for the Task Management API.
     *
     * @return the configured {@link OpenAPI} instance
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Management API")
                        .version("1.0")
                        .description("API для управления задачами"));
    }
}
