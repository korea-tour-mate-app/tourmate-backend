package com.snowflake_guide.tourmate.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(info);
    }

    Info info = new Info()
            .version("v1.0")
            .title("TourMate API")
            .description("외국인을 위한 서울 여행 가이드 애플리케이션");
}
