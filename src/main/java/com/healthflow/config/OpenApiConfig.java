package com.healthflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI healthFlowOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("HealthFlow API")
                        .description("REST API for hospital process automation")
                        .version("1.0.0"));
    }
}
