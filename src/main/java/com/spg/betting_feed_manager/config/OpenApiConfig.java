package com.spg.betting_feed_manager.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI bettingFeedOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Betting Feed Manager API")
                        .description("Endpoints to accept provider feeds (Alpha/Beta) and normalize them to internal betting events.")
                        .version("v1"));
    }
}
